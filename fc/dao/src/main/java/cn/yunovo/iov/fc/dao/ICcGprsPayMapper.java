package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;

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
	
	public List<OrgPayReportResultBean> getOrgPayReportPage(IPage<OrgPayReportResultBean> page, @Param("pay_method")Integer pay_method,@Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("mdate")String mdate,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public OrgPayReportResultBean getOrgPayReportTotal(@Param("org_id")Integer org_id,@Param("pay_method")Integer pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("mdate")String mdate,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
