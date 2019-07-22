package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.form.OrgForm;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 机构管理表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcOrgService extends IService<CcOrg> {

	/**
	 * 递归获取机构下的所有子机构
	 * @param parent_id 父亲编号
	 * @param orgpos 机构串: *全部机构,空字符串表示当前机构且当前机构下属机构
	 * @return 机构列表
	 */
	public Map<String, CcOrg>  getTree(Integer parent_id, String orgpos);
	
	public List<SelectBean> select(Integer parent_id, String orgpos);
	
	public String getOrgpos(Integer orgid, String orgpos);
	
	public PageData<CcOrg, Object> getListPage(PageForm pageForm, Integer org_id, LoginInfo loginInfo);
	
	public int insert(OrgForm form, LoginInfo info);

	public int update(OrgForm org, LoginInfo loginBaseInfo);

	public int delete(Integer[] orgs, LoginInfo loginBaseInfo);

	/**
	 * 判断当前用户是否有对应机构的操作权
	 * @param org_id 被操作的机构id
	 * @param orgpos 当前用户可管理的机构id,多个逗号分割,*号代表可管理所有机构
	 * @return true | false, true 表示拥有该机构的管理权，false 则表示没有权限
	 */
	boolean hasPermission(Integer org_id, String orgpos);

	/**
	 * 机构字典接口
	 * @return
	 */
	Map<Integer, String> orgMaps();
}
