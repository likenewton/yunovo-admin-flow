package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcNation;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 国家区域表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
public interface ICcNationService extends IService<CcNation> {

	public PageData<CcNation, List<CcNation>> getNationsPage(PageForm pageForm, Integer ntid, LoginInfo loginInfo);
	
	CcNation getById(Integer ntid);

	List<CcNation> getNation(Integer ntid);

	List<SelectBean> select(Integer parent);

	JSONObject nationMap();
	
}
