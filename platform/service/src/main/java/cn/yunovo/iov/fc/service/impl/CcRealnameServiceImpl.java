package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.dao.ICcRealnameMapper;
import cn.yunovo.iov.fc.model.FcProperties;
import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.model.export.CcRealnameExportBean;
import cn.yunovo.iov.fc.model.form.RealnameForm;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcRealnameService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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
@Slf4j
public class CcRealnameServiceImpl extends ServiceImpl<ICcRealnameMapper, CcRealname> implements ICcRealnameService {

	@Autowired
	private ICcRealnameMapper iCcRealnameMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsBatchService iCcGprsBatchService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Autowired
	private ICcGprsPayService iCcGprsPayService;
	
	@Autowired
	private ICcGprsAllotService iCcGprsAllotService;
	
	@Autowired
	private ICcCardLogService iCcCardLogService;
	
	@Autowired
	@Qualifier("clwTransactionManager")
	private DataSourceTransactionManager clwTransactionManager;
	
	@Autowired
	private FcProperties fcProperties;
	
	@Override
	public PageData<CcRealname, Object> getItemsPage(PageForm form, Integer org_id, String card_iccid,
			String date_start, String date_end, Integer status, LoginInfo info) {
		

		Page<CcRealname> page = form.build(CcRealname.class, null, null);
		page.setAsc("R.cdi_status","R.time_added");
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
			Map<String, String> userMap = iCcUserService.userMap();
			CcOrg tt = null;
			
			
			for (CcRealname ccRealname : records) {
				tt = orgs.get(String.valueOf(ccRealname.getOrg_id()));
				ccRealname.setOrg_name(tt == null ? "" : tt.getName());
				ccRealname.setUpdate_by(StringUtils.defaultIfEmpty(userMap.get(ccRealname.getUpdate_by()), ccRealname.getUpdate_by()));
				ccRealname.setCdi_img1(fcProperties.getFile_server_url()+ccRealname.getCdi_img1());
				ccRealname.setCdi_img2(fcProperties.getFile_server_url()+ccRealname.getCdi_img2());
				ccRealname.setCdi_img3(fcProperties.getFile_server_url()+ccRealname.getCdi_img3());
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	@Override
	public void getItemsPageExport(Integer org_id, String card_iccid,
			String date_start, String date_end, Integer status, LoginInfo info)
					throws IOException {
		

		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			
			log.error("[getItemsPageExport][导出数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出数据失败");
		}

		if(org_id != null && !iCcOrgService.hasPermission(org_id, orgpos)) {
			
			log.error("[getItemsPageExport][导出对应机构数据失败]params={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()));
			throw new BusinessException(-1, "导出对应机构数据失败");
		}
		
		if (StringUtils.isNotEmpty(date_start)) {
			date_start = date_start + " 00:00:00";
		}

		if (StringUtils.isNotEmpty(date_end)) {
			date_end = date_end + " 23:59:59";
		}
		
		List<CcRealnameExportBean> records = iCcRealnameMapper.getItemsPageExport( org_id, card_iccid, date_start, date_end, status, orgpos, orgpos.split(","));
		
		HttpServletResponse response = WebRequestUtil.response();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		String name = "流量卡实名信息" + DateFormatUtils.format(DateUtil.now(), "yyyy-MM-dd_HH-mm-ss");
		String fileName = new String(name.getBytes(), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
		
		Sheet sheet1 = new Sheet(1, 0, CcRealnameExportBean.class);
		sheet1.setSheetName(name);
		writer.write(records, sheet1);
		writer.finish();

		out.flush();
	}
	
	/**
	 * 实名审核
	 * @param card_id
	 * @param status 审批状态, 2 审批通过
	 * @param loginInfo
	 * @return
	 */
	@Override
	public boolean audit(RealnameForm form, LoginInfo loginInfo) {
		
		boolean ret = false;
		Short status = form.getStatus();
		Integer card_id = form.getCard_id();
		//1、获取对应流量卡的实名申请信息,如果如找到则直接返回错误信息
		CcRealname data = iCcRealnameMapper.getByCardId(card_id);
		if(data == null) {
			log.warn("[audit][请选择您要审批的实名申请信息]params={form:{},user:{}}", form.buildJsonString(), loginInfo.buildJsonString());
			throw new BusinessException(-1, "请选择您要审批的实名申请信息！");
		}
		
		CcRealname opData = new CcRealname();
		//BeanUtils.copyProperties(data, opData);
		opData.setCdi_status(status);
		opData.setTime_audit(DateUtil.nowStr());
		opData.setUpdate_by(loginInfo.getLoginName());
		if(status == 2) {//如果审批通过则执行如下流程
			
			//2.1、获取流量卡信息,如果未找到流量卡信息则返回错误信息
			CcGprsCard card = iCcGprsCardService.getByIccid(data.getCard_iccid());
			if(card == null) {
				log.warn("[audit][实名审批失败,未找到对应的流量卡信息！]params={form:{},user:{}}", form.buildJsonString(), loginInfo.buildJsonString());
				throw new BusinessException(-1, "实名审批失败,未找到对应的流量卡信息！");
			}
			
			//2.2、移动实名认证照片到成功区域
			try {
				opData.setCdi_img1(this._moveFile(data.getCdi_img1()));
				opData.setCdi_img2(this._moveFile(data.getCdi_img2()));
				opData.setCdi_img3(this._moveFile(data.getCdi_img3()));
			} catch (IOException e) {
				
				log.error("[audit][exception]params:{},errmsg={}", JSONObject.toJSONString(data), ExceptionUtils.getStackTrace(e));
				throw new BusinessException(-1, "实名审批失败,处理实名证件信息失败！");
			}
			
			//2.3、判断是否已赠送过流量套餐, 获取流量卡实名认证成功后可获得多少赠送流量
			CcGprsBatch batchInfo = iCcGprsBatchService.getGiveInfoByBatchId(card.getBatch_id());
			Double give_value = batchInfo.getGive_value() == null ? 0 : batchInfo.getGive_value();
			Float live_month = batchInfo.getGive_live_month() == null ? 0 : batchInfo.getGive_live_month();
			
			if(StringUtils.isEmpty(data.getGive_time()) && give_value > 0 && live_month > 0) {
				
				
				/*
				 * 循环判断当前流量接口是否在计算修改中，如果有缓存程序则暂停1秒执行
				 */
				Integer is_update = 0;
				String cardLockCacheKey = FcConstant.memResKey("CARD-LOCK-"+card.getCard_iccid());
				while(StringUtils.equals(jedisPoolUtil.get(cardLockCacheKey),"1")) {
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.error("[audit][exception]params={},exception={}", JSONObject.toJSONString(data), ExceptionUtils.getStackTrace(e));
						throw new BusinessException("实名审核失败");
					}
					is_update = 1;
				}
				
				if(is_update == 1) {
					card = iCcGprsCardService.getByIccid(data.getCard_iccid());
				}
				
				//
				//事务定义
				DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
				definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				TransactionStatus transactionStatus = clwTransactionManager.getTransaction(definition);
				CcGprsPay pay = null;
				boolean flag = false;
				try {
					
					/**
					 * 增加流水支付记录,
					 */
					Date now = new Date();
					String now_format = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
					Date time_expire = liveMonthConverToExpireTime(live_month, now); //赠送流量到期时间
					
					pay = new CcGprsPay();
					pay.setOrg_id(card.getOrg_id());
					pay.setCard_id(card.getCard_id());
					pay.setGprs_amount(give_value);
					pay.setGprs_price(new BigDecimal("0"));
					pay.setLive_month(live_month);
					pay.setPay_sn(makePaySn());
					pay.setPay_memo("实名成功赠送");
					pay.setPay_method((short) 0);
					pay.setTransfer_id("realname-give");
					pay.setIs_paid((short)1);
					pay.setTime_paid(now_format);
					pay.setTime_added(now_format);
					pay.setTime_expire(DateFormatUtils.format(time_expire, "yyyy-MM-dd HH:mm:ss"));
					
					flag = iCcGprsPayService.save(pay);
					if(!flag) {
						log.error("[audit][exception]params={data:{},pay:{}},errmsg={}", JSONObject.toJSONString(data), JSONObject.toJSONString(pay), "新增订单失败");
						throw new BusinessException(-1, "实名审核失败！");
					}
					
					CcGprsAllot gprsAllot = new CcGprsAllot();
					gprsAllot.setCard_id(card.getCard_id());
					gprsAllot.setGprs_amount(give_value);
					gprsAllot.setAllot_month(1);
					gprsAllot.setAllot_value(give_value);
					gprsAllot.setAllot_reset((short) 0);
					gprsAllot.setAssigned_month(0);
					gprsAllot.setTime_added(now_format);
					gprsAllot.setTime_expire(pay.getTime_expire());
					if(!iCcGprsAllotService.save(gprsAllot)) {
						log.error("[audit][exception]params={data:{},gprsAllot:{}},errmsg={}", JSONObject.toJSONString(data), JSONObject.toJSONString(pay), "新增订单失败");
						throw new BusinessException(-1, "实名审核失败！");
					}
					
					clwTransactionManager.commit(transactionStatus);
					
					//time_expire = (live_month < 1) ? ("+"+()):();
					//$time_expire = ($live_month < 1) ? ('+' . (round($live_month, 2) * 100) . ' day') : ($live_month == 999 ? '2038-01-01 01:01:01' : "+{$live_month} month");
				}catch(BusinessException e) {
					log.error("[audit][exception]params={},exception={}", JSONObject.toJSONString(data), ExceptionUtils.getStackTrace(e));
					clwTransactionManager.rollback(transactionStatus);
					throw e;
				}catch (Exception e) {
					log.error("[audit][exception]params={},exception={}", JSONObject.toJSONString(data), ExceptionUtils.getStackTrace(e));
					clwTransactionManager.rollback(transactionStatus);
					throw new BusinessException(-1, "实名审核异常！");
				}
				
				try {
					iCcGprsAllotService.gprsAllot(card_id);
				} catch (Exception e) {
					log.error("[audit][exception]params={card_id:{}},exception={}", card_id, ExceptionUtils.getStackTrace(e));
					throw new BusinessException(-1, "实名赠送流量分配异常！");
				}
				
				//充值成功通知队列
				JSONObject pay_queue = new JSONObject();
				pay_queue.put("payid", pay.getPay_id());
				pay_queue.put("iccid", card.getCard_iccid());
				jedisPoolUtil.lpush(FcConstant.PAY_QUEUE_CACHEKEY, pay_queue.toJSONString());
				
				opData.setGive_value(BigDecimal.valueOf(give_value));
				opData.setGive_time(DateUtil.nowStr());
				
				card.setOwner_real(1);
				
				if(StringUtils.isEmpty(card.getTime_active())) {
					
					iCcGprsCardService.updateCard(card);
				}else {
					GprsCalculateBean gprs = new GprsCalculateBean();
					gprs.setMonth(card.getUsed_month() + 0.001);
					gprs.setTotal(card.getUsed_total() + 0.001);
					gprs.setIs_unicom(false);
					gprs.setOpen_card(true);
					iCcGprsAllotService.gprsCalculate(card, gprs);
				}
				
				data.setTime_audit(DateUtil.nowStr());
				iCcCardLogService.log10Rlname(data, false);
				
			}
			
		}
		
		UpdateWrapper<CcRealname> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("card_id", form.getCard_id());
		ret = this.update(opData, updateWrapper);
		this.getByIccid(data.getCard_iccid(), true);
		data = iCcRealnameMapper.getByCardIccid(data.getCard_iccid());
		cache(data);
		return ret;
	}
	
	public CcRealname getByIccid(String iccid, boolean noCache) {
		
		String sql = FcConstant.memSqlKey(String.format(CACHE_SQL_KEY, iccid), FcConstant.DB_GET_ROW);
		String cache = null;
		if(!noCache) {
			cache = jedisPoolUtil.get(sql);
		}
		CcRealname returnData = null;
		if(StringUtils.isEmpty(cache)) {
			
			QueryWrapper<CcRealname> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("card_iccid", iccid);
			returnData = this.getBaseMapper().selectOne(queryWrapper);
			if(returnData != null) {
				jedisPoolUtil.setEx(sql, JSONObject.toJSONString(returnData, SerializerFeature.WriteMapNullValue));
			}
		}else {
			returnData = JSONObject.parseObject(cache, CcRealname.class);
		}
		
		return returnData;
	}
	
	public String _moveFile(String source) throws IOException {
		
		String month = DateFormatUtils.format(new Date(), "yyyyMM");
		String filenew = source.replace("real/", String.format("realname/%s/", month));
		
		File srcFile = new File(fcProperties.getFile_dir_root() +"/www/img/"+source);
		File destFile = new File(fcProperties.getFile_dir_root() +"/www/img/"+filenew);
		File parentFile = destFile.getParentFile();
		if(!parentFile.exists()) {
			parentFile.mkdirs();
		}
		
		FileUtils.moveFile(srcFile, destFile);
		return filenew;
	}
	
	/**
	 * liveMonth to 实际日期
	 * @param liveMonth 有效周期，参考arr_live_month
	 * @param now
	 * @return
	 */
	public static Date liveMonthConverToExpireTime(float liveMonth, Date now) {
		
		int liveMonth_1 = 0;
		if(liveMonth < 1) { //例如 0.01 
			liveMonth_1 = (int)(liveMonth * 100);
			return DateUtils.addDays(now, liveMonth_1);
		}
		
		if(liveMonth == 999) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.set(2038, 0, 1, 1, 1, 1);
			return rightNow.getTime();
		}
		
		liveMonth_1 = (int) liveMonth;
		
		return DateUtils.addMonths(now, liveMonth_1);
	}
	
	public static String makePaySn() {
		
		return DateFormatUtils.format(new Date(), "yyyyMMdd-HHmmss-")+RandomUtils.nextInt(100000, 999999);
	}

	@Override
	public boolean unbind(RealnameForm form, LoginInfo user) {
		
		boolean isOk = false;
		//1、获取对应流量卡的实名申请信息,如果如找到则直接返回错误信息
		CcRealname data = iCcRealnameMapper.getByCardId(form.getCard_id());
		if(data == null) {
			log.warn("[unbind][请选择您要解除的实名信息]params={form:{},user:{}}", form.buildJsonString(), user.buildJsonString());
			throw new BusinessException(-1, "请选择您要解除的实名信息！");
		}
		
		//2.1、获取流量卡信息,如果未找到流量卡信息则返回错误信息
		CcGprsCard card = iCcGprsCardService.getByIccid(data.getCard_iccid());
		if(card == null) {
			log.warn("[unbind][实名审批失败,未找到对应的流量卡信息！]params={form:{},user:{}}", form.buildJsonString(), user.buildJsonString());
			throw new BusinessException(-1, "实名解除失败,未找到对应的流量卡信息！");
		}
		
		card.setOwner_real(0);
		iCcGprsCardService.updateCard(card);
		
		data.setTime_audit(DateUtil.nowStr());
		iCcCardLogService.log10Rlname(data, true);
		isOk = SqlHelper.retBool(iCcRealnameMapper.updateIccidByCardid(form.getCard_id(), null, user.getLoginName()));
		
		data = iCcRealnameMapper.getByCardIccid(card.getCard_iccid());
		cache(data);
		return isOk;
	}
	
	private final String CACHE_SQL_KEY = "SELECT * FROM cc_realname WHERE card_iccid = '%s'";
	public void cache(CcRealname data) {
		
		String cacheKey = String.format(CACHE_SQL_KEY, data.getCard_iccid());
		cacheKey = FcConstant.memSqlKey(cacheKey, FcConstant.DB_GET_ROW);
		String content = "[]";
		if(data != null) {
			content = data.cacheJsonString();
		}
		jedisPoolUtil.setEx(cacheKey, content);
	}
	
	

}
