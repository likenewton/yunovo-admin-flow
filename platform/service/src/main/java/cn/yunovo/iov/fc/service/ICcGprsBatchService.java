package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.form.CcGprsBatchForm;
import cn.yunovo.iov.fc.model.result.BatchSaveResultBean;

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

	/**
	 * 通过出货批次获取实名认证后可赠送流量信息
	 * @param batch_id 出货批次id
	 * @return
	 */
	CcGprsBatch getGiveInfoByBatchId(Integer batch_id);

	Double getGprsAmountByBatchId(Integer batch_id);

	BatchSaveResultBean saveBatch(CcGprsBatchForm form, LoginInfo info);

	/**
	 * 通过批次id获取批次详情信息,有数据权限
	 * @param batch_id 批次id
	 * @param info 当前登录者信息
	 * @return
	 */
	CcGprsBatch getInfoByBatchId(Integer batch_id, LoginInfo info);

	boolean updateBatchInfo(CcGprsBatchForm form, LoginInfo info);

	/**
	 * 判断当前机构下是否存在相同配置的批次信息
	 * @param org_id 出货批次机构id
	 * @param device_org_code 设备中心机构id
	 * @param pro_name 项目型号
	 * @param sim_type 卡类型
	 * @return 批次信息
	 */
	public CcGprsBatch check(Integer org_id, String device_org_code, String pro_name, Short sim_type);
	
}
