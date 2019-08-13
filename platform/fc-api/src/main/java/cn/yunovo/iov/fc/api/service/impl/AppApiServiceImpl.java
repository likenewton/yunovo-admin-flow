package cn.yunovo.iov.fc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.yunovo.iov.fc.api.service.IAppApiService;
import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.form.api.BelongForm;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppApiServiceImpl implements IAppApiService{

	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	
	public void belong(BelongForm form) {
	
		//获取流量卡信息,必须是我司流量卡
		CcGprsCard card = iCcGprsCardService.getByIccid(form.getIccid());
		if(card == null) {
			
			log.warn("[belong][iccid not found]params={}", form.buildJsonString());
			throw new BusinessException(-1, "iccid not found");
		}
		
		//只有贴片卡才需要变更机构
		if(card.getCard_type() != 1) {
			log.warn("[belong][iccid nonsupport]params={}", form.buildJsonString());
			throw new BusinessException(0, "iccid not nonsupport");
		}
		
		//通过device_org 获取对应的关系信息
		
	}
	
	
}
