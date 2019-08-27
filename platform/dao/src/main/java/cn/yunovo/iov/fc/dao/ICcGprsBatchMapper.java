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
	
	public CcGprsBatch getInfoById(@Param("batch_id")Integer batch_id, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public CcGprsBatch getGiveInfoByBatchId(Integer batch_id);
	
	public int updateCardAmount();

	/**
	 *SELECT
	 * batch_id,batch_sn,batch_name,allot_month,allot_value,allot_reset,live_month,give_value,give_live_month,wszl_value,
	 * wszl_live_month,bind_value,bind_live_month,time_added,sim_type,device_org_code,pro_name
	 * FROM cc_gprs_batch
	 * where device_org_code = ? and pro_name = ? and sim_type = ?
	 * ORDER BY time_added DESC
	 * LIMIT 1
	 * @return 批次信息
	 */
    CcGprsBatch getByDeviceOrgAndProNameAndSimtype(@Param("device_org_code") String device_org_code, @Param("pro_name")String pro_name, @Param("sim_type")Short sim_type);
}
