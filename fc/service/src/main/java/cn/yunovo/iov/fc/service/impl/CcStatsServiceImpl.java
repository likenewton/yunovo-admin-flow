package cn.yunovo.iov.fc.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.dao.ICcStatsMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcNotify;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.model.export.CcStatsExportBean;
import cn.yunovo.iov.fc.model.export.HaltPageExportBean;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 各项参数统计表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-03
 */
@Service
@Slf4j
public class CcStatsServiceImpl extends ServiceImpl<ICcStatsMapper, CcStats> implements ICcStatsService {

	@Autowired
	private ICcUserService iCcUserService;

	@Autowired
	private ICcStatsMapper iCcStatsMapper;
	
	@Autowired
	private ICcOrgService iCcOrgService;

	@Override
	public PageData<CcStats, Object> getItemsPage(PageForm pageForm, Integer org_id, String date_start, String date_end,
			LoginInfo info) {

		// 组装分页参数
		Page<CcStats> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {

			page.setDesc("stats_date");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcStats, Object> p = new PageData<>();
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

		List<CcStats> records = iCcStatsMapper.getItemsPage(page, org_id, date_start, date_end, orgpos,
				orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		page.setRecords(records);
		// page.setTotal(count);
		p.setPage(page);

		return p;

	}
	
	@Override
	public void getItemsPageExport(Integer org_id, String date_start, String date_end,
			LoginInfo info) throws IOException {
		
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			log.error("[getItemsPageExport][导出数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出数据失败");
		}

		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			log.error("[getItemsPageExport][导出数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出数据失败");
		}
		
		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}
		
		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		List<CcStatsExportBean> records = iCcStatsMapper.getItemsPageExport(org_id, date_start, date_end, orgpos,
				orgpos.split(","));
		
		HttpServletResponse response = WebRequestUtil.response();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		String name = "流量卡运营统计-" + DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd_HH-mm-ss");
		String fileName = new String(name.getBytes(), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
		
		Sheet sheet1 = new Sheet(1, 0, CcStatsExportBean.class);
		sheet1.setSheetName(name);
		writer.write(records, sheet1);
		writer.finish();

		out.flush();
		
	}

	@Override
	public PageData<CcStats, Object> getItemsOrgPage(PageForm pageForm, Integer org_id, String stats_date,
			LoginInfo info) {
		// 组装分页参数
		Page<CcStats> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());
		
		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc();
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		
		PageData<CcStats, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}
		
		QueryWrapper<CcStats> queryWrapper = new QueryWrapper<>();
		
		if(org_id != null) {
			queryWrapper.eq("org_id", org_id);
		}
		
		queryWrapper.eq("stats_date", stats_date);
		
		iCcStatsMapper.selectPage(page, queryWrapper);
		List<CcStats> records = page.getRecords();
		
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			for (CcStats ccStats : records) {
				ccStats.setOrg_name(orgs.get(String.valueOf(ccStats.getOrg_id())).getName());
			}
		}
		/*page.setTotal(selectPage.getTotal());
		page.setRecords(selectPage.getRecords());*/
		p.setPage(page);
		return p;
		
	}

}
