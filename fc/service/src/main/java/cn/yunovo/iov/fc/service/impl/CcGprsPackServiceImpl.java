package cn.yunovo.iov.fc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.dao.ICcGprsPackMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPack;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.form.GprsPackForm;
import cn.yunovo.iov.fc.service.ICcGprsPackService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量充值套餐表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Service
@Slf4j
public class CcGprsPackServiceImpl extends ServiceImpl<ICcGprsPackMapper, CcGprsPack> implements ICcGprsPackService {

	@Autowired
	private ICcGprsPackMapper iCcGprsPackMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Override
	public List<SelectBean> select(LoginInfo info) {
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return null;
		}
		
		List<CcGprsPack> packs = iCcGprsPackMapper.getPack(orgpos, orgpos.split(","));
		
		if(CollectionUtils.isEmpty(packs)) {
			return null;
		}
		
		List<SelectBean> result = new ArrayList<>();
		for (CcGprsPack ccGprsPack : packs) {
			result.add(new SelectBean(ccGprsPack.getGprs_amount(),ccGprsPack.getGprs_amount()));
		}
		
		return result;
	}
	
	

	@Override
	public PageData<CcGprsPack, Object> getItemsPage(PageForm form, Integer org_id, LoginInfo info) {
		
		Page<CcGprsPack> page = form.build(CcGprsPack.class, null, "time_added");
		PageData<CcGprsPack, Object> returnData = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}

		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}

		List<CcGprsPack> records = iCcGprsPackMapper.getItemsPage(page, org_id, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcGprsPack ccGprsPack : records) {

				ccGprsPack.setOrg_name(getGprsPackOrg(ccGprsPack.getOrg_id(), orgs));
				ccGprsPack.setFirst_name(userMap.get(ccGprsPack.getUser_id()));
				ccGprsPack.setAlter_name(userMap.get(ccGprsPack.getAlter_id()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	public String getGprsPackOrg(Integer org_id, Map<String, CcOrg> orgs) {
		
		if(org_id == null) {
			return "";
		}
		
		if(org_id == 0) {
			return "公共套餐";
		}
		
		CcOrg org = orgs.get(String.valueOf(org_id));
		
		return org == null ? "":org.getName();
	}
	
	@Override
	public void save(GprsPackForm form, LoginInfo info) {
		
		CcGprsPack target = new CcGprsPack();
		BeanUtils.copyProperties(form, target);
		target.setTime_added(DateUtil.nowStr());
		target.setUser_id(info.getId());
		boolean  isOk = this.save(target);
		if(!isOk) {
			
			log.warn("[save][套餐新增失败]params={form:{},target:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(target),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "新增套餐失败");
		}
	}

	@Override
	public CcGprsPack detail(Integer pack_id, LoginInfo info) {
		
		if(pack_id == null) {
			return null;
		}
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return null;
		}
		return iCcGprsPackMapper.getByPackId(pack_id, orgpos, orgpos.split(","));
	}

	@Override
	public void update(GprsPackForm form, LoginInfo info) {

		CcGprsPack pack = this.detail(form.getPack_id(), info);
		if(pack == null) {
			throw new BusinessException("未找到对应的套餐信息");
		}
		
		CcGprsPack target = new CcGprsPack();
		BeanUtils.copyProperties(form, target);
		target.setAlter_id(info.getId());
		target.setTime_modify(DateUtil.nowStr());
		boolean  isOk = this.updateById(target);
		
		if(!isOk) {
			
			log.warn("[save][变更套餐信息失败]params={form:{},target:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(target),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "变更套餐信息失败");
		}
	}



	@Override
	public void stop(GprsPackForm form, LoginInfo info) {
		
		CcGprsPack pack = this.detail(form.getPack_id(), info);
		if(pack == null) {
			log.warn("[stop][未找到对应的套餐信息]params={form:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(info));
			throw new BusinessException("未找到对应的套餐信息");
		}
		
		pack = new CcGprsPack();
		pack.setAlter_id(info.getId());
		pack.setPack_id(form.getPack_id());
		pack.setPack_status(form.getPack_status() == null? 0 : form.getPack_status());
		boolean  isOk = this.updateById(pack);
		if(!isOk) {
			
			log.warn("[stop][套餐停用操作失败]params={form:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "套餐停用操作失败");
		}
	}

}
