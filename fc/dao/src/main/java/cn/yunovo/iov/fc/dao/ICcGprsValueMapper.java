package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 流量值表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsValueMapper extends BaseMapper<CcGprsValue> {

	public List<GprsAllotResultBean> getAllotPage(IPage<GprsAllotResultBean> page, @Param("card_id")Integer card_id);
	
}
