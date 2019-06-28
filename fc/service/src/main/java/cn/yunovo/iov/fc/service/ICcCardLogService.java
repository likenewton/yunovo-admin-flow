package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.entity.CcOnoffLog;
import cn.yunovo.iov.fc.model.entity.CcRealname;

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

	/**
	 * 流量卡实名认证日志
	 * @param res 实名数据
	 * @param unbind 是否解除实名 true 解除实名，flase 绑定实名
	 */
	boolean log10Rlname(CcRealname res, boolean unbind);

	boolean log5On6Off(CcOnoffLog res, boolean isSuccess);

	public boolean log9Reset(Integer card_id, Double unicom_month, Double unicom_total, String nowStr);
	
}
