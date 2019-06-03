package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcOnoffLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOnoffLog;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOnoffLogService;
import cn.yunovo.iov.fc.service.ICcOrgService;
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
 * 停卡日志 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-01
 */
@Service
public class CcOnoffLogServiceImpl extends ServiceImpl<ICcOnoffLogMapper, CcOnoffLog> implements ICcOnoffLogService {

	@Autowired
	private ICcOnoffLogMapper iCcOnoffLogMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Override
	public PageData<CcOnoffLog, Object> getItems(PageForm pageForm, String card_iccid, Integer card_type,
			Integer org_id, LoginInfo info) {
		
		// 组装分页参数
		Page<CcOnoffLog> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("SL.onoff_id");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcOnoffLog, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<CcOnoffLog> records = iCcOnoffLogMapper.getItemsPage(page, card_iccid, card_type, org_id, orgpos, orgpos.split(","));
		
		if(CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(records);
			p.setPage(page);
			
			return p;
		}
		Map<String, String> cardTypes = iCcGprsCardService.getCard_type();
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (CcOnoffLog ccOnoffLog : records) {
			ccOnoffLog.setOrg_name(orgs.get(String.valueOf(ccOnoffLog.getOrg_id())).getName());
			ccOnoffLog.setCard_type_name(cardTypes.get(String.valueOf(ccOnoffLog.getCard_type())));
		}
		
		Long count = iCcOnoffLogMapper.getItemsPageCount(card_iccid, card_type, org_id, orgpos, orgpos.split(","));
		
		page.setTotal(count);
		page.setRecords(records);
		p.setPage(page);
		
		return p;
		
	}
	
}
