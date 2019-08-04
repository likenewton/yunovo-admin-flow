package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.math.MathUtils;
import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.dao.ICcGprsPayMapper;
import cn.yunovo.iov.fc.dao.ICcGprsValueMapper;
import cn.yunovo.iov.fc.dao.ICcRealnameMapper;
import cn.yunovo.iov.fc.dao.ICcStatsMonthMapper;
import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.model.result.CardRestBean;
import cn.yunovo.iov.fc.model.result.UnicomDataBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsValueService;
import cn.yunovo.iov.fc.service.ICcOnoffLogService;
import cn.yunovo.iov.fc.service.manage.M2mService;
import cn.yunovo.iov.fc.service.manage.ZhiWangApiService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

/**
 * <p>
 * 流量分配表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
@Slf4j
public class CcGprsAllotServiceImpl extends ServiceImpl<ICcGprsAllotMapper, CcGprsAllot> implements ICcGprsAllotService {

	@Autowired
	private ICcGprsAllotMapper iCcGprsAllotMapper;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Autowired
	private ICcGprsValueService iCcGprsValueService;
	
	@Autowired
	private ICcGprsValueMapper iCcGprsValueMapper;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcStatsMonthMapper iCcStatsMonthMapper;
	
	@Autowired
	private ZhiWangApiService zhiWangApiService;
	
	@Autowired
	private ICcOnoffLogService iCcOnoffLogService;
	
	@Autowired
	private ICcGprsPayMapper  iCcGprsPayMapper;
	
	@Autowired
	private ICcRealnameMapper iCcRealnameMapper;
	
	@Autowired
	private ICcCardLogService iCcCardLogService;
	
	@Autowired
	private ICcGprsBatchService iCcGprsBatchService;
	
	@Autowired
	private ICcGprsCardMapper iCcGprsCardMapper;
	
	@Autowired
	private M2mService m2mService;
	
	@Autowired
	@Qualifier("clwTransactionManager")
	private DataSourceTransactionManager clwTransactionManager;
	
	@Override
	public boolean gprsAllot(Integer card_id) {
		
		String month_end_type_cacheKey = FcConstant.memResKey(FcConstant.MONTH_END_TYPE_CACHEKEY);
		
		Integer month_end_type_val = NumberUtils.createInteger(jedisPoolUtil.get(month_end_type_cacheKey));
		//获取未过期的流量套餐分配情况列表，根据此列表分配当月可使用的流量套餐
		List<CcGprsAllot>  unAllotGprsPack = iCcGprsAllotMapper.getUnAllotGprsPack(card_id, month_end_type_val);
		
		if(CollectionUtils.isEmpty(unAllotGprsPack)) {
			
			return false; //没有流量套餐可分配
		}
		
		String curt_month = null;
		String last_month = null;
		String yesterday = jedisPoolUtil.get(FcConstant.memResKey(FcConstant.YESTERDAY_ACTIVE_CACHEKEY));
		Integer dayOfMonth = DateUtil.getDayOfMonth(new Date());
		/**
		 * 判断是否为月结日，并且卡是昨天（在非车机上）激活，那么流量套餐需要从上个月开始分配
		 */
		if(unAllotGprsPack.get(0).getCard_type() == 1) { //吉林联通
			
			if(dayOfMonth == 1 && !StringUtils.isEmpty(yesterday)) {
				
				curt_month = DateFormatUtils.format(DateUtils.addMonths(new Date(), -1), "yyyyMM");//月结且昨天激活,当月需等于上月
				last_month = DateFormatUtils.format(new Date(), "yyyyMM");//月结且昨天激活,上月需等于当月
				jedisPoolUtil.del(FcConstant.memResKey(FcConstant.YESTERDAY_ACTIVE_CACHEKEY));//清除昨天激活状态
			}else {
				
				last_month = DateFormatUtils.format(DateUtils.addMonths(new Date(), -1), "yyyyMM");//上月
				curt_month = DateFormatUtils.format(new Date(), "yyyyMM");//当月
			}
			
		}else {//JASPER物联卡
			
			if( dayOfMonth == 27 && !StringUtils.isEmpty(yesterday)) {
				
				curt_month = DateFormatUtils.format(DateUtils.addMonths(new Date(), -1), "yyyyMM");//月结且昨天激活,当月需等于上月
				last_month = DateFormatUtils.format(new Date(), "yyyyMM");//月结且昨天激活,上月需等于当月
				jedisPoolUtil.del(FcConstant.memResKey(FcConstant.YESTERDAY_ACTIVE_CACHEKEY));//清除昨天激活状态
			}else {
				
				if(dayOfMonth >= 27) {
					
					curt_month = DateFormatUtils.format(DateUtils.addMonths(new Date(), 1), "yyyyMM");//当月
					last_month = DateFormatUtils.format(new Date(), "yyyyMM");//上月
				}else {
					curt_month = DateFormatUtils.format(new Date(), "yyyyMM");//当月
					last_month = DateFormatUtils.format(DateUtils.addMonths(new Date(), -1), "yyyyMM");//上月
				}
			}
		}
		
		Integer curt_month_int = NumberUtils.toInt(curt_month);
		Integer last_month_int = NumberUtils.toInt(last_month);
		Integer[] howMonth = new Integer[] {curt_month_int, last_month_int};
		Map<Integer, CcGprsValue> temp = null;
		CcGprsValue value = null;
		
		//事务定义
		DefaultTransactionDefinition definition = null;
		TransactionStatus transactionStatus = null;
		
		/**
		 * 分配流量卡拥有的未过期套餐
		 */
		for (CcGprsAllot ccGprsAllot : unAllotGprsPack) {
			
			temp = iCcGprsValueService.allotValueMapForHowmonth(ccGprsAllot.getCard_id(), ccGprsAllot.getAllot_id(), howMonth);
			if(temp.containsKey(NumberUtils.createInteger(curt_month))) {//判断当前月是否已分配流量,如果已分配则无需分配
				
				value = new CcGprsValue();
				value.setGprs_vid(temp.get(curt_month_int).getGprs_vid());
				value.setTime_expire(ccGprsAllot.getTime_expire());
				value.setTime_modify(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				if(!iCcGprsValueService.updateById(value)) {
					log.warn("[gprsAllot][更新流量分配信息失败]params={value:{},gprsAllot:{}}", JSONObject.toJSONString(value), JSONObject.toJSONString(ccGprsAllot));
				}
				continue;
			}
			
			if(temp.containsKey(last_month_int)) {
				
				value = temp.get(last_month_int);
			
			}else {
				
				value = new CcGprsValue();
				value.setCard_id(ccGprsAllot.getCard_id());
				value.setAllot_id(ccGprsAllot.getAllot_id());
				value.setTime_expire(ccGprsAllot.getTime_expire());
				value.setGprs_value(0.0);
				value.setBalance_dval(0.0);
				value.setBalance_value(0.0);
			}
			
			value.setGprs_vid(null);
			value.setHow_month(curt_month_int);
			value.setTime_added(DateUtil.nowStr());
			value.setTime_modify(null);
				
			/**
			 * 判断是否还有未分配的流量
			 */
			if(ccGprsAllot.getAssigned_month() < ccGprsAllot.getAllot_month()) {
				
				value.setGprs_value((ccGprsAllot.getAllot_reset() == 0 && value.getBalance_value() > 0) ? (value.getBalance_value() + ccGprsAllot.getAllot_value()) : ccGprsAllot.getAllot_value());
				value.setBalance_value(value.getGprs_value());
				value.setBalance_dval(value.getGprs_value());
				
				definition = new DefaultTransactionDefinition();
				definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				transactionStatus = clwTransactionManager.getTransaction(definition);
				try {
					if(!iCcGprsValueService.save(value)) {
						log.warn("[gprsAllot][保存流量分配信息失败]params={value:{},gprsAllot:{}}", JSONObject.toJSONString(value), JSONObject.toJSONString(ccGprsAllot));
						clwTransactionManager.rollback(transactionStatus);
						continue;
					}
					
					int rows = iCcGprsAllotMapper.addAssignedMonthById(ccGprsAllot.getAllot_id());
					
					if(rows < 1) {
						clwTransactionManager.rollback(transactionStatus);
					}else {
						clwTransactionManager.commit(transactionStatus);
					}
				
				}catch(Exception e) {
					log.warn("[gprsAllot][exception]params={gprsAllot:{}},exception={}", JSONObject.toJSONString(ccGprsAllot), ExceptionUtils.getStackTrace(e));
					clwTransactionManager.rollback(transactionStatus);
					throw e;
				}
				
			}else {
				
				/**
				 * 如果套餐不清零且套剩余流量大于0，则需要(结转)继承上月余量
				 */
				if(ccGprsAllot.getAllot_reset() == 0 && value.getBalance_value() > 0) {
					
					value.setGprs_value(value.getBalance_value());
					iCcGprsValueService.save(value);
				}
			}
			
			
		}
		
//		if(card_id != null) {
			return true;
		//}
			
		//TODO 批量分配逻辑后续再做
		/*
		List<String> iccids = iCcGprsValueMapper.getIccidByCardtypeAndHowmonth(month_end_type_val, curt_month_int);
		CcGprsCard card = null;
		if(!CollectionUtils.isEmpty(iccids)) {
			
			for (String iccid : iccids) {
				
				card = iCcGprsCardService.getByIccid(iccid);
				GprsCalculateBean gprs = new GprsCalculateBean();
				gprs.setMonth(card.getUnicom_month() + GPRS_CAL_OFFSET);
				gprs.setTotal(card.getUnicom_total() + GPRS_CAL_OFFSET);
				gprs.setIs_unicom(true);
				gprs.setThis_month(curt_month_int);
				
				this.gprsCalculate(card, gprs);
			}
		}
		jedisPoolUtil.del(month_end_type_cacheKey);*/
		//return true;
	}

	/**
	 * 根据所传流量计算流量卡的当月流量
	 * @param card 流量卡信息
	 * @param gprs 当前流量数据
	 * 流量卡信息
	 */
	@Override
	public CcGprsCard gprsCalculate(CcGprsCard card, GprsCalculateBean gprs) {
		
		String cardLockCacheKey = FcConstant.memResKey(String.format(FcConstant.CARD_LOCK_CACHEKEY, card.getCard_iccid()));
		jedisPoolUtil.setEx(cardLockCacheKey, "1");
		
		/**
		 * 判断是否当月因重置流量卡需要做特殊差异化对等当月使用量处理
		 */
		if(card.getReset_diff() > 0 && gprs.getMonth() - card.getReset_diff() > 0) {
			
			gprs.setMonth(gprs.getMonth() - card.getReset_diff());
		}
		
		CcGprsCard data  = new CcGprsCard();
		BeanUtils.copyProperties(card, data);
		data.setUsed_total(gprs.getTotal());
		data.setUsed_month(gprs.getMonth());
		
		Integer month = null;
		Date month_date = null;
		Double gprs_diff = null;
		
		/**
		 * 判断是否为联通数据计算，因联通脚本是计算的昨天的流量数据，则当为该月第一天时，计算时需以上个月的查询为准
		 */
		if(gprs.getIs_unicom()) {
			
			card.setMax_unused(card.getUnicom_unused());//计算字段统一为设备剩余流量
			data.setUnicom_total(gprs.getTotal());//累计使用流量
			data.setUnicom_month(gprs.getMonth());//当前月使用流量
			data.setUnicom_diff(gprs.getMonth() - card.getUnicom_month());//距离上次月流量的差异值
			month = gprs.getThis_month();//需要计算的月份，区分联通月结
			gprs_diff = data.getUnicom_diff();//距离上次月流量的差异值
		}else {
			month_date = (card.getCard_type() >= 2 && DateUtil.getDayOfMonth(null)>=27) ? DateUtils.addMonths(new Date(), 1):DateUtil.now();
			month = NumberUtils.toInt(DateFormatUtils.format(month_date, "yyyyMM")); //计算当月的流量
			gprs_diff = gprs.getMonth() - card.getUsed_month();//距离上次月流量的差异值
		}
		
		//查询该卡当月的流量套餐列表
		List<CcGprsValue> temp2 = iCcGprsValueMapper.getGprsInfoByCardidAndHowmonth(card.getCard_id(), card.getCard_type(), month);
		
		if(CollectionUtils.isEmpty(temp2)) { //判断是否有套餐流量
			
			/**
			 * 没有套餐时，也需要扣除流量，将其计算到剩余流量中方便日后待扣; 如果原来最大可使用流量大于零将其作废
			 */
			if(card.getMax_unused() > 0) {
				data.setMax_unused(0 - gprs_diff);
			}else {
				data.setMax_unused(card.getMax_unused() - gprs_diff);
			}
			
			/**
			 * 联通数据计算时，需把联通的最大可使用流量更新
			 */
			if(gprs.getIs_unicom()) {
				data.setUnicom_unused(data.getMax_unused());
				this.saveMonthData(month, data);
				this.saveDayData(null, data.getUnicom_diff(), data);
			}
			
			if(card.getCard_type() == 4) {
				/**
				 * 计算流量卡的过期时间 - 仅适用于定向流量卡
				 */
				String maxTimeExpire = iCcGprsValueMapper.getMaxTimeExpireByCardidAndHowMonth(card.getCard_id(), month);
				if(StringUtils.isNotEmpty(maxTimeExpire)) {
					data.setTime_expire(maxTimeExpire);
				}
			}
			jedisPoolUtil.del(cardLockCacheKey);
			this.whiteList(data, month, null);
			
			return iCcGprsCardService.updateCard(card) ? card : null;
			
		
		}
		
		/**
		 * 判断剩余流量是否小于等于0，防止剩余流量重复扣
		 */
		gprs_diff = card.getMax_unused() <= 0 ? Math.abs(card.getMax_unused() - gprs_diff ) : gprs_diff;
		
		/**
		 * 套餐列表中如有剩余流量为负数则将负数按升序排前优先计算（防止计算异常）
		 */
		Integer pack_count = temp2.size();
		
		if(pack_count > 1) {//流量卡拥有的套餐数大于1方可进行排序才有意义
			
			if(gprs.getIs_unicom()) {
				
				Collections.sort(temp2, new Comparator<CcGprsValue>() {

					@Override
					public int compare(CcGprsValue o1, CcGprsValue o2) {
						
						if(o1.getBalance_value() <0 || o2.getBalance_value() < 0) {
							return o1.getBalance_value() > o2.getBalance_value() ? 1 : -1;
						}
						return 0;
					}
				
				});
				
			}else {
				Collections.sort(temp2, new Comparator<CcGprsValue>() {

					@Override
					public int compare(CcGprsValue o1, CcGprsValue o2) {
						
						if(o1.getBalance_dval() < 0 || o2.getBalance_dval() < 0) {
							return o1.getBalance_dval() > o2.getBalance_dval() ? 1 : -1;
						}
						return 0;
					}
				
				});
			}
			
			
		}
		
		Double balance_val = null;
		Double balance_dval = null;
		CcGprsValue temp3 = null;
		int k = 0;
		/**
		 * 循环流量值列表计算剩余的流量
		 */
		for(int i = 0; i < temp2.size(); i ++) {
			k ++;
			temp3 = temp2.get(i);
			balance_val = gprs.getIs_unicom() ? temp3.getBalance_value() : temp3.getBalance_dval();
			
			/**
			 * 判断此套餐流量是否等于0，且不是最后一个套餐则继续运算下一个套餐
			 */
			if(balance_val == 0 && pack_count != k) {
				continue;
			}
			
			/**
			 * 判断套餐流量值是否已过期，过期的需要将原套餐大于0的流量置为0，小于0的作为扣除流量的依据
			 */
			if(DateUtil.compare(DateUtil.parseDate(temp3.getTime_expire(), "yyyy-MM-dd HH:mm:ss"), DateUtil.now()) < 1) {
				
				balance_val = balance_val > 0 ? 0 : balance_val;
			}
			
			if(balance_val >= 0) {
				balance_dval = balance_val - gprs_diff;//扣除差异值流量，得此套餐剩余流量
			}else {
				balance_dval = (balance_val - card.getMax_unused()) - gprs_diff;//剩余流量与流量卡最大可使用流量对等
			}
			
			CcGprsValue gprs_value = new CcGprsValue();
			/**
			 * 更新此套餐剩余流量，剩余流量大于0时，说明此套餐扣完后还有剩余流量，小于0时，判断是否为最后一个套餐，是则需记录所欠流量，不是则等于0
			 */
			gprs_value.setBalance_dval(balance_dval > 0 ? balance_dval : (pack_count == k ? balance_dval : 0));
			gprs_value.setTime_modify(DateUtil.nowStr());
			gprs_value.setBalance_value(gprs.getIs_unicom() ? gprs_value.getBalance_dval() : gprs_value.getBalance_value());
			gprs_value.setGprs_vid(temp3.getGprs_vid());
			iCcGprsValueService.updateById(gprs_value);
			
			if(balance_dval < 0) {
				gprs_diff = Math.abs(balance_dval);
				continue;
			}
			
			break;//说明当前流量套餐够扣，无需计算下一套餐，直接跳出
		}
		
		/**
		 * 计算该卡的总剩余流量与过期时间
		 */
		CcGprsValue gprsValueSum = iCcGprsValueMapper.sumBalanceDvalByCardidAndHowMonth(card.getCard_id(), month);
		data.setMax_unused(gprsValueSum.getBalance_dval());
		data.setTime_expire(gprsValueSum.getTime_expire());
		
		/**
		 * 联通数据计算时，需把联通的最大可使用流量更新，并记录月流量使用数据
		 */
		if(gprs.getIs_unicom()) {
			data.setUnicom_unused(data.getMax_unused());
			this.saveMonthData(month, data);
			this.saveDayData(null, data.getUnicom_diff(), data);
		}
		
		/**
		 * 如果卡已被停用且计算后的最大可使用流量大于0 并且可开卡（充值、月结需开卡），才需重新开启流量卡
		 */
		/*if ($data['unicom_stop'] == 1 && $data['max_unused'] > 0 && !empty($gprs['open_card']))
		{
			$username = ($data['card_type'] == 1 && date('j') == 1) || ($data['card_type'] >= 2 && date('j') == 27) ? 'mend' : 'topup';
			if (self::cardOnoff($mem, $data, 0, 0, $username))
			{
				$data['unicom_stop'] = 0;
			}
		}*/
		
		if(data.getUnicom_stop() == 1 && data.getMax_unused() > 0 && (gprs.getOpen_card() == null || !gprs.getOpen_card())) {
			
			String username = (data.getCard_type() == 1 && DateUtil.getDayOfMonth(null) == 1) || (data.getCard_type() >= 2 && DateUtil.getDayOfMonth(null) == 27) ? "mend" : "topup";
			if(this.cardOnoff(card, 0, 0, username)) {
				data.setUnicom_stop((short)0);
			}
		}
		
		jedisPoolUtil.del(cardLockCacheKey);
		this.whiteList(data, month, null);
		
		return iCcGprsCardService.updateCard(data) ? data : null;
		
	}
	
	@Override
	public CardRestBean cardReset(CcGprsCard card) {
		
		CardRestBean result = new CardRestBean();
		/**
		 * 流量卡未激活不能重置
		 */
		if(StringUtils.isEmpty(card.getTime_active())) {
			
			result.setRet("unactivated");
			result.setMsg("Card is not activated");
			return result;
		}
		
		/**
		 * 判断流量卡是否充值过如果有则暂不能重置需待迁移后方可重置
		 */
		List<String>  res = iCcGprsPayMapper.listPaysnByCardidAndPaymethodAndIspaid(card.getCard_id());
		if(!CollectionUtils.isEmpty(res)) {
			result.setRet("havepay");
			result.setMsg("Card have pay pack, need move it.");
			return result;
		}
		
		/**
		 * 判断流量卡是拥有套餐
		 */
		res = iCcGprsPayMapper.listPaysnByCardid(card.getCard_id());
		
		//事务定义
		DefaultTransactionDefinition definition = null;
		TransactionStatus transactionStatus = null;
		/**
		 * 判断流量卡是拥有套餐
		 */
		if(!CollectionUtils.isEmpty(res)) {
			definition = new DefaultTransactionDefinition();
			definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transactionStatus = clwTransactionManager.getTransaction(definition);
			
			try {
				/**
				 * 将此流量卡下所有套餐过期
				 */
				iCcGprsAllotMapper.updateTimeExpireByCardidAndTimeexpire(card.getCard_id());
				
				/**
				 * 将已分配的当前月套餐月份变成负数从而使用不期参流量运算
				 */
				Date now = DateUtil.now();
				Date this_month = (card.getCard_type() >= 2 && DateUtil.getDayOfMonth(now) >= 27) ? DateUtils.addMonths(now, 1) : DateUtil.now();
				Integer month = NumberUtils.createInteger(DateFormatUtils.format(this_month, "yyyyMM"));
				iCcGprsValueMapper.updateHowmonthByCardidAndHowMonth(card.getCard_id(), month);
				
				/**
				 * 将此流量卡下所有已付款的充值记录支付方式设置为-1,仅仅是为是前端不显示支付方式为-1的充值记录
				 */
				int isSuccess = iCcGprsPayMapper.updatePayMethodByCardid(card.getCard_id());
				if(isSuccess < 1) {
					
					log.warn("[cardReset][Set card pay log of pay method to -1 failed]params={}",JSONObject.toJSONString(card));
					clwTransactionManager.rollback(transactionStatus);
					result.setRet("errorpay");
					result.setMsg("Set card pay log of pay method to -1 failed");
					return result;
				}
				
				clwTransactionManager.commit(transactionStatus);
			}catch(Exception e) {
				log.error("[cardReset][exception]params={},exception={}",JSONObject.toJSONString(card),ExceptionUtils.getStackTrace(e));
				clwTransactionManager.rollback(transactionStatus);
				
				result.setRet("errorpay");
				result.setMsg("Set card pay log of pay method to -1 failed");
				return result;
			}
			
		}
		
		CcRealname realname = iCcRealnameMapper.getByCardId(card.getCard_id());
		if(realname != null) {
			realname.setTime_audit(DateUtil.nowStr());
			iCcCardLogService.log10Rlname(realname, true);
			iCcRealnameMapper.updateIccidByCardid(card.getCard_id(), null, null);
		}
		
		card.setOwner_real(0);
		
		iCcCardLogService.log9Reset(card.getCard_id(), card.getUnicom_month(), card.getUnicom_total(), DateUtil.nowStr());
		
		/**
		 * 判断流量卡是否已停卡,如果已停卡需开启
		 */
		if(card.getUnicom_stop() == 1) {
			if(this.cardOnoff(card, 0, 0, "reset")) {
				card.setUnicom_stop((short)0);
			}
		}
		
		Double gprs_amount = iCcGprsBatchService.getGprsAmountByBatchId(card.getBatch_id());
		gprs_amount = gprs_amount == null ? 0 : gprs_amount;
		card.setCard_openid(null);
		card.setOwner_bind(0);
		card.setOwner_wszl(0);
		card.setUsed_month(0D);
		card.setMax_unused(gprs_amount);
		card.setReset_diff(card.getReset_diff() + card.getUnicom_month());
		card.setUnicom_month(0D);
		card.setUnicom_unused(gprs_amount);
		card.setUnicom_ctime(null);
		card.setTime_active(null);
		card.setTime_last(null);
		card.setTime_paid(null);
		card.setTime_expire(null);
		
		jedisPoolUtil.del(FcConstant.cardInfoKey(card.getCard_iccid()));
		iCcGprsCardService.updateInfoById(card);
		
		result.setRet("ok");
		result.setMsg("Reset Succeed");
		//iCcGprsCardService.updateby
		return result;
	}
	
	/**
	 * 调用联通接口开卡或停卡
	 * @param card 卡信息
	 * @param onoff 开关：0为开卡，1为停卡
	 * @param userid 操作者编号
	 * @param username 操作者姓名
	 * @return bool 成功true失败false
	 */
	@Override
	public boolean cardOnoff(CcGprsCard card, int onoff, int userid, String username) {

		JSONObject apiData = null;
		boolean isSuccess = true;
		if(card.getCard_type() == 1) { //吉林联通卡
			
			try {
				apiData = zhiWangApiService.cardOnoff(card.getCard_sn(), onoff);
				isSuccess = zhiWangApiService.isSuccess(apiData);
			} catch (RestClientException e) {
				log.error("[cardOnoff][exception]params={card:{},onoff:{},userid:{},username:{}},exception={}", JSONObject.toJSONString(card),onoff,userid,username, ExceptionUtils.getStackTrace(e));
				throw e;
			}
		}else if(card.getCard_type() == 4) {//智网定向流量卡无开关功能
			
			return false;
		}else {//联通JASPER平台接口
			
			try {
				apiData = m2mService.editTerminal(card.getCard_type(), "", "", card.getCard_iccid(), 3, onoff == 1 ? "DEACTIVATED_NAME":"ACTIVATED_NAME");
				isSuccess = m2mService.isSuccess(apiData);
			} catch (RestClientException e) {
				log.error("[cardOnoff][exception]params={card:{},onoff:{},userid:{},username:{}},exception={}", JSONObject.toJSONString(card),onoff,userid,username, ExceptionUtils.getStackTrace(e));
				throw e;
			}
		}
		if(!isSuccess) {
			log.error("[cardOnoff][开卡停卡失败]params={card:{},onoff:{},userid:{},username:{}},apiData={}", JSONObject.toJSONString(card),onoff,userid,username, JSONObject.toJSONString(apiData));
		}
		iCcOnoffLogService.cardOnOffLog(card, onoff, isSuccess, userid, username);
		return isSuccess;
	}

	/**
	 * 白名单计费处理
	 * @param data 卡信息
	 * @param month 当前月份
	 */
	public void whiteList(CcGprsCard card, Integer month, String stats_date) {
		
		
		if(card.getCard_type() == 5 && card.getMax_unused() < 0) {
			
			String sql1 = "balance_dval = 0";
			String sql = null;
			if(card.getUnicom_unused() - card.getMax_unused() == 0) {
				
				sql1 += ", balance_value = 0";
				sql = "how_month = "+month+", month_wlist = month_wlist + "+Math.abs(card.getUnicom_unused());
				
				sql = "INSERT INTO cc_stats_month SET card_id = "+card.getCard_id()+", "+sql+" ON DUPLICATE KEY UPDATE "+sql;
				
				if(jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) != 1) {
					
					iCcStatsMonthMapper.updateForSql(sql);
				}
				
				String sdate = StringUtils.defaultIfEmpty(stats_date, DateUtil.nowStr());
				sql = "stats_date = '"+sdate+"', day_wlist = day_wlist + "+Math.abs(card.getUnicom_unused());
				sql = "INSERT INTO cc_stats_day SET card_id = "+card.getCard_id()+", "+sql+" ON DUPLICATE KEY UPDATE "+sql;
				
				if(jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) != 1) {
					
					iCcStatsMonthMapper.updateForSql(sql);
				}
				
				card.setUnicom_unused(0.0);
			}
			
			sql = "UPDATE cc_gprs_value SET "+sql1+" WHERE card_id = "+card.getCard_id()+" AND how_month = "+month;
			
			if(jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) != 1) {
				
				iCcStatsMonthMapper.updateForSql(sql);
			}
			
			card.setMax_unused(0.0);
			
		}
	}

	/**
	 * 记录下流量卡每日流量使用情况，使用消息队列
	 * @param unicom_diff  流量卡日使用量
	 * @param data 流量卡数据
	 */
	private boolean saveDayData(String stats_date, Double unicom_diff, CcGprsCard data) {

		if(unicom_diff <= 0.01) {
			return false; //流量无变化或与联通对账异常则不记录日使用量
		}
		
		String sdate = StringUtils.defaultIfEmpty(stats_date, DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd"));
		String over = data.getMax_unused() < 0 ? "IF(ayer_unused >= 0, ABS(today_unused), day_used)" : "0";
		String sql1 = String.format("day_used = day_used + %s, ayer_unused = day_used + %s, today_unused = %s, day_over = day_wlist + %s", unicom_diff, data.getMax_unused(), data.getMax_unused(),over);
		String sql = String.format("INSERT INTO cc_stats_day SET stats_date = '%s', card_id = %s, %s, time_modify = NOW() ON DUPLICATE KEY UPDATE %s, time_modify = NOW()", sdate, data.getCard_id(),sql1, sql1);
		
		boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
		if(!isOk) {
			iCcStatsMonthMapper.updateForSql(sql);
		}
		
		return true;
	}

	/**
	 * 记录下流量卡每月流量使用情况，使用消息队列
	 * @param month 记录的月份
	 * @param data 流量卡数据
	 * @return true | false 处理成功返回true反之为false
	 */
	public boolean saveMonthData(Integer month, CcGprsCard data) {

		if(data.getUsed_month() <= 0.01) {
			return false;
		}
		
		Double unused = data.getUnicom_unused() > 0 ? data.getUnicom_unused() : 0;
		Double overval = data.getUnicom_unused() < 0 ? Math.abs(data.getUnicom_unused()) : 0;
		String sql1 = String.format("how_month = %s, month_used = %s, month_unused = %s, month_over = month_wlist + %s", month, data.getUsed_month(), unused, overval);
		
		String sql = String.format("INSERT INTO cc_stats_month SET card_id = %s, %s, time_modify = NOW() ON DUPLICATE KEY UPDATE %s, time_modify = NOW()", data.getCard_id(), sql1, sql1);
		
		boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
		if(!isOk) {
			iCcStatsMonthMapper.updateForSql(sql);
		}
		
		return true;
	}
	
	/**
	 * 同步联通数据
	 *
	 * @param array   card_info 流量卡详情
	 * @return UnicomDataBean 返回联通数据
	 */
	@Override
	public UnicomDataBean syncUnicomData(CcGprsCard card) {
		
		UnicomDataBean result = new UnicomDataBean();
		if(StringUtils.isEmpty(card.getTime_active())) {
			result.setStatus(0);
			result.setMsg("暂未激活");
			return result;
		}
		String card_status = "已启用";
		String this_month = DateFormatUtils.format(new Date(), "yyyyMM");
		Long consumeDataMon = null;
		Long consumeDataAll = null;
		Double gprs_month = null;
		Double gprs_total = null;
		JSONObject apiData = null;
		if(card.getCard_type() == 1) {
			try {
				apiData = zhiWangApiService.getTerminalDetails(card.getCard_sn());
			} catch (Exception e) {
				log.error("[syncUnicomData][exception]params={card:{}},exception={}", JSONObject.toJSONString(card), ExceptionUtils.getStackTrace(e));
				result.setStatus(-1);
				result.setMsg("第三方接口调用失败");
				return result;
			}
			
			if(apiData == null) {
				
				result.setStatus(-1);
				result.setMsg("接口调用失败");
				return result;
			}else if(zhiWangApiService.isSuccess(apiData)){
				
				if(apiData.getJSONObject("data").getInteger("state") != 1) {
					
					result.setStatus(0);
					result.setMsg(apiData.getJSONObject("data").getInteger("state") == 2 ? "已停用" : "未激活");
					return result;
				}
			}else {
				log.warn("[syncUnicomData][接口返回错误]params={card:{}},apiData={}", JSONObject.toJSONString(card), JSONObject.toJSONString(apiData));
				result.setStatus(apiData.getInteger("status"));
				result.setMsg(apiData.getString("msg"));
				return result;
			}
			consumeDataMon = StringUtils.equals("[]", apiData.getJSONObject("data").getString("consumeDataMon"))? 0L : NumberUtils.createLong(apiData.getJSONObject("data").getString("consumeDataMon"));
			consumeDataAll = StringUtils.equals("[]", apiData.getJSONObject("data").getString("consumeDataAll"))? 0L : NumberUtils.createLong(apiData.getJSONObject("data").getString("consumeDataAll"));
			gprs_month = MathUtils.round(consumeDataMon / 1048576D, 3); //当前月使用流量情况MB
			gprs_total = MathUtils.round(consumeDataAll / 1048576D,3 ); //流量卡总使用流量情况MB
		}else if(card.getCard_type() == 4){
			result.setStatus(-1);
			result.setMsg("该流量卡暂不支持同步功能");
			return result;
		}else {
			
			try {
				apiData = m2mService.getTerminalDetails(card.getCard_type(), "", "", card.getCard_iccid());
			} catch (Exception e) {
				log.error("[syncUnicomData][exception]params={card:{}},exception={}", JSONObject.toJSONString(card), ExceptionUtils.getStackTrace(e));
				result.setStatus(-1);
				result.setMsg("第三方接口调用失败");
				return result;
			}
			if(apiData == null) {
				
				result.setStatus(-1);
				result.setMsg("接口调用失败");
				return result;
			}
			if(m2mService.isSuccess(apiData)) {
				
				JSONObject temp = apiData.getJSONObject("data").getJSONObject("terminals").getJSONArray("terminal").getJSONObject(0);
				card_status = StringUtils.equals(temp.getString("status"), "DEACTIVATED_NAME") ? "已停用":"已启用";
				gprs_month = temp.getDouble("monthToDateDataUsage");
				gprs_total = card.getUnicom_total() + (gprs_month - card.getUnicom_month() - card.getReset_diff());
				this_month = DateUtil.getDayOfMonth(new Date()) >= 27 ? DateFormatUtils.format(DateUtils.addMonths(new Date(), 1), "yyyyMM") : DateFormatUtils.format(new Date(), "yyyyMM");
			}else {
				
				log.warn("[syncUnicomData][接口返回错误]params={card:{}},apiData={}", JSONObject.toJSONString(card), JSONObject.toJSONString(apiData));
				result.setStatus(apiData.getInteger("code"));
				result.setMsg(apiData.getString("msg"));
				return result;
			}
		}
		
		card = iCcGprsCardService.getByIccid(card.getCard_iccid());
		GprsCalculateBean gprs = new GprsCalculateBean();
		gprs.setMonth(gprs_month + GPRS_CAL_OFFSET); 
		gprs.setTotal(gprs_total + GPRS_CAL_OFFSET);
		gprs.setIs_unicom(true);
		gprs.setThis_month(NumberUtils.createInteger(this_month));
		gprs.setOpen_card(false);
		card = this.gprsCalculate(card, gprs);
		
		/**
		 * 判断最大可使用流量是否(超标100M)小于等于-100M，如果是则调用联通停卡接口停卡
		 */
		if(card.getMax_unused() <= -100 && card.getUnicom_stop() == 0) {
			
			if(this.cardOnoff(card, 1, 0, "sys2api")) {
				card.setUnicom_stop((short)1);
				card.setTime_stop(DateUtil.nowStr());
				iCcGprsCardService.updateCard(card);
			}
		}
		
		result.setStatus(1);
		result.setMsg(card_status);
		result.setConsumeDataMon(gprs_month);
		result.setConsumeDataAll(gprs_total);
		return result;
	}
	
	@Override
	public Integer getThisMonth(Integer card_type) {
		
		Date now = DateUtil.now();
		Date month = (card_type >= 2 && DateUtil.getDayOfMonth(now) >= 27) ? DateUtils.addMonths(now, 1) : now;
		return NumberUtils.createInteger(DateFormatUtils.format(month, "yyyyMM"));
	}

}
