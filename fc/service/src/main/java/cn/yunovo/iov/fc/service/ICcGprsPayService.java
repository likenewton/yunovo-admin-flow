package cn.yunovo.iov.fc.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;

/**
 * <p>
 * GPRS流量充值表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPayService extends IService<CcGprsPay> {

	PageData<PayCountResultBean,PayCountResultBean> getPayCountPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);
	
	PageData<OrgPayReportResultBean, OrgPayReportResultBean> getOrgPayReportPage(PageForm pageForm, Integer org_id, Integer pay_method, String date_start, String mdate, String date_end, LoginInfo info);

	List<SelectBean> select();
	
	PageData<CcGprsPay, CcGprsPay> getPayListPage(PageForm pageForm, Integer org_id, String pay_sn, String card_iccid, Integer card_id, Integer card_type, String transfer_id, Double gprs_amount, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String  paid_start, String paid_end, LoginInfo info);
	
}
