package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.yunovo.iov.fc.model.entity.CcStatsMonth;
import cn.yunovo.iov.fc.model.export.CcStatsMonthExportBean;

/**
 * <p>
 * 流量卡月流量使用情况统计表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcStatsMonthMapper extends BaseMapper<CcStatsMonth> {

	public List<String> getMonthsByOrgs(@Param("orgs")String[] orgs);
	
	public List<String> getAllMonths();
	
	public List<CcStatsMonth> queryItemsPage(Page<CcStatsMonth> page, @Param("org_id")Integer org_id, @Param("card_type")Integer card_type, @Param("card_iccid")String card_iccid, @Param("mdate")String mdate, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<CcStatsMonthExportBean> queryItemsPageExport(@Param("org_id")Integer org_id, @Param("card_type")Integer card_type, @Param("card_iccid")String card_iccid, @Param("mdate")String mdate, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public Double usedTotal(@Param("org_id")Integer org_id, @Param("card_type")Integer card_type, @Param("card_iccid")String card_iccid, @Param("mdate")String mdate, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<CcStatsMonth> getMonthUsePage(IPage<CcStatsMonth> page, @Param("card_id")Integer card_id);
	
	public Double getWlistTotalByCardId(@Param("card_id")Integer card_id);
	
	public Integer updateForSql(@Param("value")String value);
}
