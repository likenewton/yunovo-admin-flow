package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcGprsPackMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPack;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcGprsPackService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量充值套餐表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Service
public class CcGprsPackServiceImpl extends ServiceImpl<ICcGprsPackMapper, CcGprsPack> implements ICcGprsPackService {

	@Autowired
	private ICcGprsPackMapper iCcGprsPackMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Override
	public List<SelectBean> select(LoginInfo info) {
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return null;
		}
		
		List<CcGprsPack> packs = iCcGprsPackMapper.getPack(orgpos, orgpos.split(","));
		
		if(CollectionUtils.isEmpty(packs)) {
			return null;
		}
		
		List<SelectBean> result = new ArrayList<>();
		for (CcGprsPack ccGprsPack : packs) {
			result.add(new SelectBean(ccGprsPack.getGprs_amount(),ccGprsPack.getGprs_amount()));
		}
		
		return result;
	}
	
	

	@Override
	public PageData<CcGprsPack, Object> getItemsPage(PageForm form, Integer org_id, LoginInfo info) {
		
		Page<CcGprsPack> page = form.build(CcGprsPack.class, null, "time_added");
		PageData<CcGprsPack, Object> returnData = new PageData<>();
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

		List<CcGprsPack> records = iCcGprsPackMapper.getItemsPage(page, org_id, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcGprsPack ccGprsPack : records) {

				ccGprsPack.setOrg_name(orgs.get(String.valueOf(ccGprsPack.getOrg_id())).getName());
				ccGprsPack.setFirst_name(userMap.get(ccGprsPack.getUser_id()));
				ccGprsPack.setAlter_name(userMap.get(ccGprsPack.getAlter_id()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

}
