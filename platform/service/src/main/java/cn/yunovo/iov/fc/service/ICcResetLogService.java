package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.result.CardResetForm;
import cn.yunovo.iov.fc.model.result.CardRestBean;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 重置流量卡日志 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcResetLogService extends IService<CcResetLog> {

	/**
	 * 重置流量卡日志历史列表查询接口
	 * @param form
	 * @param org_id
	 * @param card_iccid
	 * @param date_start
	 * @param date_end
	 * @param info
	 * @return
	 */
	PageData<CcResetLog, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid, String date_start,
			String date_end, LoginInfo info);

	List<CardRestBean> cardReset(CardResetForm form, LoginInfo info);

}
