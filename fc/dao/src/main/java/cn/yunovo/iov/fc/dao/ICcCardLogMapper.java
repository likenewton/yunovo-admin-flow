package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcCardLog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 流量卡日志档案表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcCardLogMapper extends BaseMapper<CcCardLog> {

	public List<CcCardLog> getLogsPage(IPage<CcCardLog> page, @Param("card_id")Integer card_id);
	
}
