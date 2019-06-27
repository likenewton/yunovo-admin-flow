package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.math.MathUtils;
import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.SellPayResultBean;
import cn.yunovo.iov.fc.model.form.CardOnoffForm;
import cn.yunovo.iov.fc.model.result.CardDetailInfoBean;
import cn.yunovo.iov.fc.model.result.CardTotalByOrgidInfoBean;
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
import cn.yunovo.iov.fc.model.result.UnicomDataBean;
import cn.yunovo.iov.fc.model.result.UnicomStatResultBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
	
	@Autowired
	private ICcGprsAllotService iCcGprsAllotService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;

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

		page.setRecords(records);
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
	
	/**
	 * 通过ICCID获取流量卡数据, 如果缓存里有流量卡数据，则优先使用缓存
	 * @param iccid 流量卡号
	 * @return 
	 */
	@Override
	public CcGprsCard getByIccid(String iccid) {
		
		//iccid 的长度必须 >= 19位
		if(StringUtils.isEmpty(iccid) || iccid.length() < 19) {
			return null;
		}
		String cachekey = FcConstant.cardInfoKey(iccid);
		CcGprsCard card = null;
		String card_info = jedisPoolUtil.get(cachekey);
		if(StringUtils.isEmpty(card_info)) {
			
			card = iGprsCardMapper.getByIccid(iccid);
			if(card == null) { //如果通过完整的iccid查询不到流量卡信息则查19位流量卡
				
				card = iGprsCardMapper.getByLikeIccid(iccid.substring(0, 19));
			}
			
			//如果从数据库中无法查询到流量卡信息，则说明改卡非我司流量卡
			if(card == null || StringUtils.isEmpty(card.getCard_iccid())) {
				card = new CcGprsCard();
				card.setNone(1);
				//为防止缓存穿透 或减少数据层访问,因此也会将该iccid缓存
				card_info = JSONObject.toJSONString(card);
				jedisPoolUtil.setEx(cachekey, card_info);
				return null;
			}else {
				
				card_info = JSONObject.toJSONString(card, SerializerFeature.WriteMapNullValue);
				jedisPoolUtil.setEx(cachekey, card_info);
				return card;
			}
		}else {
			card = JSONObject.parseObject(card_info, CcGprsCard.class);
			if(card == null || StringUtils.isEmpty(card.getCard_iccid())) {
				return null;
			}else {
				return card;
			}
		}
	}

	@Override
	public boolean updateCard(CcGprsCard card) {

		int isOk = jedisPoolUtil.setEx(FcConstant.cardInfoKey(card.getCard_iccid()), JSONObject.toJSONString(card, SerializerFeature.WriteMapNullValue), 300 * 60);
		
		if(isOk < 1) {
			return this.updateById(card);
		}
		
		/**
		 * 将流量卡ICCID加入到消息队列中，方便队列处理存储流量卡信息
		 */
		if(jedisPoolUtil.lpush(FcConstant.RES_QUEUE_CACHEKEY, card.getCard_iccid()) == 1) {
			return true;//队列存储成功返回true
		}else {
			return this.updateCard(card);
		}
	}
	
	@Override
	public String gprsFormat(Double gprs) {
		
		if(gprs == null) {
			return "";
		}
		
		if(gprs.toString().length() == 8 && (gprs / 10000000) > 6) {
			return "无限制";
		}
		if(gprs == 0.01) {
			return "畅享无限";
		}
		if(gprs == 0.02) {
			return "定向无限";
		}
		
		Integer gb = 1024;
		Integer tb = 1024 * 1024;
		
		Double _gprs = Math.abs(gprs);
		if(_gprs < gb) {
			return MathUtils.round(gprs, 3) + "M";
		}
		
		if(_gprs >= gb && _gprs < tb) {
			return MathUtils.round(gprs / gb, 3) + "G";
		}
		
		if(_gprs >= tb) {
			return MathUtils.round(gprs / tb, 3) + "T";
		}
		return gprs.toString();
	}

	@Override
	public UnicomDataBean syncUnicomData(String card_sn, LoginInfo loginBaseInfo) {

		if(StringUtils.isEmpty(card_sn)) {
			throw new BusinessException("系统提示：请选择您要同步的流量卡");
		}
		CcGprsCard card = iGprsCardMapper.getByCardSn(card_sn);
		if(card == null) {
			throw new BusinessException("系统提示：未找到对应的流量卡信息");
		}
		
		UnicomDataBean result = null;
		try {
			result = iCcGprsAllotService.syncUnicomData(card);
		} catch (Exception e) {
			log.error("[onoff][syncUnicomData]params={card_ns:{}},logininfo={},exception={}", card_sn, loginBaseInfo.buildJsonString(),ExceptionUtils.getStackTrace(e));
			throw new BusinessException(-1, "操作失败");
		}
		
		return result;
	}

	@Override
	public boolean onoff(CardOnoffForm form, LoginInfo loginBaseInfo) {

		CcGprsCard card = this.getByIccid(form.getCard_iccid());
		
		if(card == null) {
			throw new BusinessException("系统提示：未找到对应的流量卡信息");
		}
		
		boolean isSuccess = true;
		try {
			isSuccess = iCcGprsAllotService.cardOnoff(card, form.getStatus(), loginBaseInfo.getId(), loginBaseInfo.getLoginName());
		} catch (Exception e) {
			
			log.error("[onoff][exception]params={},logininfo={},exception={}", form.buildJsonString(), loginBaseInfo.buildJsonString(),ExceptionUtils.getStackTrace(e));
			return false;
		}
		
		if(isSuccess) {
			card.setUnicom_stop(form.getStatus());
			if(form.getStatus() == 1) {
				card.setTime_stop(DateUtil.nowStr());
			}
		}else {
			if(form.getStatus() == 0) {
				card.setUnicom_stop((short)0);
			}else {
				card.setUnicom_stop((short)1);
			}
		}
		
		return this.updateCard(card);
		
	}

}
