package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcNotify;

/**
 * <p>
 * 通知或来源统计分析表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-04
 */
public interface ICcNotifyMapper extends BaseMapper<CcNotify> {

	public List<CcNotify> getItemsPage(IPage<CcNotify> page, @Param("ntf_type")String ntf_type, @Param("date_start")String date_start, @Param("date_end")String date_end);
}
