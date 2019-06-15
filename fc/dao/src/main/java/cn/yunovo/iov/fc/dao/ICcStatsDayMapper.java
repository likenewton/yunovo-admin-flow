package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcStatsDay;

/**
 * <p>
 * 流量卡日流量使用情况统计表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcStatsDayMapper extends BaseMapper<CcStatsDay> {

	public List<CcStatsDay> getDayUsePage(IPage<CcStatsDay> page, @Param("card_id")Integer card_id);
	
}
