package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.dao.ICcStatsMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsService;
import cn.yunovo.iov.fc.service.ICcUserService;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
public class CcStatsServiceImpl extends ServiceImpl<ICcStatsMapper, CcStats> implements ICcStatsService {

	@Autowired
	private ICcUserService iCcUserService;

	@Autowired
	private ICcStatsMapper iCcStatsMapper;

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

}
