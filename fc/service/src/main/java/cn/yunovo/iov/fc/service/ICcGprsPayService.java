package cn.yunovo.iov.fc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;

/**
 * <p>
 * GPRS流量充值表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPayService extends IService<CcGprsPay> {

	public PageData<CcGprsPay,CcGprsPay> getPayCountPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);
	
	public PageData<OrgPayReportResultBean, OrgPayReportResultBean> getOrgPayReportPage(PageForm pageForm, Integer org_id, Integer pay_method, String date_start, String mdate, String date_end, LoginInfo info);

	List<SelectBean> select();
}
