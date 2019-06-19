package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.yunovo.iov.fc.model.entity.CcGprsAllot;

/**
 * <p>
 * 流量分配表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsAllotMapper extends BaseMapper<CcGprsAllot> {

	/**
	 * 获取所有流量卡未过期的套餐记录,根据这些记录处理流量分配
	 * @param card_id 流量卡id, if card_id == null then 获取除废卡机构所有流量卡未过期的套餐记录 else 获取指定卡的未过期套餐记录
	 * @param month_end_type 月结卡类型
	 * @return
	 */
	public List<CcGprsAllot> getUnAllotGprsPack(@Param("card_id")Integer card_id, @Param("month_end_type")Integer month_end_type);
	
}
