package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcCardLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOnoffLog;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOnoffLogService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量卡日志档案表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
@ConfigurationProperties(prefix = "fc.gprs")
public class CcCardLogServiceImpl extends ServiceImpl<ICcCardLogMapper, CcCardLog> implements ICcCardLogService {

	private Map<Integer, String> arr_log_type;
	
	@Autowired
	private ICcCardLogMapper iCcCardLogMapper;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcOnoffLogService iCcOnoffLogService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	private final String INSERT_SQL = "INSERT INTO cc_card_log SET card_id = %s, log_type = %s, log_text = '%s', log_url = '%s', time_added = '%s'";
	
	
	@Override
	public PageData<CcCardLog, Object> logList(PageForm pageForm, Integer card_id, LoginInfo info) {
		
		Page<CcCardLog> page = pageForm.build(CcCardLog.class, null, "time_added");
		PageData<CcCardLog, Object> returnData = new PageData<>();
		List<CcCardLog> records = iCcCardLogMapper.getLogsPage(page, card_id);
		if(!CollectionUtils.isEmpty(records)) {
			
			for (CcCardLog ccCardLog : records) {
				
				ccCardLog.setLog_type_name(this.getArr_log_type_CN(ccCardLog.getLog_type()));
			}
		}
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

	public Map<Integer, String> getArr_log_type() {
		return arr_log_type;
	}
	
	public String getArr_log_type_CN(Integer key) {
		
		if(this.arr_log_type == null) {
			return "";
		}
		
		return this.arr_log_type.get(key);
	}

	public void setArr_log_type(Map<Integer, String> arr_log_type) {
		this.arr_log_type = arr_log_type;
	}
	
	
	@Override
	public boolean log10Rlname(CcRealname res, boolean unbind) {
		
		String log_url = null;
		String log_text = null;
		if(unbind) {
			log_url = "";
			log_text = res.getOwner_name()+" 解除实名认证成功";
		}else {
			log_url = "gprs/realname?iccid="+res.getCard_iccid();
			log_text = res.getOwner_name()+" 绑定实名认证成功";
		}
		
		CcCardLog log = build(res.getCard_id(), 10, log_text, log_url, res.getTime_audit());
		
		String sql = String.format("INSERT INTO cc_card_log SET card_id = %s, log_type = %s, log_text = '%s', log_url = '%s', time_added = '%s'", res.getCard_id(), 10,log_text, log_url, res.getTime_added());
		boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
		if(!isOk) {
			
			return this.save(log);
		}
		
		return isOk;
	}
	
	@Override
	public boolean log5On6Off(CcOnoffLog res, boolean isSuccess) {
		
		Integer log_type = null;
		String log_text = null, log_url = "";
		if(res.getOnoff_type() == 1) {
			log_type = 5;
			log_text = "停卡";
			log_url = "gprs/onofflog?card_id="+res.getCard_id();
		}else {
			log_type = 6;
			log_text = "开卡";
		}
		log_text = log_text + (isSuccess ?   "成功" : "失败");
		String bal_amount = iCcGprsCardService.gprsFormat(res.getBalance_value());
		
		HashMap<String, String> onofflog = iCcOnoffLogService.getArr_onofflog();
		
		log_text = log_text + " 剩余流量"+bal_amount + (onofflog.containsKey(res.getUser_name())? onofflog.get(res.getUser_name()) : res.getUser_name());
				
		String sql = String.format("INSERT INTO cc_card_log SET card_id = %s, log_type = %s, log_text = '%s', log_url = '%s', time_added = '%s'", res.getCard_id(), log_type,log_text, log_url, res.getTime_added());
		
		boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
		if(!isOk) {
			CcCardLog log = build(res.getCard_id(), log_type, log_text, log_url, res.getTime_added());
			return this.save(log);
		}
		
		return isOk;
	}
	
	public CcCardLog build(Integer card_id, Integer log_type, String log_text, String log_url, String time_added) {
		
		CcCardLog log = new CcCardLog();
		log.setCard_id(card_id);
		log.setLog_type(log_type);
		log.setLog_text(log_text);
		log.setLog_url(log_url);
		log.setTime_added(time_added);
		return log;
	}

	@Override
	/**
	 * 流量卡重置日志
	 */
	public boolean log9Reset(Integer card_id, Double unicom_month, Double unicom_total, String nowStr) {

		String log_url = "";
		String unicom_month_str = iCcGprsCardService.gprsFormat(unicom_month);
		String unicom_total_str = iCcGprsCardService.gprsFormat(unicom_total);
		String log_text = "流量卡重置时月用量"+unicom_month_str+" 总用量"+unicom_total_str;
		
		String sql = String.format(INSERT_SQL, card_id, 9,log_text, log_url, nowStr);
		
		boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
		if(!isOk) {
			CcCardLog log = build(card_id, 9, log_text, log_url, nowStr);
			return this.save(log);
		}
		
		return isOk;
	}

}
