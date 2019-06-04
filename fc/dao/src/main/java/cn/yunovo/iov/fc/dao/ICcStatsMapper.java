package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcStats;

/**
 * <p>
 * 各项参数统计表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-03
 */
public interface ICcStatsMapper extends BaseMapper<CcStats> {

	public List<CcStats> getItemsPage(IPage<CcStats> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
}
