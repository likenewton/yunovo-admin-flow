package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.yunovo.iov.fc.model.entity.CcGprsPack;

/**
 * <p>
 * 流量充值套餐表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPackMapper extends BaseMapper<CcGprsPack> {

	public List<CcGprsPack> getPack(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<CcGprsPack> getItemsPage(Page<CcGprsPack> page, @Param("org_id")Integer org_id, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 获取套餐数
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public Integer getPackNum(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 通过套餐id 获取套餐信息
	 * @param pack_id
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public CcGprsPack getByPackId(@Param("pack_id") Integer pack_id, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
}
