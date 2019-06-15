package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量卡日志档案表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcCardLogService extends IService<CcCardLog> {

	public PageData<CcCardLog, Object> logList(PageForm pageForm, Integer card_id, LoginInfo info);
	
}
