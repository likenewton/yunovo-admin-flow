package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


import cn.yunovo.iov.fc.model.entity.CcOnoffLog;
/**
 * <p>
 * 停卡日志 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-01
 */
public interface ICcOnoffLogMapper extends BaseMapper<CcOnoffLog> {

	
	public List<CcOnoffLog> getItemsPage(IPage<CcOnoffLog> page, @Param("card_id")Integer card_id, @Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
