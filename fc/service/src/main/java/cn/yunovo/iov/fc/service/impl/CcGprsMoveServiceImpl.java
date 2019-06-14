package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsMoveMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsMove;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcGprsMoveService;
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
 * GPRS流量迁移表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcGprsMoveServiceImpl extends ServiceImpl<ICcGprsMoveMapper, CcGprsMove> implements ICcGprsMoveService {

	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsMoveMapper iCcGprsMoveMapper;
	
	@Override
	public PageData<CcGprsMove, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String old_card_iccid, String date_start, String date_end, LoginInfo info) {
		
		Page<CcGprsMove> page = form.build(CcGprsMove.class, null, "time_added");
		PageData<CcGprsMove, Object> returnData = new PageData<>();
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
		
		List<CcGprsMove> records = iCcGprsMoveMapper.getItemsPage(page, org_id, card_iccid, old_card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcGprsMove ccGprsMove : records) {

				ccGprsMove.setOrg_name(orgs.get(String.valueOf(ccGprsMove.getNew_orgid())).getName());
				ccGprsMove.setOld_org_name(orgs.get(String.valueOf(ccGprsMove.getOld_orgid())).getName());
				ccGprsMove.setFirst_name(userMap.get(ccGprsMove.getUser_id()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
		
	}

}
