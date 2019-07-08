package cn.yunovo.iov.fc.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.export.CcGprsPayExportBean;
import cn.yunovo.iov.fc.model.result.MonthPayReportResultBean;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.model.result.PayListChartDataResultBean;
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
	
	//充值明细查询
	public List<CcGprsPay> getPayListPage(IPage<CcGprsPay> page, @Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	//充值明细导出
	public List<CcGprsPayExportBean> getPayListPageExport(@Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
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
	
	/**
	 * 财务报表-月度充值统计
	 * @param page
	 * @param org_id
	 * @param pay_method
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<MonthPayReportResultBean> getMonthCountPage(IPage<MonthPayReportResultBean> page, @Param("org_id")Integer org_id, @Param("mdate")String mdate,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<PayListChartDataResultBean> getMethodChart(@Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<PayListChartDataResultBean> getPaidChart(@Param("org_id")Integer org_id, @Param("pay_sn")String pay_sn, @Param("card_iccid")String card_iccid, @Param("card_id")Integer card_id, @Param("card_type")Integer card_type, @Param("transfer_id")String transfer_id, @Param("gprs_amount")Double gprs_amount, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("is_paid")Short is_paid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 首页-获取上月支付信息
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public HashMap<String, Object> getSYPayCase(@Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
/*	*//**
	 * 首页-获取上月支付信息
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 *//*
	public HashMap<String, Object> getJRPayData(@Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	*/
	/**
	 * 首页-获取机构下的充值与返利情况
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public HashMap<String, Object> getPayCase(@Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 首页-当月支付信息
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public HashMap<String, Object> getDYPayCase(@Param("date_start")String date_start, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
	/**
	 * 首页-当月流量卡激活数据
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public HashMap<String, Object> getJRActiveData(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 
	 * @param card_id
	 * @return
	 */
	public List<String> listPaysnByCardid(@Param("card_id")Integer card_id);
	
	/**
	 * 
	 * @param card_id
	 * @return
	 */
	public List<String> listPaysnByCardidAndPaymethodAndIspaid(@Param("card_id")Integer card_id);

	
	public int updatePayMethodByCardid(@Param("card_id")Integer card_id);
	
	public List<String> isBuyOtherGprs(@Param("card_id")Integer card_id);
	
	public int savePay(@Param("pay")CcGprsPay pay);
	
	/**
	 * 获取当前机构下所有月份
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<String> getAllMonth(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);

	

}
