package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcGprsBatchMapper;
import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.CcGprsBatchForm;
import cn.yunovo.iov.fc.model.result.BatchSaveResultBean;
import cn.yunovo.iov.fc.model.result.GprsBatchBean;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcNationService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcUserService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 流量卡发货批次表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Service
@Slf4j
@ConfigurationProperties(prefix = "fc.gprs")
public class CcGprsBatchServiceImpl extends ServiceImpl<ICcGprsBatchMapper, CcGprsBatch> implements ICcGprsBatchService {

	@Autowired
	private ICcUserService iCcUserService;
	
	private Map<String, String> arr_live_month;
	
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcGprsBatchMapper iCcGprsBatchMapper;
	
	@Autowired
	private ICcNationService iCcNationService;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Autowired
	private ICcGprsCardMapper iCcGprsCardMapper;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcCardLogService iCcCardLogService;

	@Override
	public boolean saveBatch(Collection<CcGprsBatch> entityList, int batchSize) {
		return super.saveBatch(entityList, batchSize);
	}

	@Override
	public PageData<CcGprsBatch, Object> getItemsPage(PageForm form, Integer org_id, String batch_sn, String date_start,
			String date_end, LoginInfo info) {
		
		Page<CcGprsBatch> page = form.build(CcGprsBatch.class, null, "time_added");
		PageData<CcGprsBatch, Object> returnData = new PageData<>();
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
		
		List<CcGprsBatch> records = iCcGprsBatchMapper.getItemsPage(page, org_id, batch_sn, date_start, date_end, orgpos, orgpos.split(","));
		
		if(!CollectionUtils.isEmpty(records)) {
			
			Map<String, CcOrg> orgs = iCcOrgService.getTree(0, orgpos);
			Map<String, String> userMap = iCcUserService.userMap();
			JSONObject nation = iCcNationService.nationMap();
			CcOrg tt = null;
			for (CcGprsBatch ccGprsBatch : records) {
				tt = orgs.get(String.valueOf(ccGprsBatch.getOrg_id()));
				ccGprsBatch.setOrg_name(tt == null ? "" : tt.getName());
				ccGprsBatch.setProvince_name(ccGprsBatch.getProvince_id() == null ? "": StringUtils.defaultIfEmpty(nation.getString(ccGprsBatch.getProvince_id().toString()),ccGprsBatch.getProvince_id().toString()));
				ccGprsBatch.setDistrict_name(ccGprsBatch.getDistrict_id() == null ? "": StringUtils.defaultIfEmpty(nation.getString(ccGprsBatch.getDistrict_id().toString()),ccGprsBatch.getDistrict_id().toString()));
				ccGprsBatch.setCity_name(nation.getString(ccGprsBatch.getCity_id().toString()));
				ccGprsBatch.setCreate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsBatch.getCreate_by()), ccGprsBatch.getCreate_by()));
				ccGprsBatch.setUpdate_by(StringUtils.defaultIfEmpty(userMap.get(ccGprsBatch.getUpdate_by()), ccGprsBatch.getUpdate_by()));
			}
		}
		
		page.setRecords(records);
		returnData.setPage(page);
		
		return returnData;
	}

	public Map<String, String> getArr_live_month() {
		return arr_live_month;
	}

	public void setArr_live_month(Map<String, String> arr_live_month) {
		this.arr_live_month = arr_live_month;
	}

	@Override
	public List<SelectBean> select(){
		
		Map<String, String>  typeMap = this.getArr_live_month();
		if(typeMap == null || typeMap.isEmpty()) {
			return null;
		}
		
		List<SelectBean> select = new ArrayList<>();
		SelectBean bean = null;
		Set<Entry<String, String>> entrySet = typeMap.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		Entry<String, String> temp = null;
		while(it.hasNext()) {
			temp = it.next();
			bean = new SelectBean(temp.getValue(), temp.getKey());
			select.add(bean);
		}
		
		return select;
		
		
	}
	
	@Override
	public CcGprsBatch getGiveInfoByBatchId(Integer batch_id) {
		
		if(batch_id == null) {
			return null;
		}
		String sql = "SELECT give_value, give_live_month FROM cc_gprs_batch WHERE batch_id = "+batch_id;
		
		String cacheKey = FcConstant.memSqlNewKey(sql, FcConstant.DB_GET_ROW);
		String cache = jedisPoolUtil.get(cacheKey);
		CcGprsBatch data = null;
		if(StringUtils.isEmpty(cache)) {
			
			data = iCcGprsBatchMapper.getGiveInfoByBatchId(batch_id);
			if(data == null) {
				return null;
			}else {
				cache = JSONObject.toJSONString(data);
				jedisPoolUtil.setEx(cacheKey, cache);
			}
		}else {
			data = JSONObject.parseObject(cache, CcGprsBatch.class);
		}
		
		return data;
	}
	
	@Override
	public Double getGprsAmountByBatchId(Integer batch_id) {
		
		if(batch_id == null) {
			return null;
		}
		String sql = "SELECT gprs_amount FROM cc_gprs_batch WHERE batch_id = "+batch_id;
		
		String cacheKey = FcConstant.memSqlNewKey(sql, FcConstant.DB_GET_ONE);
		String cache = jedisPoolUtil.get(cacheKey);
		CcGprsBatch data = null;
		if(StringUtils.isEmpty(cache)) {
			
			data = iCcGprsBatchMapper.getGiveInfoByBatchId(batch_id);
			if(data == null || data.getGprs_amount() == null) {
				return null;
			}else {
				jedisPoolUtil.setEx(cacheKey, data.getGprs_amount().toString());
				return data.getGprs_amount();
			}
		}else {
			return NumberUtils.createDouble(cache);
		}
		
	}
	
	public List<GprsBatchBean> getCards(CcGprsBatchForm form, LoginInfo info){
		
		InputStream is = null;
		try {
			is = form.getFile().getInputStream();
		} catch (IOException e1) {
			
			form.setFile(null);
			log.error("[save][文件读取失败]params={form:{},user:{}}", form.buildJsonString(), JSONObject.toJSONString(info));
			throw new BusinessException(-1, "系统提示：文件读取失败");
		}
		
		form.setFile(null);
		//文件读取
//		List<Object> datas = EasyExcelFactory.read(is, new Sheet(1, 0, GprsBatchBean.class));
		List<GprsBatchBean> cards = new ArrayList<>();
		
		try {
			new ExcelReader(is, null, new AnalysisEventListener<GprsBatchBean>() {
			    @Override
			    public void invoke(GprsBatchBean object, AnalysisContext context) {
			    	if(StringUtils.isNotEmpty(object.getICCID())) {
			    		cards.add(object);
			    	}
			    }

			    @Override
			    public void doAfterAllAnalysed(AnalysisContext context) {
			    }
			}, true).read(new Sheet(1, 0, GprsBatchBean.class));
		}catch(Exception e){
			log.error("[saveBatch][exception]exception={}",ExceptionUtils.getStackTrace(e));
			throw new BusinessException("文件解析异常");
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				log.error("[saveBatch][exception]exception={}",ExceptionUtils.getStackTrace(e));
			}
		}
		
		if(CollectionUtils.isEmpty(cards) || cards.size() < 2) {
			log.error("[save][导入的流量卡列表为空]params={form:{},info:{}}", form.buildJsonString(), JSONObject.toJSONString(info));
			throw new BusinessException("导入的流量卡列表为空");
		}
		
		GprsBatchBean b = cards.get(0);
		if(!StringUtils.equalsIgnoreCase("MSISDN", b.getMSISDN()) || !StringUtils.equalsIgnoreCase("ICCID", b.getICCID()) || !StringUtils.equalsIgnoreCase("IMSI", b.getIMSI())) {
			log.error("[save][无效的excel表头]params={form:{},info:{}}", form.buildJsonString(), JSONObject.toJSONString(info), JSONObject.toJSONString(b));
			throw new BusinessException("无效的excel表头,请参考文件模板");
		}
		
		cards.remove(0);
		
		//数据校验
		for (GprsBatchBean bean : cards) {
			
			if(bean.getMSISDN() != null && bean.getMSISDN().length() > 15 ) {
				log.error("[save][导入的数据中存在无效MSISDN]params={data:{},info:{}}", JSONObject.toJSONString(bean), JSONObject.toJSONString(info));
				throw new BusinessException("导入的数据中存在无效的MSISDN【"+bean.getMSISDN()+"】");
			}
			
			if(bean.getIMSI() != null && bean.getIMSI().length() > 15 ) {
				log.error("[save][导入的数据中存在无效IMSI]params={data:{},info:{}}", JSONObject.toJSONString(bean), JSONObject.toJSONString(info));
				throw new BusinessException("导入的数据中存在无效的IMSI【"+bean.getMSISDN()+"】");
			}
			
			if(bean.getICCID() != null && (bean.getICCID().length() < 19 || bean.getICCID().length() > 20)) {
				log.error("[save][导入的数据中存在无效ICCID]params={data:{},info:{}}", JSONObject.toJSONString(bean), JSONObject.toJSONString(info));
				throw new BusinessException("导入的数据中存在无效的ICCID【"+bean.getICCID()+"】");
			}
		}
		
		return cards;
	}
	
	@Override
	public BatchSaveResultBean saveBatch(CcGprsBatchForm form, LoginInfo info) {
		
		List<GprsBatchBean> cards = getCards(form, info);
		
		CcGprsBatch batch = new CcGprsBatch();
		BeanUtils.copyProperties(form, batch);
		batch.setTime_added(DateUtil.nowStr());
		batch.setCreate_by(info.getLoginName());

		if(batch.getSim_type() == 0){
			batch.setPro_name("");
			batch.setDevice_org_code("");
		}
		this.check(form.getOrg_id(), form.getDevice_org_code(),form.getPro_name(),form.getSim_type());

		if(!this.save(batch)) {
			log.error("[save][保存批次信息出错]params={form:{},info:{}}", form.buildJsonString(), JSONObject.toJSONString(info));
			throw new BusinessException(-1, "系统提示：保存批次信息出错");
		}
		
		CcGprsCard card_data = null;
		CcGprsCard res = null;
		Integer batch_id = batch.getBatch_id();
		
		if(batch_id == null || batch_id < 1 ) {
			log.error("[save][保存批次信息出错,batch_id is null]params={form:{},info:{}}", form.buildJsonString(), JSONObject.toJSONString(info));
			throw new BusinessException(-1, "系统提示：保存批次信息出错");
		}
		CcGprsCard cardCache = null;
		JSONObject log_data = null;
		BatchSaveResultBean bean = new BatchSaveResultBean();
		Integer iccid_count = 0,success_count = 0, update_count = 0,failed_count = 0;
		if(!CollectionUtils.isEmpty(cards)) {
			iccid_count = cards.size();
			List<String> failed_iccid = new ArrayList<>(), update_iccid = new ArrayList<>();
			
			for (GprsBatchBean gprsBatchBean : cards) {
				card_data = new CcGprsCard();
				card_data.setCard_type(form.getCard_type());
				card_data.setCard_sn(gprsBatchBean.getMSISDN());
				card_data.setCard_iccid(gprsBatchBean.getICCID());
				card_data.setCard_imsi(gprsBatchBean.getIMSI());
                card_data.setSim_type(batch.getSim_type());

				/**
				 * 如果导入ICCID卡已存在，则更新ICCID的相关信息
				 */
				res = iCcGprsCardMapper.getGprsCardByIccid(gprsBatchBean.getICCID());
				if(res != null) {
					
					card_data.setBatch_id(batch_id);
					card_data.setCard_id(res.getCard_id());
					card_data.setOrg_id(batch.getOrg_id());
                    card_data.setSim_type(batch.getSim_type());
					/**
					 * 如果ICCID卡已经激活使用，只需要更新机构编号与卡类型
					 */
					if(StringUtils.isEmpty(res.getTime_active())) {
						
						/**
						 * 更新流量卡缓存
						 */
						cardCache = iCcGprsCardService.getByIccidOnlyCache(res.getCard_iccid());
						if(cardCache != null) {
							
							cardCache.setBatch_id(batch_id);
							cardCache.setOrg_id(card_data.getOrg_id());
							cardCache.setCard_type(card_data.getCard_type());
                            cardCache.setSim_type(batch.getSim_type());
							iCcGprsCardService.cacheCardInfo(cardCache);
							
							card_data.setMax_unused(cardCache.getMax_unused() - 0.01);
						}
					}else {
						
						/**
						 * 流量卡未激活需更新流量套餐、导卡时间
						 */
						card_data.setMax_unused(batch.getGprs_amount());
						card_data.setUnicom_unused(batch.getGprs_amount());
						card_data.setTime_added(DateUtil.nowStr());
					}
					
					if(iCcGprsCardService.updateById(card_data)) {
						update_count ++;
						update_iccid.add(gprsBatchBean.getICCID());
						
						if(res.getOrg_id().intValue() != batch.getOrg_id().intValue()) {
							//重新入库不同机构才记录变更机构日志
							log_data = new JSONObject();
							log_data.put("card_id", res.getCard_id());
							log_data.put("org_id", res.getOrg_id());
							log_data.put("org2id", batch.getOrg_id());
							log_data.put("batch_id", batch_id);
							log_data.put("live_month", batch.getLive_month());
							log_data.put("gprs_amount", batch.getGprs_amount());
							log_data.put("time_added", DateUtil.nowStr());
							iCcCardLogService.log7Change(log_data);
							
						}
					}
					
					iCcGprsCardService.removCardCacheInfo(res.getCard_iccid());
					
				}else {
					
					/**
					 * 插入流量卡数据
					 */
					card_data.setBatch_id(batch_id);
					card_data.setOrg_id(batch.getOrg_id());
					card_data.setMax_unused(batch.getGprs_amount());
					card_data.setUnicom_unused(batch.getGprs_amount());
					card_data.setTime_added(DateUtil.nowStr());
					if(iCcGprsCardService.save(card_data)) {
						success_count ++;
						if(card_data.getOrg_id() != 1) {//导入到非(自营)机构下的卡记录入库日志
							iCcCardLogService.log1Storage(card_data, batch);
						}
						continue;
					}
					
					failed_count ++;
					failed_iccid.add(gprsBatchBean.getICCID());
				}
			}
			
			if(success_count == 0 && update_count == 0) {
				this.removeById(batch_id);
			}else {
				iCcGprsBatchMapper.updateCardAmount();
			}
			form.setFile(null);
			log.info("[save][批次录入记录]params={form:{},iccid_count:{},failed_count:{},update_count:{},success_count:{},failed_iccid:{},update_iccid:{},cards:{},user:{}}", 
					form.buildJsonString(),iccid_count,failed_count,update_count,success_count, JSONObject.toJSONString(failed_iccid),JSONObject.toJSONString(update_iccid),
					JSONObject.toJSONString(cards),JSONObject.toJSONString(info));
		}
		
		
		bean.setBatch_sn(batch.getBatch_sn());
		bean.setFailed_count(failed_count);
		bean.setIccid_count(iccid_count);
		bean.setSuccess_count(success_count);
		bean.setUpdate_count(update_count);
		
		return bean;
	}
	
	@Override
	public CcGprsBatch getInfoByBatchId(Integer batch_id, LoginInfo info) {
		
		if(batch_id == null) {
			return null;
		}
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		return iCcGprsBatchMapper.getInfoById(batch_id, orgpos, orgpos.split(","));
	}
	
	@Override
	public boolean updateBatchInfo(CcGprsBatchForm form, LoginInfo info) {
		
		CcGprsBatch batch = this.getInfoByBatchId(form.getBatch_id(), info);
		if(batch == null) {
			log.warn("[update][未找到对应的出货批次信息]params={form:{},info:{}}", form.buildJsonString(), info.buildJsonString());
			throw new BusinessException(-1, "未找到对应的出货批次信息");
		}
		
//		batch.setAlter_id(info.getId());
		//batch.setAlter_id(0);
		batch.setBatch_sn(form.getBatch_sn());
		batch.setBatch_name(form.getBatch_name());
		batch.setBatch_memo(form.getBatch_memo());
		batch.setBatch_shipper(form.getBatch_shipper());
		batch.setProvince_id(form.getProvince_id());
		batch.setCity_id(form.getCity_id());
		batch.setDistrict_id(form.getDistrict_id());
		batch.setTime_modify(DateUtil.nowStr());
		batch.setUpdate_by(info.getLoginName());
        //batch.setPro_name(form.getPro_name());
		return this.updateById(batch);
	}

	@Override
	public CcGprsBatch check(Integer org_id, String device_org_code, String pro_name, Short sim_type){

		JSONObject form = new JSONObject();
		form.put("org_id", org_id);
		form.put("device_org_code", device_org_code);
		form.put("pro_name", pro_name);
		form.put("sim_type", sim_type);
		if(org_id == null){
			return null;
		}

		if(sim_type == null || sim_type == 0 || StringUtils.isEmpty(device_org_code) || StringUtils.isEmpty(pro_name)){
			return null;
		}
		//根据对应的条件获取批次信息
		CcGprsBatch batch = this.iCcGprsBatchMapper.getByDeviceOrgAndProNameAndSimtype(device_org_code,pro_name,sim_type);
		//如果为空则直接结束
		//否则判断机构id是否一致，如果一致则返回状态码 1前端提示是否覆盖，否则返回-1 机构错误
		if(batch == null){
			return null;
		}else if(org_id - batch.getOrg_id() != 0){
			log.warn("[check][机构填写有误]params={}", form.toJSONString());
			throw new BusinessException(-1, "请确认机构映射关系配置是否有误");
		}else{
			return batch;
		}
	}
	
}
