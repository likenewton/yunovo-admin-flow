package cn.yunovo.iov.fc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.dao.ICcGprsGiftMapper;
import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsGift;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.GprsGiftForm;
import cn.yunovo.iov.fc.model.form.GprsPackForm;
import cn.yunovo.iov.fc.model.result.CardGiftBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsGiftService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * GPRS流量赠送表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
@Slf4j
public class CcGprsGiftServiceImpl extends ServiceImpl<ICcGprsGiftMapper, CcGprsGift> implements ICcGprsGiftService {

	private static final Map<String, String> arr_ret = new HashMap<>();
	
	static {
		arr_ret.put("ok", "流量卡重置成功");
		arr_ret.put("notfound", "非本公司的卡不可赠送");
		arr_ret.put("fail", "流量赠送失败");
		arr_ret.put("success", "流量赠送成功");
		arr_ret.put("notyour", "非您机构的卡不可赠送");
		arr_ret.put("unactivated", "暂未激活不可赠送");
		arr_ret.put("exception", "流量赠送异常");
		arr_ret.put("gprsAllotFail", "流量分配失败");
		arr_ret.put("gprsCalculateFail", "流量计算失败");
	}
	
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsGiftMapper iCcGprsGiftMapper;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Autowired
	private ICcGprsPayService iCcGprsPayService;
	
	@Autowired
	private ICcGprsAllotService iCcGprsAllotService;
	
	@Autowired
	private ICcGprsAllotMapper iCcGprsAllotMapper;
	
	@Autowired
	@Qualifier("clwTransactionManager")
	private DataSourceTransactionManager clwTransactionManager;
	
	@Override
	public PageData<CcGprsGift, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String date_start, String date_end, LoginInfo info) {
		

		Page<CcGprsGift> page = form.build(CcGprsGift.class, null, "time_added");
		PageData<CcGprsGift, Object> returnData = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}

		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}
		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		List<CcGprsGift> records = iCcGprsGiftMapper.getItemsPage(page, org_id, card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> userMap = iCcUserService.userMap();
			CcOrg tt = null;
			for (CcGprsGift ccGprsGift : records) {
				tt = orgs.get(String.valueOf(ccGprsGift.getOrg_id()));
				ccGprsGift.setOrg_name(tt == null ? "" : tt.getName());
				ccGprsGift.setCreate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsGift.getCreate_by()), ccGprsGift.getCreate_by()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	@Override
	public List<CardGiftBean> gift(GprsGiftForm form, LoginInfo info) {
		
		//CcUser user = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		String iccidTemp = form.getIccids().replaceAll("\r\n", "\n");
		String[] arr_iccids = iccidTemp.split("\n");
		
		if(arr_iccids.length > 200) {
			throw new FormValidateException("同一批次赠送ICCID个数不能超过200个！","iccids",form.buildJsonString());
		}
		
		Date time_expire = CcRealnameServiceImpl.liveMonthConverToExpireTime(form.getLive_month(), DateUtil.now());
		// 初始套餐信息
		CcGprsGift pack = new CcGprsGift();
//		pack.setUser_id(info.getId());
//		pack.setUser_id(0);
		pack.setCreate_by(info.getLoginName());
		pack.setGprs_amount(form.getGprs_amount());
		pack.setAllot_month(form.getAllot_month());
		pack.setAllot_value(GprsPackForm.computeAllotValue(form.getGprs_amount(), form.getAllot_month()));
		pack.setAllot_reset(form.getAllot_reset());
		pack.setLive_month(form.getLive_month());
		pack.setGift_name(form.getGift_name());
		pack.setTime_added(DateUtil.nowStr());
		pack.setTime_expire(DateFormatUtils.format(time_expire,"yyyy-MM-dd HH:mm:ss"));
		CcGprsCard card = null;
		List<CardGiftBean> returnData = new ArrayList<>();
		CardGiftBean temp = null;
		
		Set<String> iccids = new LinkedHashSet<>(Arrays.asList(arr_iccids));
		for (String iccid : iccids) {
			
			iccid = iccid.trim();
			if(StringUtils.isEmpty(iccid)) {
				continue;
			}
			temp = new CardGiftBean();
			if(StringUtils.length(iccid) > 20) {
				temp.setIccid(iccid);
				temp.setMsg(arr_ret.get("notfound"));
				temp.setRet("2");
				returnData.add(temp);
				continue;
			}
			try {
				
				card = iCcGprsCardService.getByIccid(iccid);
				
				if(card == null) {
					temp.setIccid(iccid);
					temp.setMsg(arr_ret.get("notfound"));
					temp.setRet("2");
					returnData.add(temp);
					continue;
				}
				
				if(!orgs.containsKey(card.getOrg_id().toString())) {
					temp.setIccid(iccid);
					temp.setMsg(arr_ret.get("notyour"));
					temp.setRet("2");
					returnData.add(temp);
					continue;
				}
				
				/**
				 * 流量卡充值赠送
				 */
				String topupresutl = this._topup(card, pack, form.getPack_mode());
				temp.setIccid(iccid);
				temp.setRet(StringUtils.equals("success", topupresutl) ? "0":"1");
				temp.setMsg(arr_ret.get(topupresutl));
			} catch (Exception e) {
				
				log.error("[gift][exception]params={form:{},user:{}},exception={}",form.buildJsonString(), JSONObject.toJSONString(info), ExceptionUtils.getStackTrace(e));
				temp.setIccid(iccid);
				temp.setRet("1");
				temp.setMsg(arr_ret.get("exception"));
			}
			returnData.add(temp);
		}
		return returnData;
		
	}
	
	/**
	 * 充值赠送
	 * @param card 流量卡信息
	 * @param pack 流量赠送信息
	 * @param pack_model 套餐模式0为叠加1为延期
	 * @return 处理结果
	 */
	public String _topup(CcGprsCard card, CcGprsGift pack, Integer pack_model) {
		
		int is_update = 0;
		/*
		 * 循环判断当前流量接口是否在计算修改中，如果有缓存程序则暂停1秒执行
		 */
		String mkey = FcConstant.memResKey(String.format(FcConstant.CARD_LOCK_CACHEKEY, card.getCard_iccid()));
		while(StringUtils.equals(jedisPoolUtil.get(mkey), "1")) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			
			is_update = 1;
		}
		
		if(is_update == 1) {
			card = iCcGprsCardService.getByIccid(card.getCard_iccid());
		}
		
		//事务定义
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus transactionStatus = clwTransactionManager.getTransaction(definition);
		CcGprsPay pay = null;
		try {
			
			pack.setCard_iccid(card.getCard_iccid());
			pack.setCard_id(card.getCard_id());
			/**
			 * 增加赠送记录
			 */
			if(!this.save(pack)) {
				log.error("[_topup][保存流量赠送记录失败]params={card:{},pack:{}}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack));
				clwTransactionManager.rollback(transactionStatus);
				return "fail";
			}
			
			/**
			 * 增加流水支付记录
			 */
			pay = new CcGprsPay();
			pay.setOrg_id(card.getOrg_id());
			pay.setCard_id(card.getCard_id());
			pay.setGprs_amount(pack.getGprs_amount());
			pay.setGprs_price(new BigDecimal(0));
			pay.setLive_month(pack.getLive_month());
			pay.setPay_sn(CcRealnameServiceImpl.makePaySn());
			pay.setPay_memo("系统赠送");
			pay.setPay_method(Short.valueOf("0"));
			pay.setTransfer_id("system-give");
			pay.setIs_paid(Short.valueOf("1"));
			pay.setTime_paid(pack.getTime_added());
			pay.setTime_added(pack.getTime_added());
			pay.setTime_expire(pack.getTime_expire());
			
			if(!iCcGprsPayService.save(pay)) {
				log.error("[_topup][增加流水支付记录失败]params={card:{},pay:{}}", JSONObject.toJSONString(card), JSONObject.toJSONString(pay));
				clwTransactionManager.rollback(transactionStatus);
				return "fail";
			}
			
			/**
			 * 判断充值卡机构充值方式是否为仅增加充值有效时间，而不是增加充值套餐
			 */
			boolean add_gprs_allot = true;//增加流量分配套餐
			Integer allot_id = null;
			Integer rows = null;
			if(card.getCard_type() == 4 || pack_model == 1) {//定向流量卡或套餐模式为延期，则执行延期操作
				
				/**
				 * 获取与判断充值卡是否已拥有此套餐，如果有则增加充值有效周期
				 */
				allot_id = iCcGprsAllotMapper.getAllotIdByCardidAndValueAndTimeexpire(card.getCard_id(), pack.getAllot_value());
				if(allot_id != null) {
					if(pack.getAllot_month() == 1 && pack.getGprs_amount() < 1) {//如果是一次性分配且分配流量小于1M，那么充值则只需修改有效周期
						rows = iCcGprsAllotMapper.updateTimeExpireById(pack.getLive_month(), allot_id);
					}else {//其他套餐情况则修改流量总值、增加分配月数、增加有效周期
						rows = iCcGprsAllotMapper.UpdateGprsAmountAndAllotMonthAndTimeExById(pack.getGprs_amount(), pack.getAllot_month(), pack.getLive_month(), allot_id);
					}
					
					if(rows < 1) {
						log.error("[_topup][Update gprs allot failed]params={card:{},pack:{}}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack));
						clwTransactionManager.rollback(transactionStatus);
						return "fail";
					}
					
					add_gprs_allot = false;
				}
			}
			
			if(add_gprs_allot) {
				CcGprsAllot allot = new CcGprsAllot();
				allot.setCard_id(card.getCard_id());
				allot.setGprs_amount(pack.getGprs_amount());
				allot.setAllot_month(pack.getAllot_month());
				allot.setAllot_value(pack.getAllot_value());
				allot.setAllot_reset(pack.getAllot_reset());
				allot.setAssigned_month(0);
				allot.setTime_added(pack.getTime_added());
				allot.setTime_expire(pack.getTime_expire());
				
				if(!iCcGprsAllotService.save(allot)) {
					
					log.error("[_topup][Insert gprs allot failed]params={card:{},allot:{}}", JSONObject.toJSONString(card), JSONObject.toJSONString(allot));
					clwTransactionManager.rollback(transactionStatus);
					return "fail";
				}
			}
			
			clwTransactionManager.commit(transactionStatus);//所有流程处理正确提交时务成功到数据库
			
		}catch(Exception e) {
			
			log.error("[_topup][exception]params={card:{},pack:{}},exception={}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack), ExceptionUtils.getStackTrace(e));
			clwTransactionManager.rollback(transactionStatus);
			return "fail";
		}
		
		try {
			iCcGprsAllotService.gprsAllot(card.getCard_id());//根据套餐分配流量
			
		} catch (Exception e) {
			log.error("[_topup][exception]params={card:{},pack:{}},exception={}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack), ExceptionUtils.getStackTrace(e));
			return "gprsAllotFail";
		}
		
		try {
			/*
			 * 流量卡的可使用流量增加,并重新计算可使用流量
			 */
			card.setTime_paid(DateUtil.nowStr());
			GprsCalculateBean gprs = new GprsCalculateBean();
			gprs.setMonth(card.getUsed_month() + ICcGprsAllotService.GPRS_CAL_OFFSET);
			gprs.setTotal(card.getUsed_total() + ICcGprsAllotService.GPRS_CAL_OFFSET);
			gprs.setIs_unicom(false);
			gprs.setOpen_card(true);
			iCcGprsAllotService.gprsCalculate(card, gprs);
		} catch (Exception e) {
			log.error("[_topup][exception]params={card:{},pack:{}},exception={}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack), ExceptionUtils.getStackTrace(e));
			return "gprsCalculateFail";
		}
		
		/**
		 * 充值成功通知队列
		 */
		try {
			JSONObject pay_queue = new JSONObject();
			pay_queue.put("payid", pay.getPack_id());
			pay_queue.put("iccid", card.getCard_iccid());
			jedisPoolUtil.lpush(FcConstant.PAY_QUEUE_CACHEKEY, pay_queue.toJSONString());
		} catch (Exception e) {
			log.warn("[_topup][warn]params={card:{},pack:{}},exception={}", JSONObject.toJSONString(card), JSONObject.toJSONString(pack), ExceptionUtils.getStackTrace(e));
		}
		return "success";
	}

}
