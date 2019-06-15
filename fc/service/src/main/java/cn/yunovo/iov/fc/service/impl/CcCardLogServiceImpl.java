package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcCardLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;
import cn.yunovo.iov.fc.service.ICcCardLogService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流量卡日志档案表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcCardLogServiceImpl extends ServiceImpl<ICcCardLogMapper, CcCardLog> implements ICcCardLogService {

	@Autowired
	private ICcCardLogMapper iCcCardLogMapper;
	
	@Override
	public PageData<CcCardLog, Object> logList(PageForm pageForm, Integer card_id, LoginInfo info) {
		
		Page<CcCardLog> page = pageForm.build(CcCardLog.class, null, "time_added");
		PageData<CcCardLog, Object> returnData = new PageData<>();
		List<CcCardLog> records = iCcCardLogMapper.getLogsPage(page, card_id);
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

}
