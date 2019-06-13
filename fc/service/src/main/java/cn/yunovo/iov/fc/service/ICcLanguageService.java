package cn.yunovo.iov.fc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcLanguage;

/**
 * <p>
 * 语言表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
public interface ICcLanguageService extends IService<CcLanguage> {

	List<SelectBean> select();

}
