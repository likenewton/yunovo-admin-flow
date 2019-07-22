package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.dao.ICcGprsMoveMapper;
import cn.yunovo.iov.fc.dao.ICcGprsPayMapper;
import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsMove;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.GprsMoveForm;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsMoveService;
import cn.yunovo.iov.fc.service.ICcGprsValueService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
 * GPRS流量迁移表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
@Slf4j
public class CcGprsMoveServiceImpl extends ServiceImpl<ICcGprsMoveMapper, CcGprsMove> implements ICcGprsMoveService {

	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcGprsMoveMapper iCcGprsMoveMapper;
	
	@Autowired
	private ICcGprsPayMapper iCcGprsPayMapper;
	
	@Autowired
	private ICcGprsAllotService iCcGprsAllotService;
	
	@Autowired
	private ICcCardLogService iCcCardLogService;
	
	@Autowired
	private ICcGprsAllotMapper iCcGprsAllotMapper;
	
	@Autowired
	private ICcGprsValueService iCcGprsValueService;
	
	@Autowired
	@Qualifier("clwTransactionManager")
	private DataSourceTransactionManager clwTransactionManager;
	
	@Override
	public PageData<CcGprsMove, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String old_card_iccid, String date_start, String date_end, LoginInfo info) {
		
		Page<CcGprsMove> page = form.build(CcGprsMove.class, null, "time_added");
		PageData<CcGprsMove, Object> returnData = new PageData<>();
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
		
		List<CcGprsMove> records = iCcGprsMoveMapper.getItemsPage(page, org_id, card_iccid, old_card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> userMap = iCcUserService.userMap();
			for (CcGprsMove ccGprsMove : records) {

				ccGprsMove.setOrg_name(orgs.get(String.valueOf(ccGprsMove.getNew_orgid())).getName());
				ccGprsMove.setOld_org_name(orgs.get(String.valueOf(ccGprsMove.getOld_orgid())).getName());
				ccGprsMove.setCreate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsMove.getCreate_by()), ccGprsMove.getCreate_by()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
		
	}
	
	@Override
	public void move(GprsMoveForm form, LoginInfo info) {
		
		CcGprsCard old_card = iCcGprsCardService.getByIccid(form.getOld_card_iccid());
		
		
		if(StringUtils.equals(form.getNew_card_iccid(), form.getOld_card_iccid())) {
			throw new FormValidateException("新卡ICCID不能等于旧卡ICCID", "new_card_iccid", form.buildJsonString());
		}
		
		if(old_card == null) {
			throw new FormValidateException("请输入正确的旧卡ICCID", "old_card_iccid", form.buildJsonString());
		}
		
		if(old_card.getCard_type() == 4) {
			
			if(!CollectionUtils.isEmpty(iCcGprsPayMapper.isBuyOtherGprs(old_card.getCard_id()))) {
				
				throw new FormValidateException("抱歉，此定向流量卡已充值智网规化的套餐，导致迁移失败！", "old_card_iccid", form.buildJsonString());
			}
		}
		
		CcGprsCard new_card = iCcGprsCardService.getByIccid(form.getNew_card_iccid());
		if(new_card == null) {
			throw new FormValidateException("请输入正确的新卡ICCID", "new_card_iccid", form.buildJsonString());
		}
		
		CcGprsMove move = new CcGprsMove();
//		move.setUser_id(info.getId());
//		move.setUser_id(0);
		move.setCreate_by(info.getLoginName());
		move.setOld_cardid(old_card.getCard_id());
		move.setNew_cardid(new_card.getCard_id());
		move.setOld_iccid(old_card.getCard_iccid());
		move.setNew_iccid(new_card.getCard_iccid());
		move.setOld_orgid(old_card.getOrg_id());
		move.setNew_orgid(new_card.getOrg_id());
		move.setMove_memo(form.getMove_memo());
		move.setTime_added(DateUtil.nowStr());
		if(!this.insert(move, old_card.getCard_id(), new_card.getCard_id())) {
			throw new BusinessException(-1, "抱歉，流量卡流量迁移失败！");
		}
		
		GprsCalculateBean gprs = new GprsCalculateBean();
		gprs.setMonth(old_card.getUnicom_month() + ICcGprsAllotService.GPRS_CAL_OFFSET);
		gprs.setTotal(old_card.getUnicom_total() + ICcGprsAllotService.GPRS_CAL_OFFSET);
		gprs.setThis_month(iCcGprsAllotService.getThisMonth(old_card.getCard_type()));
		gprs.setIs_unicom(true);
		gprs.setOpen_card(false);
		
		Double pay_total = old_card.getPay_total();
		old_card.setPay_total(0D);
		old_card.setTime_paid(DateUtil.nowStr());
		/**
		 * 重新计算旧卡流量
		 */
		try {
			iCcGprsAllotService.gprsCalculate(old_card, gprs);
		} catch (Exception e) {
			log.error("[move][exception]params={old_card:{},gprs:{},form:{}},exception={}", JSONObject.toJSONString(old_card),JSONObject.toJSONString(gprs),form.buildJsonString(), ExceptionUtils.getStackTrace(e));
			throw new BusinessException(-1, "抱歉，流量卡流量迁移失败！");
		}
		
		/**
		 * 重新计算新卡流量
		 */
		gprs = new GprsCalculateBean();
		gprs.setMonth(new_card.getUnicom_month() + ICcGprsAllotService.GPRS_CAL_OFFSET);
		gprs.setTotal(new_card.getUnicom_total() + ICcGprsAllotService.GPRS_CAL_OFFSET);
		gprs.setThis_month(iCcGprsAllotService.getThisMonth(old_card.getCard_type()));
		gprs.setIs_unicom(true);
		gprs.setOpen_card(true);
		
		new_card.setTime_paid(DateUtil.nowStr());
		if(StringUtils.isEmpty(new_card.getTime_active())) {//新卡未激活,需将老卡相关参数转移到新卡上
			
			new_card.setPay_total(pay_total);
			new_card.setOwner_bind(old_card.getOwner_bind());
			new_card.setOwner_wszl(old_card.getOwner_wszl());
			new_card.setTime_last(old_card.getTime_last());
			new_card.setUnicom_ctime(old_card.getUnicom_ctime());
			new_card.setTime_active(old_card.getTime_active());
		}
		
		try {
			iCcGprsAllotService.gprsCalculate(new_card, gprs);
		} catch (Exception e) {
			log.error("[move][exception]params={new_card:{},gprs:{},form:{}},exception={}", JSONObject.toJSONString(new_card),JSONObject.toJSONString(gprs),form.buildJsonString(), ExceptionUtils.getStackTrace(e));
			throw new BusinessException(-1, "抱歉，流量卡流量迁移失败！");
		}
		
		try {
			iCcCardLogService.log8Move(move);
		} catch (Exception e) {
			log.warn("[move][exception]params={},exception={}", JSONObject.toJSONString(move), ExceptionUtils.getStackTrace(e));
		}
		
	}

	/**
	 * 插入迁移数据
	 * @param move
	 * @param old_card_id
	 * @param new_card_id
	 * @return
	 */
	private boolean insert(CcGprsMove move, Integer old_card_id, Integer new_card_id) {
		
		List<CcGprsPay> pay_rs = iCcGprsPayMapper.getActivePackByCardId(old_card_id);
		List<Integer> pay_ids = new ArrayList<>();
		pay_ids.add(0);
		List<Integer> allot_ids = new ArrayList<>();
		allot_ids.add(0);
		Integer allot_id = null;
		if(!CollectionUtils.isEmpty(pay_rs)) {
			
			for (CcGprsPay ccGprsPay : pay_rs) {
				pay_ids.add(ccGprsPay.getPay_id());
				allot_id = iCcGprsAllotMapper.getAllotByCardIdAndGprsAmountAndTimeExpire(old_card_id, ccGprsPay.getGprs_amount(), ccGprsPay.getTime_expire());
				
				if(allot_id != null) {
					allot_ids.add(allot_id);
				}
			}
		}
		
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus transactionStatus = clwTransactionManager.getTransaction(definition);
		
		try {
			
			//将购买的套餐迁移至新卡
			UpdateWrapper<CcGprsPay> updateWrapper1 = new UpdateWrapper<>();
			updateWrapper1.eq("card_id", old_card_id);
			updateWrapper1.notIn("pay_id", pay_ids);
			
			CcGprsPay pay = new CcGprsPay();
			pay.setCard_id(new_card_id);
			if(!retBool(iCcGprsPayMapper.update(pay, updateWrapper1))){
				log.warn("[insert][update gprs pay 失败]params={move:{},pay_ids:{},allot_ids:{}}", JSONObject.toJSONString(move),JSONObject.toJSONString(pay_ids),JSONObject.toJSONString(allot_ids));
				clwTransactionManager.rollback(transactionStatus);
				return false;
			}
			
			//将已分配的套餐迁移至新卡
			UpdateWrapper<CcGprsAllot> updateWrapper2 = new UpdateWrapper<>();
			updateWrapper2.eq("card_id", old_card_id);
			updateWrapper2.notIn("allot_id", allot_ids);
			CcGprsAllot ccGprsAllot = new CcGprsAllot();
			ccGprsAllot.setCard_id(new_card_id);
			if(!iCcGprsAllotService.update(ccGprsAllot, updateWrapper2)){
				log.warn("[insert][update gprs allot 失败]params={move:{},pay_ids:{},allot_ids:{}}", JSONObject.toJSONString(move),JSONObject.toJSONString(pay_ids),JSONObject.toJSONString(allot_ids));
				clwTransactionManager.rollback(transactionStatus);
				return false;
			}
			
			//将已分配的套餐迁移至新卡
			UpdateWrapper<CcGprsValue> updateWrapper3 = new UpdateWrapper<>();
			updateWrapper3.eq("card_id", old_card_id);
			updateWrapper3.notIn("allot_id", allot_ids);
			CcGprsValue ccGprsValue = new CcGprsValue();
			ccGprsValue.setCard_id(new_card_id);
			if(!iCcGprsValueService.update(ccGprsValue, updateWrapper3)){
				log.warn("[insert][update gprs value 失败]params={move:{},pay_ids:{},allot_ids:{}}", JSONObject.toJSONString(move),JSONObject.toJSONString(pay_ids),JSONObject.toJSONString(allot_ids));
				clwTransactionManager.rollback(transactionStatus);
				return false;
			}
			
			if(!this.save(move)) {
				
				log.warn("[insert][保存迁移记录失败]params={}", JSONObject.toJSONString(move));
				clwTransactionManager.rollback(transactionStatus);
				return false;
			}
			
			clwTransactionManager.commit(transactionStatus);
		}catch(Exception e) {
			
			clwTransactionManager.rollback(transactionStatus);
			log.error("[insert][exception]params={move:{},pay_ids:{},allot_ids:{}},exception={}", JSONObject.toJSONString(move),JSONObject.toJSONString(pay_ids),JSONObject.toJSONString(allot_ids), ExceptionUtils.getStackTrace(e));
			return false;
		}
		
		return true;
	}
	
	

}