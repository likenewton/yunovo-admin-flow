package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsBatchMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcNationService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量卡发货批次表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Service
@ConfigurationProperties(prefix = "fc.gprs")
public class CcGprsBatchServiceImpl extends ServiceImpl<ICcGprsBatchMapper, CcGprsBatch> implements ICcGprsBatchService {

	@Autowired
	private ICcUserService iCcUserService;
	
	private Map<String, String> arr_live_month;
	
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsBatchMapper iCcGprsBatchMapper;
	
	@Autowired
	private ICcNationService iCcNationService;
	
	@Override
	public PageData<CcGprsBatch, Object> getItemsPage(PageForm form, Integer org_id, String batch_sn, String date_start,
			String date_end, LoginInfo info) {
		
		Page<CcGprsBatch> page = form.build(CcGprsBatch.class, null, "time_added");
		PageData<CcGprsBatch, Object> returnData = new PageData<>();
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
		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		List<CcGprsBatch> records = iCcGprsBatchMapper.getItemsPage(page, org_id, batch_sn, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			JSONObject nation = iCcNationService.nationMap();
			for (CcGprsBatch ccGprsBatch : records) {

				ccGprsBatch.setOrg_name(orgs.get(String.valueOf(ccGprsBatch.getOrg_id())).getName());
				ccGprsBatch.setFirst_name(StringUtils.defaultIfEmpty(userMap.get(ccGprsBatch.getUser_id()), String.valueOf(ccGprsBatch.getUser_id())));
				ccGprsBatch.setProvince_name(ccGprsBatch.getProvince_id() == null ? "": StringUtils.defaultIfEmpty(nation.getString(ccGprsBatch.getProvince_id().toString()),ccGprsBatch.getProvince_id().toString()));
				ccGprsBatch.setDistrict_name(ccGprsBatch.getDistrict_id() == null ? "": StringUtils.defaultIfEmpty(nation.getString(ccGprsBatch.getDistrict_id().toString()),ccGprsBatch.getDistrict_id().toString()));
				ccGprsBatch.setCity_name(ccGprsBatch.getCity_id() == null ? "": StringUtils.defaultIfEmpty(nation.getString(ccGprsBatch.getCity_id().toString()),ccGprsBatch.getCity_id().toString()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		
		return returnData;
	}

	public Map<String, String> getArr_live_month() {
		return arr_live_month;
	}

	public void setArr_live_month(Map<String, String> arr_live_month) {
		this.arr_live_month = arr_live_month;
	}

	@Override
	public List<SelectBean> select(){
		
		Map<String, String>  typeMap = this.getArr_live_month();
		if(typeMap == null || typeMap.isEmpty()) {
			return null;
		}
		
		List<SelectBean> select = new ArrayList<>();
		SelectBean bean = null;
		Set<Entry<String, String>> entrySet = typeMap.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		Entry<String, String> temp = null;
		while(it.hasNext()) {
			temp = it.next();
			bean = new SelectBean(temp.getValue(), temp.getKey());
			select.add(bean);
		}
		
		return select;
		
		
	}
	
	
}
