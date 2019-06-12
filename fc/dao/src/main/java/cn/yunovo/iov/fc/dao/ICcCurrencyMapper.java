package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcCurrency;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 货币表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-12
 */
public interface ICcCurrencyMapper extends BaseMapper<CcCurrency> {

	public List<CcCurrency> getItemsPage(IPage<CcCurrency> page);
	
}
