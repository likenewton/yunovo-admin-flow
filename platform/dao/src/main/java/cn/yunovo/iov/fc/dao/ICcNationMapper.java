package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcNation;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 国家区域表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
public interface ICcNationMapper extends BaseMapper<CcNation> {

	public List<CcNation> queryListByParent(@Param("parent")Integer parent);
	
	public List<CcNation> queryList();
}
