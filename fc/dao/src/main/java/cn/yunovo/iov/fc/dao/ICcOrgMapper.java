package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.yunovo.iov.fc.model.entity.CcOrg;

/**
 * <p>
 * 机构管理表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcOrgMapper extends BaseMapper<CcOrg> {

	public List<CcOrg> getTree(@Param("parent_id")Integer parent_id);
	
	public CcOrg getNameAndByOrgid(@Param("org_id") Integer org_id);
	
	public List<CcOrg> getOrgidAndParentIdByParentid(@Param("parent_id")Integer parent_id);
	
	public List<CcOrg> getListPage(Page<CcOrg> page, @Param("org_id")Integer org_id, @Param("name")String name, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
}
