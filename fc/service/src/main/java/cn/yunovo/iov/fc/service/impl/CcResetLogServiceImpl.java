package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcResetLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcResetLogService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 重置流量卡日志 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Service
public class CcResetLogServiceImpl extends ServiceImpl<ICcResetLogMapper, CcResetLog> implements ICcResetLogService {

	@Autowired
	private ICcResetLogMapper iCcResetLogMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Override
	public PageData<CcResetLog, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid, String date_start, String date_end, LoginInfo info) {
		
		Page<CcResetLog> page = form.build(CcResetLog.class, null, "time_added");
		PageData<CcResetLog, Object> returnData = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}

		if(org_id != null && iCcOrgService.hasPermission(org_id, orgpos)) {
			
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
		
		List<CcResetLog> records = iCcResetLogMapper.getItemsPage(page, org_id, card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> userMap = iCcUserService.userMap();
			for (CcResetLog ccResetLog : records) {

				ccResetLog.setOrg_name(orgs.get(String.valueOf(ccResetLog.getOrg_id())).getName());
				ccResetLog.setFirst_name(StringUtils.defaultIfEmpty(userMap.get(ccResetLog.getUser_name()),ccResetLog.getUser_name()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return null;
	}
	
}
