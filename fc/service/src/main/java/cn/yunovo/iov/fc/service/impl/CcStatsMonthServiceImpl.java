package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.dao.ICcStatsMonthMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcStatsMonth;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量卡月流量使用情况统计表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Service
public class CcStatsMonthServiceImpl extends ServiceImpl<ICcStatsMonthMapper, CcStatsMonth>
		implements ICcStatsMonthService {

	@Autowired
	private ICcOrgService iCcOrgService;

	@Autowired
	private ICcUserService iCcUserService;

	@Autowired
	private ICcGprsCardService iCcGprsCardService;

	@Autowired
	private ICcStatsMonthMapper iCcStatsMonthMapper;

	@Override
	public List<String> getMonths(LoginInfo info) {

		if (info == null) {
			return null;
		}

		CcUser ccUser = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());

		if (ccUser == null) {
			return null;
		}

		List<String> months = null;
		if (StringUtils.equals("*", ccUser.getOrgpos())) {
			months = iCcStatsMonthMapper.getAllMonths();
			return months;
		}

		String orgpos = iCcOrgService.getOrgpos(ccUser.getOrg_id(), ccUser.getOrgpos());
		if (StringUtils.isEmpty(orgpos)) {
			return null;
		}

		String[] orgs = orgpos.split(",");
		if (orgs == null || orgs.length < 1) {
			return null;
		}
		months = iCcStatsMonthMapper.getMonthsByOrgs(orgs);

		return months;
	}

	@Override
	public List<SelectBean> monthSelect(LoginInfo info) {

		List<String> months = this.getMonths(info);

		if (CollectionUtils.isEmpty(months)) {
			return null;
		}

		List<SelectBean> result = new ArrayList<>();
		SelectBean temp = null;
		for (String string : months) {

			temp = new SelectBean(string, string);
			result.add(temp);
		}

		return result;
	}

	@Override
	public PageData<CcStatsMonth, Map<String, Object>> getItemsPage(Integer org_id, Integer card_type,
			String card_iccid, String mdate, PageForm pageForm, LoginInfo info) {

		// 组装分页参数
		Page<CcStatsMonth> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("s.time_modify");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcStatsMonth, Map<String, Object>> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<CcStatsMonth> records = iCcStatsMonthMapper.queryItemsPage(page, org_id, card_type, card_iccid, mdate,
				orgpos, orgpos.split(","));

		// 获取总使用流量
		Long usedTotal = iCcStatsMonthMapper.usedTotal(org_id, card_type, card_iccid, mdate, orgpos, orgpos.split(","));

		if (usedTotal == null) {
			usedTotal = 0L;
		}

		Map<String, Object> other = new HashMap<>(1);
		other.put("usedTotal", usedTotal);

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		Map<String, String> cardTypes = iCcGprsCardService.getCard_type();
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (CcStatsMonth ccStatsMonth : records) {

			ccStatsMonth.setOrg_name(orgs.get(ccStatsMonth.getOrg_id()).getName());
			ccStatsMonth.setCard_type_name(cardTypes.get(ccStatsMonth.getCard_type()));
		}

		//Long count = iCcStatsMonthMapper.queryItemsPageCount(org_id, card_type, card_iccid, mdate, orgpos,
		//		orgpos.split(","));
		page.setRecords(records);
		//page.setTotal(count);
		p.setPage(page);
		p.setOther(other);

		return p;
	}

	@Override
	public void export(Integer org_id, Integer card_type, String card_iccid, String mdate, LoginInfo info)
			throws IOException {

		// 组装分页参数
		Page<CcStatsMonth> page = new Page<>();
		page.setCurrent(1);
		page.setSize(65536);
		page.setDesc("s.time_modify");

		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return;
		}

		List<CcStatsMonth> records = iCcStatsMonthMapper.queryItemsPage(page, org_id, card_type, card_iccid, mdate,
				orgpos, orgpos.split(","));

		if (!CollectionUtils.isEmpty(records)) {
			Map<String, String> cardTypes = iCcGprsCardService.getCard_type();
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			for (CcStatsMonth ccStatsMonth : records) {

				ccStatsMonth.setOrg_name(orgs.get(ccStatsMonth.getOrg_id()).getName());
				ccStatsMonth.setCard_type_name(cardTypes.get(ccStatsMonth.getCard_type()));
			}
		}
		HttpServletResponse response = WebRequestUtil.response();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		String fileName = new String(("月度用量" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, false);
		
		Sheet sheet1 = new Sheet(1, 0, CcStatsMonth.class);
		sheet1.setSheetName("第一个sheet");
		writer.write(records, sheet1);
		writer.finish();

		out.flush();

	}

}
