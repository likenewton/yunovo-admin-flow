package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsBatchMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcNationService;
import cn.yunovo.iov.fc.service.ICcOrgService;
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
 * 流量卡发货批次表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Service
public class CcGprsBatchServiceImpl extends ServiceImpl<ICcGprsBatchMapper, CcGprsBatch> implements ICcGprsBatchService {

	@Autowired
	private ICcUserService iCcUserService;
	
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
		
		List<CcGprsBatch> records = iCcGprsBatchMapper.getItemsPage(page, org_id, batch_sn, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcGprsBatch ccGprsBatch : records) {

				ccGprsBatch.setOrg_name(orgs.get(String.valueOf(ccGprsBatch.getOrg_id())).getName());
				ccGprsBatch.setFirst_name(StringUtils.defaultIfEmpty(userMap.get(ccGprsBatch.getUser_id()), String.valueOf(ccGprsBatch.getUser_id())));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		
		return returnData;
	}

}
