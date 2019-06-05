package cn.yunovo.iov.fc.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;

/**
 * <p>
 * GPRS流量充值表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPayMapper extends BaseMapper<CcGprsPay> {

	public List<CcGprsPay> getPayCountPage(IPage<CcGprsPay> page, Integer org_id, String date_start, String date_end, String orgpos, String[] orgs);
	
	public CcGprsPay getPayCountTotal(Integer org_id, String date_start, String date_end, String orgpos, String[] orgs);
	
	public List<OrgPayReportResultBean> getOrgPayReportPage(IPage<OrgPayReportResultBean> page, Integer org_id, String date_start, String date_end, String mdate,String orgpos, String[] orgs);
	
	public OrgPayReportResultBean getOrgPayReportTotal(Integer org_id, String date_start, String date_end, String orgpos,String mdate, String[] orgs);
	
}
