package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcResetLogMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.result.CardResetForm;
import cn.yunovo.iov.fc.model.result.CardRestBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcResetLogService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
@Slf4j
public class CcResetLogServiceImpl extends ServiceImpl<ICcResetLogMapper, CcResetLog> implements ICcResetLogService {

	@Autowired
	private ICcResetLogMapper iCcResetLogMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcGprsAllotService iCcGprsAllotService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	private static final Map<String, String> arr_ret = new HashMap<>();
	
	private static final StringBuffer INSERT_SQL = new StringBuffer();
	
	static {
		arr_ret.put("ok", "流量卡重置成功");
		arr_ret.put("notfound", "非本公司卡不可重置");
		arr_ret.put("unactivated", "暂未激活无需重置");
		arr_ret.put("havepay", "有充值套餐需先迁移后方可重置");
		arr_ret.put("errorpay", "将支付日志注销时失败");
		
		INSERT_SQL.append("INSERT INTO cc_reset_log SET ");
		INSERT_SQL.append("org_id      = %s,      ");
		INSERT_SQL.append("card_id     = %s,      ");
		INSERT_SQL.append("card_iccid  = '%s',    ");
		INSERT_SQL.append("card_used   = %s,      ");
		INSERT_SQL.append("card_unused = %s,      ");
		INSERT_SQL.append("card_status = %s,      ");
		INSERT_SQL.append("batch_id    = %s,      ");
		INSERT_SQL.append("batch_time  = '%s',    ");
		INSERT_SQL.append("owner_bind  = %s,      ");
		INSERT_SQL.append("owner_real  = %s,      ");
		INSERT_SQL.append("owner_wszl  = %s,      ");
		INSERT_SQL.append("user_id     = %s,      ");
		INSERT_SQL.append("user_name   = '%s',      ");
		INSERT_SQL.append("time_added  = '%s'    ");
	}
	
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
		
		List<CcResetLog> records = iCcResetLogMapper.getItemsPage(page, org_id, card_iccid, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> userMap = iCcUserService.userMap();
			for (CcResetLog ccResetLog : records) {

				ccResetLog.setOrg_name(orgs.get(String.valueOf(ccResetLog.getOrg_id())).getName());
				ccResetLog.setCreate_by(StringUtils.defaultIfEmpty(userMap.get(ccResetLog.getUser_name()),ccResetLog.getUser_name()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	@Override
	public List<CardRestBean> cardReset(CardResetForm form, LoginInfo info) {
		
		String iccidTemp = form.getIccids().trim().replaceAll("\r\n", "\n");
		List<String> iccidList = Arrays.asList(iccidTemp.split("\n"));
		Set<String> iccids = new LinkedHashSet<>(iccidList);
		
		List<CardRestBean> result = new ArrayList<>();
		CardRestBean temp = null;
		CcGprsCard card = null;
		CardRestBean exeResult = null;
		for (String iccid : iccids) {
			
			if(StringUtils.isEmpty(iccid)) {
				continue;
			}
			iccid = iccid.trim();
			try {
				card = iCcGprsCardService.getByIccid(iccid);
				if(card == null) {
					temp = new CardRestBean();
					temp.setIccid(iccid);
					temp.setRet("2");
					temp.setMsg("非本公司卡不可重置");
					result.add(temp);
					continue;
				}
				
				exeResult = iCcGprsAllotService.cardReset(card);
				temp = new CardRestBean();
				temp.setIccid(iccid);
				temp.setRet(StringUtils.equals("ok", exeResult.getRet()) ? "0":"1");
				temp.setMsg(arr_ret.get(exeResult.getRet()));
				result.add(temp);
				
			}catch (Exception e) {
				log.error("[cardReset][exception]params={iccid:{}},exception={}",iccid, ExceptionUtils.getStackTrace(e));
				temp = new CardRestBean();
				temp.setIccid(iccid);
				temp.setMsg("执行异常");
				temp.setRet("1");
				result.add(temp);
				continue;
			}
			
			if(StringUtils.equals("ok", exeResult.getRet())) {
				this.cardResetLog(card, 0, info.getLoginName());
//				this.cardResetLog(card, info.getId(), info.getLoginName());
			}
		}
		
		return result;
	}

	private boolean cardResetLog(CcGprsCard card, Integer id, String loginName) {

		CcResetLog ccResetLog = new CcResetLog();
		
		ccResetLog.setOrg_id(card.getOrg_id());
		ccResetLog.setCard_id(card.getCard_id());
		ccResetLog.setCard_iccid(card.getCard_iccid());
		ccResetLog.setCard_used(card.getUnicom_month());
		ccResetLog.setCard_unused(card.getUnicom_unused());
		ccResetLog.setCard_status(StringUtils.isEmpty(card.getTime_active()) ? (short)0 :(card.getUnicom_stop() == 1 ? (short)2 : (short)1));
		ccResetLog.setBatch_id(card.getBatch_id());
		ccResetLog.setBatch_time(card.getTime_added());
		ccResetLog.setOwner_bind(card.getOwner_bind());
		ccResetLog.setOwner_real(card.getOwner_real());
		ccResetLog.setOwner_wszl(card.getOwner_wszl());
		ccResetLog.setUser_id(id);
		ccResetLog.setUser_name(loginName);
		ccResetLog.setTime_added(DateUtil.nowStr());
		String sql = String.format(INSERT_SQL.toString(), ccResetLog.getOrg_id(),ccResetLog.getCard_id(),ccResetLog.getCard_iccid(),ccResetLog.getCard_used(),
				ccResetLog.getCard_unused(),ccResetLog.getCard_status(),ccResetLog.getBatch_id(),ccResetLog.getBatch_time(),ccResetLog.getOwner_bind(),
				ccResetLog.getOwner_real(),ccResetLog.getOwner_wszl(),ccResetLog.getUser_id(),ccResetLog.getUser_name(), ccResetLog.getTime_added());
		try {
			boolean isOk = jedisPoolUtil.lpush(FcConstant.SQL_QUEUE_CACHEKEY, sql) > 0 ? true : false;
			if(!isOk) {
				return this.save(ccResetLog);
			}
			
			return isOk;
		} catch (Exception e) {
			log.error("[cardResetLog][exception]params={card:{},userid:{},username:{}},exception={}",JSONObject.toJSONString(card),id,loginName,ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	
}
