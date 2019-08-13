package cn.yunovo.iov.fc.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.exception.FormValidateException;
import cn.yunovo.iov.fc.model.form.api.BelongForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AppApiController {

	
	
	/**
	 * 流量平台关联的车联网设备入库批次编号将设备的卡归属于某批次下的初始信息
	 * @param iccid 流量卡iccid
	 * @param device_org 流量卡所在设备的所属机构代码
	 * @return 处理结果
	 */
	@RequestMapping(path="/app/api/belong", method= {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> belong(BelongForm form){
		
		try {
			form.validate();
		} catch (FormValidateException e) {
			
			log.warn("[belong][form error]params={}", form.buildJsonString());
			return ResultUtil.build(0, e.getMessage());
		}
		
		return null;
	}
	
}
