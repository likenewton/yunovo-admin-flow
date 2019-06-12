package cn.yunovo.iov.fc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCurrency;

/**
 * <p>
 * 货币表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-12
 */
public interface ICcCurrencyService extends IService<CcCurrency> {

	public PageData<CcCurrency, Object> getItemsPage(PageForm form, LoginInfo info);
	
}
