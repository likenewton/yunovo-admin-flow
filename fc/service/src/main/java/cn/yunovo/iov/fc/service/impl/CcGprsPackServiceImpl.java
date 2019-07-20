package cn.yunovo.iov.fc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.math.MathUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
			Map<String, String> userMap = iCcUserService.userMap();
			for (CcGprsPack ccGprsPack : records) {

				ccGprsPack.setOrg_name(getGprsPackOrg(ccGprsPack.getOrg_id(), orgs));
				ccGprsPack.setCreate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsPack.getCreate_by()), ccGprsPack.getCreate_by()));
				ccGprsPack.setUpdate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsPack.getUpdate_by()), ccGprsPack.getUpdate_by()));
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
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		boolean has = iCcOrgService.hasPermission(form.getOrg_id(), orgpos);
		if(!has) {
			log.warn("[update][无效的套餐机构]params={form:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "无效的套餐机构");
		}
		CcGprsPack pack = iCcGprsPackMapper.getByOrgAndGprsAmountAndPrice(form.getOrg_id(), form.getGprs_amount(), form.getGprs_price());
		if(pack != null) {
			log.warn("[save][该流量的价格套餐已添加！请勿重复操作]params={form:{},target:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(pack),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "该流量的价格套餐已添加！请勿重复操作");
		}
		pack = new CcGprsPack();
		BeanUtils.copyProperties(form, pack);
		pack.setPack_id(null);
		pack.setTime_added(DateUtil.nowStr());
//		pack.setUser_id(info.getId());
		//pack.setUser_id(0);
		pack.setCreate_by(info.getLoginName());
		pack.setPack_status((short)1);
		boolean  isOk = this.save(pack);
		if(!isOk) {
			
			log.warn("[save][套餐新增失败]params={form:{},target:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(pack),JSONObject.toJSONString(info));
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

		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		boolean has = iCcOrgService.hasPermission(form.getOrg_id(), orgpos);
		if(!has) {
			log.warn("[update][无效的套餐机构]params={form:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "无效的套餐机构");
		}
		CcGprsPack pack = this.detail(form.getPack_id(), info);
		if(pack == null) {
			throw new BusinessException("未找到对应的套餐信息");
		}
		
		pack = iCcGprsPackMapper.getByOrgAndGprsAmountAndPrice(form.getOrg_id(), form.getGprs_amount(), form.getGprs_price());
		if(pack != null && form.getPack_id().doubleValue() != pack.getPack_id().doubleValue()) {
			log.warn("[update][该流量的价格套餐已存在！请勿重复操作]params={form:{},target:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(pack),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "该流量的价格套餐已存在！请勿重复操作");
		}
		
		CcGprsPack target = new CcGprsPack();
		BeanUtils.copyProperties(form, target);
		//target.setAlter_id(0);
		target.setUpdate_by(info.getLoginName());
//		target.setAlter_id(info.getId());
		target.setTime_modify(DateUtil.nowStr());
		target.setPack_status(null);
		
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
//		pack.setAlter_id(info.getId());
//		pack.setAlter_id(0);
		pack.setUpdate_by(info.getLoginName());
		pack.setPack_id(form.getPack_id());
		pack.setPack_status(form.getPack_status() == null ? 0 : form.getPack_status());
		boolean  isOk = this.updateById(pack);
		if(!isOk) {
			
			log.warn("[stop][套餐停用操作失败]params={form:{},login:{}}", JSONObject.toJSONString(form),JSONObject.toJSONString(info));
			throw new BusinessException(-1, "套餐停用操作失败");
		}
	}

	
	/**
	 * 格式化套餐有效周期
	 *
	 * @param float $live_month 有效周期
	 * @return string 有效周期
	 */
	@Override
	public String liveFormat(Float live_month) {
		
		return (live_month < 1) ? ((MathUtils.round(live_month, 2).intValue() * 100) + "天") : (live_month == 999 ? "无限期": live_month.intValue() + "个月" );
	}
	
}
