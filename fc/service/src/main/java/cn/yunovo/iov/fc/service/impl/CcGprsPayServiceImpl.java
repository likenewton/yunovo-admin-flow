package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsPayMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * GPRS流量充值表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Service
@ConfigurationProperties(prefix="fc.gprs")
public class CcGprsPayServiceImpl extends ServiceImpl<ICcGprsPayMapper, CcGprsPay> implements ICcGprsPayService {

	private Map<String, String> arr_pay_method;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsPayMapper iCcGprsPayMapper;
	
	@Override
	public PageData<PayCountResultBean, PayCountResultBean> getPayCountPage(PageForm pageForm, Integer org_id, String date_start,
			String date_end, LoginInfo info) {
		// 组装分页参数
		Page<PayCountResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<PayCountResultBean, PayCountResultBean> p = new PageData<>();
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

		List<PayCountResultBean> records = iCcGprsPayMapper.getPayCountPage(page, org_id, date_start, date_end, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		PayCountResultBean total = iCcGprsPayMapper.getPayCountTotal(org_id, date_start, date_end, orgpos, orgpos.split(","));
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (PayCountResultBean ccGprsPay : records) {
			ccGprsPay.setOrg_name(orgs.get(String.valueOf(ccGprsPay.getOrg_id())).getName());
		}

		page.setRecords(records);
		p.setPage(page);
		p.setOther(total);

		return p;
	}

	@Override
	public PageData<OrgPayReportResultBean, OrgPayReportResultBean> getOrgPayReportPage(PageForm pageForm, Integer org_id, Integer pay_method,
			String date_start, String mdate, String date_end, LoginInfo info) {
		// 组装分页参数
		Page<OrgPayReportResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<OrgPayReportResultBean, OrgPayReportResultBean> p = new PageData<>();
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

		List<OrgPayReportResultBean> records = iCcGprsPayMapper.getOrgPayReportPage(page, org_id, pay_method,date_start, date_end, mdate, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		OrgPayReportResultBean total = iCcGprsPayMapper.getOrgPayReportTotal(org_id,pay_method, date_start, date_end, mdate, orgpos, orgpos.split(","));
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		Map<String, String>  arr_pay_method = this.getArr_pay_method();
		for (OrgPayReportResultBean ccGprsPay : records) {
			ccGprsPay.setOrg_name(orgs.get(String.valueOf(ccGprsPay.getOrg_id())).getName());
			ccGprsPay.setPay_method_name(arr_pay_method.get(String.valueOf(ccGprsPay.getPay_method())));
		}

		page.setRecords(records);
		p.setPage(page);
		p.setOther(total);

		return p;
	}

	public Map<String, String> getArr_pay_method() {
		return arr_pay_method;
	}
	
	@Override
	public List<SelectBean> select(){
		
		Map<String, String> arr_pay_method = this.getArr_pay_method();
		if(CollectionUtils.isEmpty(arr_pay_method)) {
			return null;
		}
		
		List<SelectBean> select = new ArrayList<>();
		SelectBean bean = null;
		Set<Entry<String, String>> entrySet = arr_pay_method.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		Entry<String, String> temp = null;
		while(it.hasNext()) {
			temp = it.next();
			bean = new SelectBean(temp.getValue(), temp.getKey());
			select.add(bean);
		}
		
		return select;
	}

	public void setArr_pay_method(Map<String, String> arr_pay_method) {
		this.arr_pay_method = arr_pay_method;
	}
	
	
}
