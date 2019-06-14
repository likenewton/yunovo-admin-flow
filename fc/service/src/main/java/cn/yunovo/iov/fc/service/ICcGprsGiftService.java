package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsGift;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * GPRS流量赠送表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsGiftService extends IService<CcGprsGift> {

	
	/**
	 * 流量赠送历史列表查询接口
	 * @param form
	 * @param org_id
	 * @param card_iccid
	 * @param date_start
	 * @param date_end
	 * @param info
	 * @return
	 */
	PageData<CcGprsGift, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid, String date_start,
			String date_end, LoginInfo info);
}
