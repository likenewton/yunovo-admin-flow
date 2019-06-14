package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量卡发货批次表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcGprsBatchService extends IService<CcGprsBatch> {

	
	/**
	 * 业务管理-出货批次查询
	 * @param form
	 * @param org_id
	 * @param batch_sn
	 * @param date_start
	 * @param date_end
	 * @param info
	 * @return
	 */
	PageData<CcGprsBatch, Object> getItemsPage(PageForm form, Integer org_id, String batch_sn, String date_start,
			String date_end, LoginInfo info);

	List<SelectBean> select();
	
}
