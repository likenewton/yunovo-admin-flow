package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.MurmurHash3;
import cn.yunovo.iov.fc.common.utils.MurmurHash3.LongPair;
import cn.yunovo.iov.fc.dao.ICcApkWhitelistUsedReportInfoMapper;
import cn.yunovo.iov.fc.model.entity.CcApkWhitelistLastreportInfo;
import cn.yunovo.iov.fc.model.entity.CcApkWhitelistUsedReportInfo;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.api.WhitelistsReportForm;
import cn.yunovo.iov.fc.service.ICcApkWhitelistLastreportInfoService;
import cn.yunovo.iov.fc.service.ICcApkWhitelistUsedReportInfoService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-07-25
 */
@Slf4j
@Service
public class CcApkWhitelistUsedReportInfoServiceImpl extends ServiceImpl<ICcApkWhitelistUsedReportInfoMapper, CcApkWhitelistUsedReportInfo> implements ICcApkWhitelistUsedReportInfoService {

	@Autowired
	private ICcApkWhitelistLastreportInfoService iCcApkWhitelistLastreportInfoService;
	
	private final Long MAX_VALUE_32 = 4294967295L;
	private final String ID_PREFIX = "APK_";
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	@Qualifier("clwTransactionManager")
	private DataSourceTransactionManager clwTransactionManager;
	
	@Override
	public void report(WhitelistsReportForm form) {

		//判断是否我司流量卡
		CcGprsCard card = iCcGprsCardService.getByIccid(form.getIccid());
		if(card == null) {
			log.warn("[report][iccid not found]params={}", form.buildJsonString());
			throw new BusinessException(400, "iccid not found");
		}
		
		//只有卡商为5的卡才支持apk白名单
		if(card.getCard_type() != 5) {
			log.warn("[report][iccid unsupported]params={},card={}", form.buildJsonString(), iCcGprsCardService.cacheCardInfo(card));
			throw new BusinessException(3, "iccid unsupported");
		}
		
		//从缓存中获取该流量卡最后一次上报数据,如果缓存中没有则从数据库中获取
		CcApkWhitelistLastreportInfo lastInfo = iCcApkWhitelistLastreportInfoService.getLastReportInfoInCache(card.getCard_iccid());
		
		//首次上报、清除缓存、恢复出厂设置 
		boolean isZeroNonce = this.isZero(card.getCard_iccid(), form.getNonce());
		
		//如果未找到最后一次上报信息，则认为用户是首次上报
		if(lastInfo == null && !isZeroNonce) {
			throw new FormValidateException("invalid nonce", "nonce", form.buildJsonString());
		}
		
		Long prev_yunovo_gprs_month = 0L;
		Long prev_org_gprs_month = 0L;
		Long yunovo_gprs_month = form.getYunovo_gprs_month();
		Long org_gprs_month = form.getOrg_gprs_month();
		if(lastInfo != null){
			
			if(!isZeroNonce && form.getNonce() - lastInfo.getNonce() != 0) {
				log.warn("[report][Nonce 异常]params={lastInfo:{},form:{}}", lastInfo.cacheJsonSring(), form.buildJsonString());
			}
			
			if(!isZeroNonce) {
			
				prev_yunovo_gprs_month = lastInfo.getYunovo_gprs_month();
				prev_org_gprs_month = lastInfo.getOrg_gprs_month();
				
				//32bit 限制
				if(yunovo_gprs_month - prev_yunovo_gprs_month < 0 ) {
					
					if(form.getNonce() - lastInfo.getNonce() == 0) {
						yunovo_gprs_month = prev_yunovo_gprs_month + (MAX_VALUE_32 - prev_yunovo_gprs_month + yunovo_gprs_month);
					}else {
						yunovo_gprs_month = prev_yunovo_gprs_month + (0 + yunovo_gprs_month);
					}
					
				}
				
				//32bit 限制
				if(org_gprs_month - prev_org_gprs_month < 0) {
					
					if(form.getNonce() - lastInfo.getNonce() == 0) {
						org_gprs_month = prev_org_gprs_month + (MAX_VALUE_32 - prev_org_gprs_month + org_gprs_month);
					}else {
						org_gprs_month = prev_org_gprs_month + (0 + org_gprs_month);
					}
					
				}
			}
			
		}else {
			if(!isZeroNonce) { //nonce 错误,打印日志，不做其他处理
				log.warn("[report][Nonce 异常]params={form:{}}", form.buildJsonString());
			}
		}
		
		//判断nonce是否有效,需要考虑到网络情况
		
		
		/*if(lastInfo == null) {
			
			//从数据库中获取最后一次上报数据
			lastInfo = iCcApkWhitelistLastreportInfoService.getLastReportInfoInDB(card.getCard_iccid());
			
			if(!this.verifyNonce(lastInfo, form.getIccid(), form.getNonce(), true)) {
				throw new FormValidateException("invalid nonce", "nonce", form.buildJsonString());
			}
			
		}else {
			if(!this.verifyNonce(lastInfo, form.getIccid(), form.getNonce(), false)) {
				
				//从数据库中拉取最后一次上报数据
				lastInfo = iCcApkWhitelistLastreportInfoService.getLastReportInfoInDB(card.getCard_iccid());
				
				if(!this.verifyNonce(lastInfo, form.getIccid(), form.getNonce(), true)) {
					throw new FormValidateException("invalid nonce", "nonce", form.buildJsonString());
				}
			}
		}*/
		
		//事务
		//事务定义
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus transactionStatus = clwTransactionManager.getTransaction(definition);
		
		try {
			
			CcApkWhitelistUsedReportInfo info = new CcApkWhitelistUsedReportInfo();
			info.setIccid(card.getCard_iccid());
			info.setId(makeId(card.getCard_iccid()));
			info.setCard_id(card.getCard_id());
			info.setOrg_id(card.getOrg_id());
			info.setOrg_gprs_month(org_gprs_month);
			info.setYunovo_gprs_month(yunovo_gprs_month);
			info.setPrev_org_gprs_month(prev_org_gprs_month);
			info.setPrev_yunovo_gprs_month(prev_yunovo_gprs_month);
			
			info.setNonce(form.getNonce());
			info.setSn(form.getSn());
			info.setCreate_datetime(DateUtil.nowStr());
			
			//保存上报数据
			if(!this.save(info)) {
				log.error("[report][save report faild]params={form:{},info:{}}", form.buildJsonString(),JSONObject.toJSONString(info));
				throw new BusinessException(0, "save error");
			}
			
			//保存上报数据
			if(!this.saveLastReportInfo(form, lastInfo, card)) {
				log.error("[report][save last report info faild]params={card:{},form:{},lastInfo:{}}", JSONObject.toJSONString(card), form.buildJsonString(), JSONObject.toJSONString(lastInfo));
				throw new BusinessException(0, "save error");
			}
			clwTransactionManager.commit(transactionStatus);
		}catch(BusinessException e){
			clwTransactionManager.rollback(transactionStatus);
			throw e;
		}catch(Exception e) {
			clwTransactionManager.rollback(transactionStatus);
			log.error("[report][exception]params={card:{},form:{},lastInfo:{}}", JSONObject.toJSONString(card), form.buildJsonString(), JSONObject.toJSONString(lastInfo));
			throw new BusinessException(0, "error");
		}
		
		log.info("[report][ok]params={}",form.buildJsonString());
	}

	public String makeId(String iccid) {
		
		return ID_PREFIX + iccid + "_" + System.currentTimeMillis();
	}
	
	private boolean saveLastReportInfo(WhitelistsReportForm form, CcApkWhitelistLastreportInfo lastInfo, CcGprsCard card) {

		if(lastInfo == null) {
			return iCcApkWhitelistLastreportInfoService.saveInfo(card.getCard_id(), card.getCard_iccid(), form.getNonce(),form.getOrg_gprs_month(), form.getYunovo_gprs_month(), form.getSn());
		}else {
			
			lastInfo.setUpdate_datetime(DateUtil.nowStr());
			lastInfo.setYunovo_gprs_month(form.getYunovo_gprs_month());
			lastInfo.setOrg_gprs_month(form.getOrg_gprs_month());
			lastInfo.setSn(form.getSn());
			lastInfo.setNonce(form.getNonce());
			
			iCcApkWhitelistLastreportInfoService.updateInfo(lastInfo);
			
			return true;
		}
	}

	public boolean isZero(String iccid, Long nonce) {
		
		if(nonce == null) {
			return false;
		}
		
		//判断是否是首次上报或设备恢复出厂设置或缓存清空时数据置0 后的首次上报
		String nonceStr = iccid + ",0,0";
		
		byte[] keys = nonceStr.getBytes();
		LongPair out = new LongPair();
		MurmurHash3.murmurhash3_x64_128(keys, 0, keys.length, 0, out);
		
		return out.val1 ==  nonce;
	}

	public boolean verifyNonce(CcApkWhitelistLastreportInfo info, String iccid, Long nonce, boolean is_zero) {
		
		if(info != null && info.getNonce() - nonce == 0) {
			
			return true;
		}else {
			
			if(!is_zero) {
				return false;
			}
			
			//判断是否是首次上报或设备恢复出厂设置或缓存清空时数据置0 后的首次上报
			String nonceStr = iccid + ",0,0";
			byte[] keys = nonceStr.getBytes();
			LongPair out = new LongPair();
			MurmurHash3.murmurhash3_x64_128(keys, 0, keys.length, 0, out);
			
			return out.val1 ==  nonce;
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
	}
}
