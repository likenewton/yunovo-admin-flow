package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.model.result.PayListTotalResulBean;
import cn.yunovo.iov.fc.model.result.PayPackResultBean;

/**
 * <p>
 * GPRS流量充值表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPayMapper extends BaseMapper<CcGprsPay> {

	public List<PayCountResultBean> getPayCountPage(IPage<PayCountResultBean> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public PayCountResultBean getPayCountTotal(@Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<OrgPayReportResultBean> getOrgPayReportPage(IPage<OrgPayReportResultBean> page, @Param("org_id")Integer org_id, @Param("pay_method")Integer pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("mdate")String mdate,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public OrgPayReportResultBean getOrgPayReportTotal(@Param("org_id")Integer org_id,@Param("pay_method")Integer pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("mdate")String mdate,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<CcGprsPay> getPayListPage(IPage<CcGprsPay> page, @Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public PayListTotalResulBean getPayListTotal(@Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 财务报表-套餐充值统计
	 * @param page
	 * @param org_id
	 * @param pay_method
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<PayPackResultBean> getPayPackPage(IPage<PayPackResultBean> page, @Param("org_id")Integer org_id, @Param("pay_method")Short pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public PayPackResultBean getPayPackTotal(@Param("org_id")Integer org_id, @Param("pay_method")Short pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
