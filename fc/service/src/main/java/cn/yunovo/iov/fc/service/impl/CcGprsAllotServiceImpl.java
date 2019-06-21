package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.dao.ICcGprsValueMapper;
import cn.yunovo.iov.fc.dao.ICcStatsMonthMapper;
import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsValueService;
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
	
	private Double GPRS_CAL_OFFSET = 0.001;
	
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
		
		for (CcGprsAllot ccGprsAllot : unAllotGprsPack) {
			definition = new DefaultTransactionDefinition();
			definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transactionStatus = clwTransactionManager.getTransaction(definition);
			try {
				temp = iCcGprsValueService.allotValueMapForHowmonth(ccGprsAllot.getCard_id(), ccGprsAllot.getAllot_id(), howMonth);
				if(!CollectionUtils.isEmpty(temp) && temp.containsKey(curt_month)) {//判断当前月是否已分配流量,如果已分配则无需分配
					
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
				value.setTime_added(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				value.setTime_modify(null);
				
				/**
				 * 判断是否还有未分配的流量
				 */
				if(ccGprsAllot.getAssigned_month() < ccGprsAllot.getAllot_month()) {
					value.setGprs_value((ccGprsAllot.getAllot_reset() == null || ccGprsAllot.getAllot_reset() == 0) ? (value.getBalance_value() + ccGprsAllot.getAllot_value()) : ccGprsAllot.getAllot_value());
					value.setBalance_value(value.getGprs_value());
					value.setBalance_dval(value.getGprs_value());
					
					if(!iCcGprsValueService.save(value)) {
						log.warn("[gprsAllot][保存流量分配信息失败]params={value:{},gprsAllot:{}}", JSONObject.toJSONString(value), JSONObject.toJSONString(ccGprsAllot));
						continue;
					}
					
					int rows = iCcGprsAllotMapper.addAssignedMonthById(ccGprsAllot.getAllot_id());
					
					if(rows < 1) {
						clwTransactionManager.rollback(transactionStatus);
					}else {
						clwTransactionManager.commit(transactionStatus);
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
			
			}catch(Exception e) {
				log.warn("[gprsAllot][exception]params={gprsAllot:{}},exception={}", JSONObject.toJSONString(ccGprsAllot), ExceptionUtils.getStackTrace(e));
				clwTransactionManager.rollback(transactionStatus);
				throw e;
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
		
		if(card.getReset_diff() > 0 && gprs.getMonth() > card.getReset_diff()) {
			
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
			
			card.setMax_unused(card.getUnicom_unused());
			data.setUnicom_total(gprs.getTotal());
			data.setUnicom_month(gprs.getMonth());
			data.setUnicom_diff(gprs.getMonth() - card.getUnicom_month());
			month = gprs.getThis_month();
			gprs_diff = data.getUnicom_diff();
		}else {
			month_date = (card.getCard_type() >= 2 && DateUtil.getDayOfMonth(null)>=27) ? DateUtils.addMonths(new Date(), 1):DateUtil.now();
			month = NumberUtils.toInt(DateFormatUtils.format(month_date, "yyyyMM")); //计算当月的流量
			gprs_diff = gprs.getMonth() - card.getUsed_month();//距离上次月流量的差异值
		}
		
		//查询该卡当月的流量套餐列表
		List<CcGprsValue> temp2 = iCcGprsValueMapper.getGprsInfoByCardidAndHowmonth(card.getCard_id(), card.getCard_type(), month);
		
		if(CollectionUtils.isEmpty(temp2)) {
			
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
		
		if(pack_count > 1) {
			
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
		/**
		 * 循环流量值列表计算剩余的流量
		 */
		for(int k = 0; k < temp2.size(); k ++) {
			
			temp3 = temp2.get(k);
			balance_val = gprs.getIs_unicom() ? temp3.getBalance_value() : temp3.getBalance_dval();
			
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
				balance_dval = balance_val - gprs_diff;
			}else {
				balance_dval = (balance_val = card.getMax_unused()) - gprs_diff;
			}
			
			CcGprsValue gprs_value = new CcGprsValue();
			/**
			 * 更新此套餐剩余流量，剩余流量大于0时，说明此套餐扣完后还有剩余流量，小于0时，判断是否为最后一个套餐，是则需记录所欠流量，不是则等于0
			 */
			gprs_value.setBalance_dval(balance_dval > 0 ? balance_dval : (pack_count == k ? balance_dval : 0));
			gprs_value.setTime_modify(DateUtil.nowStr());
			gprs_value.setBalance_value(gprs.getIs_unicom() ? gprs_value.getBalance_dval() : gprs_value.getBalance_value());
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
			//TODO 这块暂未完成
			if(this.cardOnoff(card, 0, 0, username)) {
				data.setUnicom_stop((short)0);
			}
		}
		
		jedisPoolUtil.del(cardLockCacheKey);
		this.whiteList(data, month, null);
		
		return iCcGprsCardService.updateCard(card) ? card : null;
		
	}
	
	/**
	 * 调用联通接口开卡或停卡
	 * @param card 卡信息
	 * @param onoff 开关：0为开卡，1为停卡
	 * @param userid 操作者编号
	 * @param username 操作者姓名
	 * @return bool 成功true失败false
	 */
	private boolean cardOnoff(CcGprsCard card, int onoff, int userid, String username) {

		if(card.getCard_type() == 1) { //吉林联通卡
			
			
			
		}else if(card.getCard_type() == 4) {//智网定向流量卡无开关功能
			
			return false;
		}else {//联通JASPER平台接口
			
		}
		
		return true;
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
			if(card.getUnicom_unused() == card.getMax_unused()) {
				
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
	
	
}
