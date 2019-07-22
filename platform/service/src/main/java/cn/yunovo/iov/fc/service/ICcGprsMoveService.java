package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsMove;
import cn.yunovo.iov.fc.model.form.GprsMoveForm;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * GPRS流量迁移表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsMoveService extends IService<CcGprsMove> {

	PageData<CcGprsMove, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid, String old_card_iccid, String date_start,
			String date_end, LoginInfo info);

	
	/**
	 * 流量迁移
	 * @param form
	 * @param info
	 */
	void move(GprsMoveForm form, LoginInfo info);
	
}
