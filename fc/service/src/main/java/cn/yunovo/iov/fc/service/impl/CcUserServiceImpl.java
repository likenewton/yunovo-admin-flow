package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcUserMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.result.UserResultBean;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 用户管理表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Service
public class CcUserServiceImpl extends ServiceImpl<ICcUserMapper, CcUser> implements ICcUserService {

	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcUserMapper iCcUserMapper;
	
	@Override
	public CcUser findUserOrgAndOrgpos(String username) {
		
		
		CcUser user = iCcUserMapper.getUserInfoByUsernameAndStatus(username);
		
		if(user == null ) {
			return null;
		}
		
		return user;
	}

	@Override
	public String getOrgpos(String loginname) {
		
		CcUser user = this.findUserOrgAndOrgpos(loginname);
		if(user == null) {
			return null;
		}
		
		String orgpos = iCcOrgService.getOrgpos(user.getOrg_id(), user.getOrgpos());
		return orgpos;
	}

	@Override
	public Map<String, String> userMap() {
		
		List<CcUser> selectList = iCcUserMapper.selectList(new QueryWrapper<>());
		if(CollectionUtils.isEmpty(selectList)) {
			return null;
		}
		
		Map<String, String> userMap = selectList.stream().collect(Collectors.toMap(CcUser::getUsername, CcUser::getFirstname));
		
		
		return userMap;
	}

	@Override
	public Map<Integer, String> userIdMap() {
		List<CcUser> selectList = iCcUserMapper.selectList(new QueryWrapper<>());
		if(CollectionUtils.isEmpty(selectList)) {
			return null;
		}
		
		Map<Integer, String> userMap = selectList.stream().collect(Collectors.toMap(CcUser::getUser_id, CcUser::getFirstname));
		
		
		return userMap;
	}
	
	@Override
	public PageData<UserResultBean, Object> userListPage(PageForm pageForm, String username, String firstname, Integer org_id, LoginInfo info) {
		
		// 组装分页参数
		Page<UserResultBean> page = pageForm.build(UserResultBean.class, "username", null);

		PageData<UserResultBean, Object> p = new PageData<>();
		String orgpos = this.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}
		
		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<UserResultBean> records = iCcUserMapper.userListPage(page, username, firstname, org_id, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			Map<Integer, String> orgMaps = iCcOrgService.orgMaps();
			for (UserResultBean user : records) {
				
				user.setOrg_name(user.getOrg_id() == null ? "暂未设置" : orgMaps.get(user.getOrg_id()));
				user.setOrgpos_name(getOrgposName(user.getOrgpos(), orgMaps));
			}
			
		}
		
		page.setRecords(records);
		p.setPage(page);

		return p;
		
	}
	
	private String getOrgposName(String orgpos, Map<Integer, String> orgMap) {
		
		if(StringUtils.isEmpty(orgpos)) {
			return "";
		}
		
		if(StringUtils.equals(orgpos, "*")) {
			return "所有机构";
		}
		
		String[] orgs = orgpos.split(",");
		StringBuffer org_names = new StringBuffer();
		for (String o : orgs) {
			if(orgMap.containsKey(NumberUtils.createInteger(o))) {
				org_names.append(',').append(orgMap.get(NumberUtils.createInteger(o)));
			}
		}
		
		return org_names.toString();
	}

}
