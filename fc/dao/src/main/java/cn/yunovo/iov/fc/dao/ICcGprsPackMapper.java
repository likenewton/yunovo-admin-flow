package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}