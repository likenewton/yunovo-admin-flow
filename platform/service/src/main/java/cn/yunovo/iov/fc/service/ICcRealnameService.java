package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.model.form.RealnameForm;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量卡实名制表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcRealnameService extends IService<CcRealname> {

	PageData<CcRealname, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid, String date_start,
			String date_end,Integer status, LoginInfo info);

	/**
	 * 实名审批
	 * @param form 
	 * @param loginInfo
	 * @return
	 */
	boolean audit(RealnameForm form, LoginInfo loginInfo);

	boolean unbind(RealnameForm form, LoginInfo loginBaseInfo);

	void getItemsPageExport(Integer org_id, String card_iccid, String date_start, String date_end, Integer status,
			LoginInfo info) throws IOException;
}
