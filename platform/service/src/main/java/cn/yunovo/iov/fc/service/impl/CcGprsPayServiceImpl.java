package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.dao.ICcGprsPayMapper;
import cn.yunovo.iov.fc.model.FcProperties;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.export.CcGprsPayExportBean;
import cn.yunovo.iov.fc.model.result.MonthPayReportResultBean;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.model.result.PayListChartDataResultBean;
import cn.yunovo.iov.fc.model.result.PayListTotalResulBean;
import cn.yunovo.iov.fc.model.result.PayPackResultBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcNotifyService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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
@Slf4j
public class CcGprsPayServiceImpl extends ServiceImpl<ICcGprsPayMapper, CcGprsPay> implements ICcGprsPayService {

	private Map<String, String> arr_pay_method;
	
	@Autowired
	private FcProperties fcProperties;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsPayMapper iCcGprsPayMapper;
	
	@Autowired
	private ICcNotifyService iCcNotifyService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	private final String GET_ALL_MONTH_SQL = "SELECT LEFT(time_added, 7) AS mdate FROM cc_gprs_pay  WHERE 1 = 1 AND org_id = %s GROUP BY mdate ORDER BY mdate DESC";
	
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

	@Override
	public PageData<CcGprsPay, PayListTotalResulBean> getPayListPage(PageForm pageForm, Integer org_id, String pay_sn,
			String card_iccid, Integer card_id, Integer card_type, String transfer_id, Double gprs_amount,
			String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String paid_start,
			String paid_end, LoginInfo info) {
		
		// 组装分页参数
		Page<CcGprsPay> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("P.time_added");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcGprsPay, PayListTotalResulBean> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
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
		
		if(StringUtils.isNotEmpty(pay_from)) {
			pay_from = iCcNotifyService.getNtfType(pay_from);
		}

		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		if (StringUtils.isNotEmpty(paid_start)) {
			paid_start = paid_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(paid_end)) {
			paid_end = paid_end + " 23:59:59";
		}

		List<CcGprsPay> records = iCcGprsPayMapper.getPayListPage(page, org_id, pay_sn, card_iccid, card_id, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		Map<String, String>  arr_pay_method = this.getArr_pay_method();
		Map<String, String> ntfType = iCcNotifyService.getArr_ntf_type();
		Map<String, String> card_types = iCcGprsCardService.getCard_type();
		for (CcGprsPay ccGprsPay : records) {
			ccGprsPay.setOrg_name(orgs.get(String.valueOf(ccGprsPay.getOrg_id())).getName());
			ccGprsPay.setPay_method_name(arr_pay_method.get(String.valueOf(ccGprsPay.getPay_method())));
			ccGprsPay.setPay_from_name(ntfType.get(StringUtils.defaultIfEmpty(ccGprsPay.getPay_from(), "unknown")));
			ccGprsPay.setCard_type_name(card_types.get(String.valueOf(ccGprsPay.getCard_type())));
		}

		p.setOther(iCcGprsPayMapper.getPayListTotal(org_id, pay_sn, card_iccid, card_id, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(",")));
		page.setRecords(records);
		p.setPage(page);

		return p;
	}
	
	@Override
	public void getPayListPageExport(Integer org_id, String pay_sn,
			String card_iccid, Integer card_id, Integer card_type, String transfer_id, Double gprs_amount,
			String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String paid_start,
			String paid_end, LoginInfo info) throws IOException {
		
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		
		if (StringUtils.isEmpty(orgpos)) {
			log.error("[getPayListPageExport][导出数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出数据失败");
		}

		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			log.error("[getPayListPageExport][导出数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出数据失败");
		}
		
		if(StringUtils.isNotEmpty(pay_from)) {
			pay_from = iCcNotifyService.getNtfType(pay_from);
		}

		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		if (StringUtils.isNotEmpty(paid_start)) {
			paid_start = paid_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(paid_end)) {
			paid_end = paid_end + " 23:59:59";
		}

		List<CcGprsPayExportBean> records = iCcGprsPayMapper.getPayListPageExport(org_id, pay_sn, card_iccid, card_id, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(","));

		if (!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String>  arr_pay_method = this.getArr_pay_method();
			Map<String, String> card_types = iCcGprsCardService.getCard_type();
			Map<Short, String> arr_pay_status = fcProperties.getArr_pay_status();
			Date time_expire = null;
			for (CcGprsPayExportBean ccGprsPay : records) {
				try {
					time_expire = StringUtils.isEmpty(ccGprsPay.getTime_paid()) ? null : DateUtils.addMonths(DateUtils.parseDate(ccGprsPay.getTime_paid(), "yyyy-MM-dd HH:mm:ss"), ccGprsPay.getLive_month().intValue());
				} catch (ParseException e) {
				}
				ccGprsPay.setTime_expire(time_expire == null ? "" : DateFormatUtils.format(time_expire, "yyyy-MM-dd HH:mm:ss"));
				ccGprsPay.setOrg_name(orgs.get(String.valueOf(ccGprsPay.getOrg_id())).getName());
				ccGprsPay.setPay_method_name(arr_pay_method.get(String.valueOf(ccGprsPay.getPay_method())));
				ccGprsPay.setCard_type_name(card_types.get(String.valueOf(ccGprsPay.getCard_type())));
				ccGprsPay.setIs_paid_name(arr_pay_status.get(ccGprsPay.getIs_paid()));
			}

		}
		HttpServletResponse response = WebRequestUtil.response();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		String name = "充值明细-" + DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd_HH-mm-ss");
		String fileName = new String(name.getBytes(), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
		Sheet sheet1 = new Sheet(1, 0, CcGprsPayExportBean.class);
		sheet1.setSheetName(name);
		writer.write(records, sheet1);
		writer.finish();
		out.flush();
		
	}

	@Override
	public PageData<PayPackResultBean, PayPackResultBean> getPayPackPage(PageForm pageForm, Short pay_method,
			Integer org_id, String date_start, String date_end, LoginInfo info) {
		
		// 组装分页参数
		Page<PayPackResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
		   page.setAsc("gprs_price");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<PayPackResultBean, PayPackResultBean> p = new PageData<>();
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
		
		List<PayPackResultBean> records = iCcGprsPayMapper.getPayPackPage(page, org_id, pay_method, date_start, date_end, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		p.setOther(iCcGprsPayMapper.getPayPackTotal(org_id, pay_method, date_start, date_end, orgpos, orgpos.split(",")));
		page.setRecords(records);
		p.setPage(page);

		return p;
	}

	@Override
	public PageData<MonthPayReportResultBean, OrgPayReportResultBean> getMonthCountPage(PageForm pageForm,
			Integer org_id, String mdate, LoginInfo info) {
		
		// 组装分页参数
		Page<MonthPayReportResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
		   page.setAsc("gprs_price");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<MonthPayReportResultBean, OrgPayReportResultBean> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}
		
		List<MonthPayReportResultBean> records = iCcGprsPayMapper.getMonthCountPage(page, org_id, mdate, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}
		
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (MonthPayReportResultBean monthPay : records) {
			monthPay.setOrg_name(orgs.get(String.valueOf(monthPay.getOrg_id())).getName());
		}

		p.setOther(iCcGprsPayMapper.getOrgPayReportTotal(org_id, null, null, null, mdate, orgpos, orgpos.split(",")));
		page.setRecords(records);
		p.setPage(page);

		return p;
	}

	@Override
	public Map<String, List<PayListChartDataResultBean>> getPaylogChart(Integer org_id,
			String pay_sn, String card_iccid, Integer card_id, Integer card_type, String transfer_id,
			Double gprs_amount, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end,
			String paid_start, String paid_end, LoginInfo info) {
		
		Map<String, List<PayListChartDataResultBean>> data = new HashMap<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return data;
		}
		
		if(StringUtils.isNotEmpty(pay_from)) {
			pay_from = iCcNotifyService.getNtfType(pay_from);
		}

		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		if (StringUtils.isNotEmpty(paid_start)) {
			paid_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(paid_end)) {
			paid_end = date_end + " 23:59:59";
		}

		
		
		List<PayListChartDataResultBean> paidChart = iCcGprsPayMapper.getPaidChart(org_id, pay_sn, card_iccid, card_id, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(","));
		data.put("paidChart", paidChart);
		Map<String, String>  arr_pay_method = this.getArr_pay_method();
		
		List<PayListChartDataResultBean> methodChart = iCcGprsPayMapper.getMethodChart(org_id, pay_sn, card_iccid, card_id, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(","));
		
		if (!CollectionUtils.isEmpty(methodChart)) {
			for (PayListChartDataResultBean payListChartDataResultBean : methodChart) {
				payListChartDataResultBean.setName(arr_pay_method.get(payListChartDataResultBean.getName()));
			}
		}
		data.put("methodChart", methodChart);
		
		return data;
	}
	
	public List<String> getAllMonth(LoginInfo info){
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		
		//TODO 是否需要讲缓存key 拼装完整
		String cacheKey = FcConstant.memSqlKey(String.format(GET_ALL_MONTH_SQL, orgpos), FcConstant.DB_GET_ALL);
		String cache = jedisPoolUtil.get(cacheKey);
		
		List<String> returnData = null;
		
		if(StringUtils.isEmpty(cache)) {
			
			returnData = iCcGprsPayMapper.getAllMonth(orgpos, orgpos.split(","));
			if(!CollectionUtils.isEmpty(returnData)) {
				
				jedisPoolUtil.setEx(cacheKey, JSONObject.toJSONString(returnData));
			}
		}else {
			returnData = JSONObject.parseArray(cache, String.class);
		}
		
		return returnData;
	}
	
	@Override
	public List<SelectBean> monthSelect(LoginInfo info){
		
		List<String> months = this.getAllMonth(info);
		if(CollectionUtils.isEmpty(months)) {
			return null;
		}
		List<SelectBean> data = new ArrayList<>(months.size());
		for (String string : months) {
			data.add(new SelectBean(string,string));
		}
		
		return data;
		
	}
	
	
}
