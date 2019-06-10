package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.form.OrgForm;

import java.util.List;
import java.util.Map;

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
}
