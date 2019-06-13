package cn.yunovo.iov.fc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcCurrency;
import cn.yunovo.iov.fc.model.form.CurrencyForm;

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

	public CcCurrency detail(Integer currency_id, LoginInfo loginBaseInfo);

	int insert(CurrencyForm form, LoginInfo info);

	public int update(CurrencyForm form, LoginInfo loginBaseInfo);

	public int delete(CurrencyForm form, LoginInfo loginBaseInfo);

	List<SelectBean> select();
	
}
