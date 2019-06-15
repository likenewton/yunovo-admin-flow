package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcCardLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
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

}
