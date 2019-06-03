package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcStatsMonth;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * GPRS流量卡信息表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
@Service
@ConfigurationProperties(prefix="fc.gprs")
public class CcGprsCardServiceImpl extends ServiceImpl<ICcGprsCardMapper, CcGprsCard> implements ICcGprsCardService {

	@Autowired
	private ICcGprsCardMapper iGprsCardMapper; 
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	private Map<String, String> card_type;
	
	public Map<String, String> getCard_type() {
		return card_type;
	}

	public void setCard_type(Map<String, String> card_type) {
		this.card_type = card_type;
	}

	@Override
	public PageData<CcGprsCard, HashMap<String, Double>> getHaltPage(PageForm pageForm, String card_iccid,
			Integer card_type, Integer org_id, Integer time_expire, LoginInfo info) {

		// 组装分页参数
		Page<CcGprsCard> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setDesc("time_expire");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcGprsCard, HashMap<String, Double>> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<CcGprsCard> records = iGprsCardMapper.getHaltPage(page, card_iccid, card_type, org_id, time_expire, orgpos, orgpos.split(","));
				

		// 获取总使用流量
		HashMap<String, Double> total = iGprsCardMapper.getHaltTotal(card_iccid, card_type, org_id, time_expire, orgpos, orgpos.split(","));
/*
		if (total == null) {
			usedTotal = 0L;
		}*/

		/*Map<String, Object> other = new HashMap<>(1);
		other.put("usedTotal", usedTotal);*/

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		Map<String, String> cardTypes = getCard_type();
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (CcGprsCard ccGprsCard : records) {
			ccGprsCard.setOrg_name(orgs.get(String.valueOf(ccGprsCard.getOrg_id())).getName());
			ccGprsCard.setCard_type_name(cardTypes.get(String.valueOf(ccGprsCard.getCard_type())));
		}

		Long count = iGprsCardMapper.getHaltPageCount(card_iccid, card_type, org_id, time_expire, orgpos, orgpos.split(","));
		page.setRecords(records);
		page.setTotal(count);
		p.setPage(page);
		p.setOther(total);

		return p;
	}



}
