package cn.yunovo.iov.fc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPack;

/**
 * <p>
 * 流量充值套餐表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
public interface ICcGprsPackService extends IService<CcGprsPack> {

	public List<SelectBean> select(LoginInfo info);
	
}
