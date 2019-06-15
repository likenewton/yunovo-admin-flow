package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.SellPayResultBean;
import cn.yunovo.iov.fc.model.result.CardDetailInfoBean;
import cn.yunovo.iov.fc.model.result.CardTotalByOrgidInfoBean;
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
import cn.yunovo.iov.fc.model.result.UnicomStatResultBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConfigurationProperties(prefix = "fc.gprs")
@Slf4j
public class CcGprsCardServiceImpl extends ServiceImpl<ICcGprsCardMapper, CcGprsCard> implements ICcGprsCardService {

	@Autowired
	private ICcGprsCardMapper iGprsCardMapper;

	@Autowired
	private ICcUserService iCcUserService;

	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcStatsMonthService iCcStatsMonthService;

	private Map<String, String> array_card_type;

	public Map<String, String> getCard_type() {
		return array_card_type;
	}

	public void setCard_type(Map<String, String> card_type) {
		this.array_card_type = card_type;
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

		List<CcGprsCard> records = iGprsCardMapper.getHaltPage(page, card_iccid, card_type, org_id, time_expire, orgpos,
				orgpos.split(","));

		// 获取总使用流量
		HashMap<String, Double> total = iGprsCardMapper.getHaltTotal(card_iccid, card_type, org_id, time_expire, orgpos,
				orgpos.split(","));

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
		page.setRecords(records);
		p.setPage(page);
		p.setOther(total);

		return p;
	}

	@Override
	public PageData<CcGprsCard, Object> getItemsPage(PageForm pageForm, String card_iccid, Integer card_type,
			Integer org_id, Integer max_unused, Integer unicom_diff, LoginInfo info) {

		// 组装分页参数
		Page<CcGprsCard> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setAsc("card_id");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CcGprsCard, Object> p = new PageData<>();
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		List<CcGprsCard> records = iGprsCardMapper.getItemsPage(page, card_iccid, card_type, org_id, max_unused,
				unicom_diff, orgpos, orgpos.split(","));

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

		//Long count = iGprsCardMapper.getItemsPageCount(card_iccid, card_type, org_id, max_unused, unicom_diff, orgpos,
		//		orgpos.split(","));
		page.setRecords(records);
		//page.setTotal(count);
		p.setPage(page);

		return p;
	}

	@Override
	public PageData<SellPayResultBean, Object> getSellPayPage(PageForm pageForm, Integer org_id, String date_start,
			String date_end, LoginInfo info) {

		// 组装分页参数
		Page<SellPayResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<SellPayResultBean, Object> p = new PageData<>();
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

		List<SellPayResultBean> records = iGprsCardMapper.getSellPayPage(page, org_id, date_start, date_end, orgpos,
				orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (SellPayResultBean sellPayResultBean : records) {
			sellPayResultBean.setOrg_name(orgs.get(String.valueOf(sellPayResultBean.getOrg_id())).getName());
		}

		//Long count = iGprsCardMapper.getSellPayPageCount(org_id, date_start, date_end, orgpos, orgpos.split(","));
		page.setRecords(records);
		//page.setTotal(count);
		p.setPage(page);

		return p;

	}

	@Override
	public PageData<CardUsedResultBean, CardUsedResultBean> getCardUsedPage(PageForm pageForm, Integer org_id,
			String date_start, String date_end, LoginInfo info) {

		// 组装分页参数
		Page<CardUsedResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<CardUsedResultBean, CardUsedResultBean> p = new PageData<>();
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

		List<CardUsedResultBean> records = iGprsCardMapper.getCardUsedPage(page, org_id, date_start, date_end, orgpos,
				orgpos.split(","));

		System.out.println(JSONObject.toJSONString(page));
		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		CardUsedResultBean total = iGprsCardMapper.getCardUsedTotal(org_id, date_start, date_end, orgpos,orgpos.split(","));
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (CardUsedResultBean sellPayResultBean : records) {
			sellPayResultBean.setOrg_name(orgs.get(String.valueOf(sellPayResultBean.getOrg_id())).getName());
		}

		//Long count = iGprsCardMapper.getCardUsedPageCount(org_id, date_start, date_end, orgpos, orgpos.split(","));
		page.setRecords(records);
		//page.setTotal(count);
		p.setPage(page);
		p.setOther(total);

		return p;

	}

	@Override
	public PageData<UnicomStatResultBean, UnicomStatResultBean> getUnicomStatPage(PageForm pageForm, Integer org_id,
			String date_start, String date_end, String jstart, String jend, LoginInfo info) {
		
		// 组装分页参数
		Page<UnicomStatResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<UnicomStatResultBean, UnicomStatResultBean> p = new PageData<>();
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

		List<UnicomStatResultBean> records = iGprsCardMapper.getUnicomStatPage(page, org_id, date_start, date_end,
				jstart, jend, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			p.setOther(null);
			return p;
		}

		UnicomStatResultBean total = iGprsCardMapper.getUnicomStatTotal(org_id, date_start, date_end, jstart, jend,
				orgpos, orgpos.split(","));
		Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
		for (UnicomStatResultBean sellPayResultBean : records) {
			sellPayResultBean.setOrg_name(orgs.get(String.valueOf(sellPayResultBean.getOrg_id())).getName());
		}

		/*Long count = iGprsCardMapper.getUnicomStatPageCount(org_id, date_start, date_end, jstart, jend, orgpos,
				orgpos.split(","));*/
		page.setRecords(records);
		//page.setTotal(count);
		p.setPage(page);
		p.setOther(total);

		return p;
	}

	@Override
	public PageData<PayDetailResultBean, PayDetailResultBean> getPayDetailPage(PageForm pageForm, Integer org_id,
			String date_start, String date_end, LoginInfo info) {
		// 组装分页参数
		Page<PayDetailResultBean> page = new Page<>();
		page.setCurrent(pageForm.getCurrent());
		page.setSize(pageForm.getSize());

		if (ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			// page.setAsc(");
		} else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		PageData<PayDetailResultBean, PayDetailResultBean> p = new PageData<>();
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

		List<PayDetailResultBean> records = iGprsCardMapper.getPayDetailPage(page, org_id, date_start, date_end, orgpos, orgpos.split(","));

		if (CollectionUtils.isEmpty(records)) {
			page.setTotal(0);
			page.setRecords(null);
			p.setPage(page);
			return p;
		}

		page.setRecords(records);
		p.setPage(page);

		return p;
	}
	
	@Override
	public PageData<CcGprsCard, Object> queryCardListPage(PageForm form, String card_iccid, Integer org_id, String date_start, String date_end, Integer time_expire, Integer unicom_stop, Integer status, LoginInfo info, Integer card_type) {
		
		Page<CcGprsCard> page = form.build(CcGprsCard.class, "card_iccid", null);
		
		PageData<CcGprsCard, Object> returnData = new PageData<>();
		
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
		
		List<CcGprsCard> records = iGprsCardMapper.queryCardListPage(page, card_iccid, card_type, org_id, date_start, date_end, time_expire, unicom_stop, status, orgpos, orgpos.split(","));
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, String> cardTypes = getCard_type();
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			for (CcGprsCard ccGprsCard : records) {
				ccGprsCard.setOrg_name(orgs.get(String.valueOf(ccGprsCard.getOrg_id())).getName());
				ccGprsCard.setCard_type_name(cardTypes.get(String.valueOf(ccGprsCard.getCard_type())));
			}
		}
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	@Override
	public CardDetailInfoBean detailByCardId(Integer card_id) {
		
		CcGprsCard card = this.getById(card_id);
		if(card == null) {
			return null;
		}
		
		CardDetailInfoBean detail = new CardDetailInfoBean();
		BeanUtils.copyProperties(card, detail);
		
		Double total = iCcStatsMonthService.getWlistTotalByCardId(card_id);
		detail.setWlistTotal(total);
		
		return detail;
	}

	@Override
	public PageData<CardTotalByOrgidInfoBean, Object> cardTotalByOrgidGroupPage(PageForm form, LoginInfo info) {

		Page<CardTotalByOrgidInfoBean> page = form.build(CardTotalByOrgidInfoBean.class, null, null);
		page.setAsc(null);
		page.setDesc(null);
		PageData<CardTotalByOrgidInfoBean, Object> returnData = new PageData<>();
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			page.setTotal(0);
			page.setRecords(null);
			returnData.setPage(page);
			return returnData;
		}
		
		List<CardTotalByOrgidInfoBean> records = iGprsCardMapper.cardTotalByOrgidGroup(page, orgpos, orgpos.split(","));
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			for (CardTotalByOrgidInfoBean ccGprsCard : records) {
				ccGprsCard.setOrg_name(orgs.get(String.valueOf(ccGprsCard.getOrg_id())).getName());
			}
		}
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}

}
