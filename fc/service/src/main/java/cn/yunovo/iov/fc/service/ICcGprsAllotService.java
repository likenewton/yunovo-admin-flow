package cn.yunovo.iov.fc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.GprsCalculateBean;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.result.UnicomDataBean;

/**
 * <p>
 * 流量分配表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsAllotService extends IService<CcGprsAllot> {

	
	/**
	 * @param card_id
	 * @return
	 */
	boolean gprsAllot(Integer card_id);

	/**
	 * @param card
	 * @param gprs
	 * @return
	 */
	CcGprsCard gprsCalculate(CcGprsCard card, GprsCalculateBean gprs);

	
	boolean cardOnoff(CcGprsCard card, int onoff, int userid, String username);

	UnicomDataBean syncUnicomData(CcGprsCard card);

}
