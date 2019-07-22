package cn.yunovo.iov.fc.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;

/**
 * <p>
 * 流量值表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsValueService extends IService<CcGprsValue> {

	public PageData<GprsAllotResultBean, Object> getAllotPage(PageForm pageForm, Integer card_id, LoginInfo info);

	Map<Integer, CcGprsValue> allotValueMapForHowmonth(Integer card_id, Integer allot_id, Integer[] how_month);
	
}
