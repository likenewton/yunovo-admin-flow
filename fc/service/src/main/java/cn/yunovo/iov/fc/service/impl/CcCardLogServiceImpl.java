package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcCardLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.service.ICcCardLogService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
		return this.save(log);
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

}
