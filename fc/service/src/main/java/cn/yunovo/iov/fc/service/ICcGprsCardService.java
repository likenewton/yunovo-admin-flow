package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * GPRS流量卡信息表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcGprsCardService extends IService<CcGprsCard> {

	
	public Map<String, String> getCard_type();
	
	public PageData<CcGprsCard, HashMap<String, Double>> getHaltPage(PageForm pageForm, String card_iccid,
			Integer card_type, Integer org_id, Integer time_expire, LoginInfo info);
}
