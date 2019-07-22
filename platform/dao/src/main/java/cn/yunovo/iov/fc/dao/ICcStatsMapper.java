package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.model.export.CcStatsExportBean;
import cn.yunovo.iov.fc.model.export.CcStatsOrgExportBean;
import cn.yunovo.iov.fc.model.result.HomeChartDataBean;
import cn.yunovo.iov.fc.model.result.HomeSimTrendInfoBean;

/**
 * <p>
 * 各项参数统计表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-03
 */
public interface ICcStatsMapper extends BaseMapper<CcStats> {

	
	/**
	 * 运营数据列表
	 * @return
	 */
	public List<CcStats> getItemsPage(IPage<CcStats> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	public List<CcStatsExportBean> getItemsPageExport(@Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
	/**
	 * 机构运营统计
	 * @return
	 */
	public List<CcStats> getOrgItemsPage(IPage<CcStats> page, @Param ("org_id")Integer org_id, @Param("stats_date")String stats_date,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	public List<CcStatsOrgExportBean> getOrgItemsPageExport(@Param("org_id")Integer org_id, @Param("stats_date")String stats_date,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
	/**
	 * SIM卡使用趋势
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeSimTrendInfoBean> simTrend(@Param("stype")Integer stype, @Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 充值趋势
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> topupTrend(@Param("stype")Integer stype, @Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 机构充值排名
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> getOrgTopupRank(@Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 续费订单趋势
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> orderTrend(@Param("stype")Integer stype, @Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 机构订单排名
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> getOrgOrderRank(@Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 流量消耗趋势
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> gprsTrend(@Param("stype")Integer stype, @Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 机构流量消耗排名
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> getOrgGprsRank(@Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
	/**
	 * 客单价趋势
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> priceTrend(@Param("stype")Integer stype, @Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 机构客单价排名
	 * @param stype
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<HomeChartDataBean> getOrgPriceRank(@Param("date_start")String date_start, @Param("date_end")String date_end,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
}
