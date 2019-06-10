package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.Md5Util;
import cn.yunovo.iov.fc.common.utils.PinYinUtil;
import cn.yunovo.iov.fc.dao.ICcOrgMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.model.form.OrgForm;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 机构管理表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Service
public class CcOrgServiceImpl extends ServiceImpl<ICcOrgMapper, CcOrg> implements ICcOrgService {

	@Autowired
	private  JedisPoolUtil jedisPoolUtil;
	
	@Autowired
	private ICcOrgMapper iCcOrgMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	
	@Override
	public Map<String, CcOrg>  getTree(final Integer parent_id, final String orgpos) {
		
		Integer _parent_id = (parent_id == null? 0 : parent_id);
		String _orgpos = StringUtils.defaultIfEmpty(orgpos, "");
		
		
		String mkey = FcConstant.CACHE_RESOURCE_PREFIX+"HASH-ORGS-" + Md5Util.getMD5String("" + _parent_id + _orgpos);
		String cacheData = jedisPoolUtil.get(mkey);
		List<CcOrg> orgs = null;
		Map<String, CcOrg> data = null, copyData = null;
		if(StringUtils.isEmpty(cacheData)) {
			
			orgs = this._getTree(_parent_id);
			if(CollectionUtils.isEmpty(orgs)) {
				return null;
			}
			data = new LinkedHashMap<>();
			for (CcOrg ccOrg : orgs) {
				data.put(ccOrg.getOrgIdStr(), ccOrg);
			}
			//data =orgs.stream().collect(Collectors.toMap(CcOrg::getOrgIdStr,Function.identity()));
			copyData = new LinkedHashMap<>(data);
			//* 表示拥有所有机构数据列表，非 * 值只有用户所属机构及所属机构下级机构的权限
			if(!StringUtils.isEmpty(orgpos) && !StringUtils.equals(orgpos, "*")) {
				Set<Entry<String, CcOrg>> entrySet = data.entrySet();
				Iterator<Entry<String, CcOrg>> it = entrySet.iterator();
				Entry<String, CcOrg> temp = null;
				String _orgpos2 = ","+orgpos+",";
				while(it.hasNext()) {
					
					temp = it.next();
					if(!StringUtils.contains(_orgpos2, ","+temp.getKey()+",")) {
						copyData.remove(temp.getKey());
					}
				}
			}
			
			data = copyData;
			
			jedisPoolUtil.setEx(mkey, JSONObject.toJSONString(data));
			//data = new LinkedHashMap<>(data);
			
		}else {
			data = JSON.parseObject(cacheData, new TypeReference<LinkedHashMap<String, CcOrg>>() {});
			
			
		}
		
		return data;
	}
	
	public CcOrg getByName(String name){
		
		QueryWrapper<CcOrg> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", name);
		return iCcOrgMapper.selectOne(queryWrapper);
	}
	
	/**
	 * 递归获取机构下的所有子机构(按树结构)
	 * @param parent_id 父级机构编号
	 * @return 机构列表
	 */
	private List<CcOrg> _getTree(Integer parent_id){
		
		
		List<CcOrg> data = iCcOrgMapper.getTree(parent_id);
		
		List<CcOrg> orgs = new ArrayList<>();
		if(CollectionUtils.isEmpty(data)) {
			return Collections.emptyList();
		}
		
		for (CcOrg ccOrg : data) {
			
			ccOrg.setName(this.getPath(ccOrg.getOrg_id()));
			orgs.add(ccOrg);
			orgs.addAll(this._getTree(ccOrg.getOrg_id()));
		}
		return orgs;
	}

	public String getPath( Integer org_id) {
		
		if(org_id == null) {
			return null;
		}
		
		String mkey = FcConstant.CACHE_RESOURCE_PREFIX+"HASH-ORGS-PATH-"+org_id;
		String path = jedisPoolUtil.get(mkey);
		
		if(StringUtils.isEmpty(path)) {
			path = this._getPath(org_id);
			jedisPoolUtil.setEx(mkey, path, 1800);
			
		}
		return path;
	}
	
	private String _getPath(Integer org_id) {
		
		if(org_id == 0) {
			return "";
		}
		
		CcOrg org = iCcOrgMapper.getNameAndByOrgid(org_id);
		
		if(org.getParent_id() == null || org.getParent_id() == 0) {
			return org.getName();
		}
		
		return this._getPath(org.getParent_id()) + " > " + org.getName();
	}

	@Override
	public List<SelectBean> select(Integer parent_id, String orgpos) {
		
		
		orgpos = this.getOrgpos(parent_id, orgpos);
		
		Map<String, CcOrg> orgs = this.getTree(0, orgpos);
		
		if(orgs == null || orgs.isEmpty()) {
			return null;
		}
		
		Set<Entry<String, CcOrg>>  entrySet = orgs.entrySet();
		Iterator<Entry<String, CcOrg>> it = entrySet.iterator();
		
		List<SelectBean> result = new ArrayList<>(orgs.size());
		Entry<String, CcOrg>  temp = null;
		SelectBean temp1 = null;
		while(it.hasNext()) {
			
			temp = it.next();
			temp1 = new SelectBean();
			
			temp1.setLabel(temp.getValue().getName() + " " + temp.getValue().getSpell());
			temp1.setValue(temp.getValue().getOrgIdStr());
			
			result.add(temp1);
		}
		
		
		return result;
	}
	
	public String getOrgpos(Integer orgid, String orgpos) {
		
		if(StringUtils.equals(orgpos, "*")) {
			
			return orgpos;
		}else {
			return this.getSubOrgId(orgid) + orgid + (StringUtils.isEmpty(orgpos) ? "" : (","+orgpos));
		}
	}
	
	public String getSubOrgId(Integer parent_id) {
		
		String opos = "";
		
		List<CcOrg> orgs = iCcOrgMapper.getOrgidAndParentIdByParentid(parent_id);
		
		if(!CollectionUtils.isEmpty(orgs)) {
			CcOrg temp = null;
			for (CcOrg ccOrg : orgs) {
				temp = ccOrg;
				opos = opos + temp.getOrg_id() + "," + this.getSubOrgId(temp.getOrg_id());
			}
		}
		
		return opos;
	}

	@Override
	public PageData<CcOrg, Object> getListPage(PageForm pageForm, Integer org_id, LoginInfo info) {
		
		// 组装分页参数
		Page<CcOrg> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("time_added");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcOrg, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<CcOrg> records = iCcOrgMapper.getListPage(page, org_id, null, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		Map<Integer, String> userMap = iCcUserService.userIdMap();
		if(!CollectionUtils.isEmpty(userMap)) {
			for (CcOrg ccOrg : records) {
				
				ccOrg.setCreate_by_name(userMap.get(ccOrg.getUser_id()));
				ccOrg.setUpdate_by_name(userMap.get(ccOrg.getAlter_id()));
			}
		}

		page.setRecords(records);
		p.setPage(page);

		return p;
		
	}
	
	@Override
	public boolean permission(Integer org_id, String orgpos) {
		
		if(org_id == null || org_id < 0 || StringUtils.isEmpty(orgpos)) {
			return false;
		}
		
		if(StringUtils.equals("*", orgpos)) {
			return true;
		}
		
		return StringUtils.contains(","+orgpos+",", ","+org_id+",");
	}

	@Override
	public int insert(OrgForm form, LoginInfo info) {
		
		CcUser user = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());
		
		if(user == null) {
			throw new BusinessException("无效操作");
		}
		
		String orgpos = this.getOrgpos(user.getOrg_id(), user.getOrgpos());
		
		if(!this.permission(form.getParent_id(), orgpos)) {
			throw new BusinessException(405,"抱歉您无权限在此父机构下创建所属子机构!");
		}
		
		if(this.getByName(form.getName()) != null) {
			throw new BusinessException(String.format("系统提示： 该【%s】机构已存在！", form.getName()));
		}
		
		CcOrg ccOrg = new CcOrg();
		BeanUtils.copyProperties(form, ccOrg);
		
		ccOrg.setSpell(PinYinUtil.getFirstSpell(ccOrg.getName(), HanyuPinyinCaseType.UPPERCASE));
		ccOrg.setPartner_id(RandomStringUtils.randomAlphanumeric(15));
		ccOrg.setPartner_key(Md5Util.getMD5String(RandomStringUtils.randomAlphanumeric(20)));
		ccOrg.setTime_added(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ccOrg.setUser_id(user.getUser_id());
		return iCcOrgMapper.insert(ccOrg);
		
	}

	@Override
	public int update(OrgForm org, LoginInfo info) {
		
		CcUser user = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());
		
		if(user == null) {
			throw new BusinessException("无效操作");
		}
		
		String orgpos = this.getOrgpos(user.getOrg_id(), user.getOrgpos());
		
		if(!this.permission(org.getOrg_id(), orgpos)) {
			throw new BusinessException("系统提示：您无权限修改该机构信息");
		}
		
		if(!this.permission(org.getParent_id(), orgpos)) {
			throw new BusinessException("抱歉您无权限在此父机构下创建所属子机构");
		}
		
		CcOrg orgInfo = this.getByName(org.getName());
		if(orgInfo != null && (orgInfo.getOrg_id() - org.getOrg_id()!= 0)) {
			throw new BusinessException(String.format("系统提示： 该【%s】机构已存在！", org.getName()));
		}
		
		
		
		CcOrg ccOrg = new CcOrg();
		BeanUtils.copyProperties(org, ccOrg);
		ccOrg.setSpell(PinYinUtil.getFirstSpell(ccOrg.getName(), HanyuPinyinCaseType.UPPERCASE));
		ccOrg.setAlter_id(user.getUser_id());
		ccOrg.setTime_modify(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		return iCcOrgMapper.updateById(ccOrg);
	}

	@Override
	public int delete(Integer[] orgs, LoginInfo info) {
		
		CcUser user = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());
		
		if(user == null) {
			throw new BusinessException("无效操作");
		}
		
		if(orgs == null || orgs.length < 1) {
			throw new BusinessException("系统提示：请选择您需要删除的机构");
		}
		
		String orgpos = this.getOrgpos(user.getOrg_id(), user.getOrgpos());
		for (Integer o : orgs) {
			
			if(!this.permission(o, orgpos)) {
				throw new BusinessException("系统提示：您无权删除该机构");
			}
			
			if(o - user.getOrg_id() == 0) {
				
				throw new BusinessException("系统提示： 您不能删除自己所属机构！");
			}
		}
		
		return iCcOrgMapper.deleteBatchIds(Arrays.asList(orgs));
		
	}
	
	
}
