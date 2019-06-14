package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcRealnameMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcRealnameService;
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
 * 流量卡实名制表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class ICcRealnameServiceImpl extends ServiceImpl<ICcRealnameMapper, CcRealname> implements ICcRealnameService {

	@Autowired
	private ICcRealnameMapper iCcRealnameMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Override
	public PageData<CcRealname, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String date_start, String date_end, Integer status, LoginInfo info) {
		

		Page<CcRealname> page = form.build(CcRealname.class, null, null);
		page.setDesc("R.cdi_status","R.time_added");
		PageData<CcRealname, Object> returnData = new PageData<>();
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
		
		List<CcRealname> records = iCcRealnameMapper.getItemsPage(page, org_id, card_iccid, date_start, date_end, status, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcRealname ccRealname : records) {

				ccRealname.setOrg_name(orgs.get(String.valueOf(ccRealname.getOrg_id())).getName());
				ccRealname.setFirst_name(userMap.get(ccRealname.getUser_id()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

}
