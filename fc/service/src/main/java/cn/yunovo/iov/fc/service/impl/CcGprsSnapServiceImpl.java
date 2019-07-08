package cn.yunovo.iov.fc.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.dao.ICcGprsSnapMapper;
import cn.yunovo.iov.fc.model.FcProperties;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcGprsSnap;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.export.CcGprsPayExportBean;
import cn.yunovo.iov.fc.model.export.CcGprsSnapExportBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsSnapService;
import cn.yunovo.iov.fc.service.ICcNotifyService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * GPRS流量快照表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-07-07
 */
@Service
@Slf4j
public class CcGprsSnapServiceImpl extends ServiceImpl<ICcGprsSnapMapper, CcGprsSnap> implements ICcGprsSnapService {

	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcNotifyService iCcNotifyService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcGprsSnapMapper iCcGprsSnapMapper;
	
	@Autowired
	private FcProperties fcProperties;
	
	@Override
	public void export(Integer org_id, 	String card_iccid, Integer card_type, 
			String pay_from, Short pay_method, String date_start, String date_end, String paid_start,
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

		List<CcGprsSnapExportBean> records = iCcGprsSnapMapper.getPaySnapExport(org_id, card_iccid, card_type, pay_from, pay_method, date_start, date_end, paid_start, paid_end, orgpos, orgpos.split(","));
				
		if (!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> card_types = iCcGprsCardService.getCard_type();
			Map<String, String> ntfType = iCcNotifyService.getArr_ntf_type();
			Map<Short, String> arr_allot_reset = fcProperties.getArr_allot_reset();
			for (CcGprsSnapExportBean ccGprsPay : records) {
				
				ccGprsPay.setOrg_name(orgs.get(String.valueOf(ccGprsPay.getOrg_id())).getName());
				ccGprsPay.setCard_type_name(card_types.get(String.valueOf(ccGprsPay.getCard_type())));
				ccGprsPay.setPay_from(ntfType.get(StringUtils.defaultIfEmpty(ccGprsPay.getPay_from(), "unknown")));
				ccGprsPay.setAllot_reset_cn(arr_allot_reset.get(ccGprsPay.getAllot_reset()));
			}

		}
		HttpServletResponse response = WebRequestUtil.response();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		String name = "快照明细-" + DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd_HH-mm-ss");
		String fileName = new String(name.getBytes(), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
		Sheet sheet1 = new Sheet(1, 0, CcGprsSnapExportBean.class);
		sheet1.setSheetName(name);
		writer.write(records, sheet1);
		writer.finish();
		out.flush();
		
	}
	
}
