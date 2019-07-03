package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsBatch;

/**
 * <p>
 * 流量卡发货批次表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcGprsBatchMapper extends BaseMapper<CcGprsBatch> {

	public List<CcGprsBatch> getItemsPage(IPage<CcGprsBatch> page, @Param("org_id")Integer org_id, @Param("batch_sn")String batch_sn, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public CcGprsBatch getGiveInfoByBatchId(Integer batch_id);
	
	public int updateCardAmount();
}
