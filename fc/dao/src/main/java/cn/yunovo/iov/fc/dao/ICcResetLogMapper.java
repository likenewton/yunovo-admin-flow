package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcResetLog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 重置流量卡日志 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcResetLogMapper extends BaseMapper<CcResetLog> {

	public List<CcResetLog> getItemsPage(IPage<CcResetLog> page, @Param("org_id")Integer org_id, @Param("card_iccid")String card_iccid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
