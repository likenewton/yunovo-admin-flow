package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.dao.ICcUserMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.form.UserForm;
import cn.yunovo.iov.fc.model.result.UserResultBean;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
@Slf4j
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
	
	public CcUser getUserByUsername(String username) {
		
		if(StringUtils.isEmpty(username)) {
			return null;
		}
		QueryWrapper<CcUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		
		return this.getOne(queryWrapper, false);
		
	}
	
	@Override
	public boolean onoff(UserForm userForm, LoginInfo user) {
		
		
		CcUser data = this.getUserByUsername(userForm.getUsername());
		if(data == null) {
			log.error("[onoff][未找到对应的用户信息]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("未找到对应的用户信息");
		}
		
		String orgpos = this.getOrgpos(user.getLoginName());
		if(data.getOrg_id() != null && !iCcOrgService.hasPermission(data.getOrg_id(), orgpos)) {
			
			log.error("[onoff][您没有权限变更该用户启用状态]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("[您没有权限变更该用户启用状态");
		}
		
		CcUser ccUser = new CcUser();
		
		ccUser.setUsername(userForm.getUsername());
		ccUser.setUpdate_by(user.getLoginName());
		ccUser.setStatus(userForm.getStatus());
		ccUser.setDate_last(DateUtil.nowStr());
		
		return retBool(iCcUserMapper.updateByUsername(ccUser));
	}
	
	@Override
	public boolean del(UserForm userForm, LoginInfo user) {
		
		CcUser data = this.getUserByUsername(userForm.getUsername());
		
		if(data == null) {
			log.error("[del][未找到对应的用户信息]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("未找到对应的用户信息");
		}
		String orgpos = this.getOrgpos(user.getLoginName());
		if(data.getOrg_id() ==  null && !"*".equals(orgpos)) {
			log.error("[del][您无权删除该用户]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("您无权删除该用户");
		}
		
		if(data.getOrg_id() != null && !iCcOrgService.hasPermission(data.getOrg_id(), orgpos)) {
			log.error("[del][您无权删除该用户]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("您无权删除该用户");
		}
		
		UpdateWrapper<CcUser> deleteWrapper = new UpdateWrapper<>();
		deleteWrapper.eq("username", userForm.getUsername());
		return retBool(iCcUserMapper.delete(deleteWrapper));
		
	}
	
	@Override
	public boolean edit(UserForm userForm, LoginInfo user) {
		
		CcUser data = this.getUserByUsername(userForm.getUsername());
		if(data == null) {
			log.error("[edit][未找到对应的用户信息]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("未找到对应的用户信息");
		}
		
		String orgpos = this.getOrgpos(user.getLoginName());
		if(data.getOrg_id() != null && !iCcOrgService.hasPermission(data.getOrg_id(), orgpos)) {
			
			log.error("[edit][您没有权限变更该用户信息]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("您没有权限变更该用户信息");
		}
		
		String _orgpos = userForm.getOrgpos();
		if(StringUtils.isEmpty(orgpos)) {
			userForm.setOrgpos("");
		}else {
			
			_orgpos = _orgpos.trim();
			if(!"*".equals(_orgpos) && _orgpos.contains("*")) {
				
				log.error("[edit][无效的可控机构]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
				throw new BusinessException("无效的参数【其他可控机构】");
			}
			
			if("*".equals(_orgpos) && !"*".equals(orgpos)) {
				
				log.error("[edit][无效的可控机构]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
				throw new BusinessException("无效的参数【其他可控机构】");
			}
			
			if("*".equals(_orgpos) && "*".equals(orgpos)) {
				
			}else {
				
				Map<Integer, String> orgMaps = iCcOrgService.orgMaps();
				String[] orgs = _orgpos.split(",");
				Integer t  = null;
				for (String o : orgs) {
					
					t = NumberUtils.createInteger(o);
					if(!orgMaps.containsKey(t) || !iCcOrgService.hasPermission(t, orgpos)) {
						log.error("[edit][无效的可控机构]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
						throw new BusinessException("无效的参数【其他可控机构】");
					}
				}
			}
			
			userForm.setOrgpos(_orgpos);
			
		}
		
		CcOrg ccorg = iCcOrgService.getById(userForm.getOrg_id());
		
		if(ccorg == null) {
			log.error("[edit][无效的所属机构]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("无效的所属机构");
		}
		
		Integer total = iCcUserMapper.getTotalByOrg(ccorg.getOrg_id());
		total = (total == null ? 0 : total);
		
		if(ccorg.getUser_total() >= total && userForm.getOrg_id() != data.getOrg_id()) {
			
			log.error("[edit][该机构账户已到达上限]params={},user:{}", userForm.buildJsonString(), user.buildJsonString());
			throw new BusinessException("该机构账户已到达上限");
		}
		
		
		
		CcUser ccUser = new CcUser();
		
		ccUser.setUsername(userForm.getUsername());
		ccUser.setUpdate_by(user.getLoginName());
		ccUser.setStatus(userForm.getStatus());
		ccUser.setDate_last(DateUtil.nowStr());
		ccUser.setOrg_id(userForm.getOrg_id());
		ccUser.setOrgpos(userForm.getOrgpos());
		
		return retBool(iCcUserMapper.updateByUsername(ccUser));
	}

	@Override
	public void userRegister(String username, String firstname) {

		
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(firstname)) {
			
			log.warn("[userRegister][用户信息为空]");
			return;
		}
		
		CcUser ccuser = this.getUserByUsername(username);
		
		if(ccuser == null) {
			ccuser = new CcUser();
			ccuser.setUsername(username);
			ccuser.setFirstname(firstname);
			ccuser.setLastname("");
			ccuser.setCreate_by(username);
			ccuser.setDate_added(DateUtil.nowStr());
			ccuser.setPassword("");
			ccuser.setSalt("");
			ccuser.setEmail("");
			ccuser.setCode("");
			ccuser.setIp("");
			ccuser.setIpaddr("");
			ccuser.setStatus(Short.valueOf("1"));
			this.save(ccuser);
			
		}else {
			ccuser.setFirstname(firstname);
			ccuser.setDate_last(DateUtil.nowStr());
			ccuser.setUpdate_by(username);
			iCcUserMapper.updateByUsername(ccuser);
		}
		
		
		
	}

}
