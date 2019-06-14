package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcGprsGiftMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsGift;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcGprsGiftService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * GPRS流量赠送表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcGprsGiftServiceImpl extends ServiceImpl<ICcGprsGiftMapper, CcGprsGift> implements ICcGprsGiftService {

	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsGiftMapper iCcGprsGiftMapper;
	
	@Override
	public PageData<CcGprsGift, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String date_start, String date_end, LoginInfo info) {
		

		Page<CcGprsGift> page = form.build(CcGprsGift.class, null, "time_added");
		PageData<CcGprsGift, Object> returnData = new PageData<>();
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
		
		List<CcGprsGift> records = iCcGprsGiftMapper.getItemsPage(page, org_id, card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<Integer, String> userMap = iCcUserService.userIdMap();
			for (CcGprsGift ccGprsGift : records) {

				ccGprsGift.setOrg_name(orgs.get(String.valueOf(ccGprsGift.getOrg_id())).getName());
				ccGprsGift.setFirst_name(userMap.get(ccGprsGift.getUser_id()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

}
