package cn.yunovo.iov.fc.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcExtension;
import cn.yunovo.iov.fc.model.result.PayInfoBean;

/**
 * <p>
 * 延伸表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
public interface ICcExtensionService extends IService<CcExtension> {

	Map<String, Map<String, String>> getPays();

	List<PayInfoBean> getPaymentItems(LoginInfo info);

}
