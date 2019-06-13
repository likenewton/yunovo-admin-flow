package cn.yunovo.iov.fc.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcLanguage;

/**
 * <p>
 * 语言表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcLanguageMapper extends BaseMapper<CcLanguage> {

	public List<CcLanguage> getItemsPage(IPage<CcLanguage> page);
	
}
