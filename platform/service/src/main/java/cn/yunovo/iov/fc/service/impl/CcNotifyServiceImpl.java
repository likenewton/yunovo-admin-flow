package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcNotifyMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcNotify;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcNotifyService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 通知或来源统计分析表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-04
 */
@Service
@ConfigurationProperties(prefix="fc.gprs")
public class CcNotifyServiceImpl extends ServiceImpl<ICcNotifyMapper, CcNotify> implements ICcNotifyService {

	@Autowired
	private ICcUserService iCcUserService;

	@Autowired
	private ICcNotifyMapper iCcNotifyMapper;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	private Map<String, String> arr_ntf_type;
	
	@Override
	public PageData<CcNotify,Object> getItemsPage(PageForm pageForm, String ntf_type, String date_start, String date_end,
			LoginInfo info) {
		
		// 组装分页参数
		Page<CcNotify> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());
		
		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("ntf_date");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		
		ntf_type = this.getNtfType(ntf_type);
		
		PageData<CcNotify, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}

		List<CcNotify> records = iCcNotifyMapper.getItemsPage(page, ntf_type, date_start, date_end);

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}
		
		//Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		
		for (CcNotify ccNotify : records) {
			//ccNotify.setOrg_name(orgs.get(String.valueOf(ccNotify.getOrg_id())).getName());
			ccNotify.setNtf_type_name(arr_ntf_type.get(StringUtils.defaultIfEmpty(ccNotify.getNtf_type(), "unknown")));
		}
		
		page.setRecords(records);
		p.setPage(page);

		return p;

	}
	
	@Override
	public List<SelectBean> select(){
		
		Map<String, String>  typeMap = this.getArr_ntf_type();
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
	
	public String getNtfType(String ntfType) {
		return StringUtils.equals(ntfType, "unknown") ? "" : ntfType;
	}

	@Override
	public PageData<CcNotify, Object> getItemsOrgPage(PageForm pageForm, String ntf_type, Integer org_id,String ntf_date, LoginInfo info) {
		
		
		// 组装分页参数
		Page<CcNotify> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());
		
		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("ntf_date");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		
		PageData<CcNotify, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}
		
		QueryWrapper<CcNotify> queryWrapper = new QueryWrapper<>();
		if(StringUtils.isNotEmpty(ntf_type)) {
			queryWrapper.eq("ntf_type", this.getNtfType(ntf_type));
		}
		
		if(org_id != null) {
			queryWrapper.eq("org_id", org_id);
		}
		
		queryWrapper.eq("ntf_date", ntf_date);
		
		iCcNotifyMapper.selectPage(page, queryWrapper);
		List<CcNotify> records = page.getRecords();
		
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			for (CcNotify ccNotify : records) {
				ccNotify.setOrg_name(orgs.get(String.valueOf(ccNotify.getOrg_id())).getName());
				ccNotify.setNtf_type_name(arr_ntf_type.get(StringUtils.defaultIfEmpty(ccNotify.getNtf_type(), "unknown")));
			}
		}
		/*page.setTotal(selectPage.getTotal());
		page.setRecords(selectPage.getRecords());*/
		p.setPage(page);
		return p;
	}

	public Map<String, String> getArr_ntf_type() {
		return arr_ntf_type;
	}

	public void setArr_ntf_type(Map<String, String> arr_ntf_type) {
		this.arr_ntf_type = arr_ntf_type;
	}
	
	
}
