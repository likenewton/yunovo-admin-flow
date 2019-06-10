package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcUserMapper;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

}
