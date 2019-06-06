package cn.yunovo.iov.fc.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcNotify;

/**
 * <p>
 * 通知或来源统计分析表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-04
 */
public interface ICcNotifyService extends IService<CcNotify> {

	public PageData<CcNotify,Object> getItemsPage(PageForm pageForm, String ntf_type, String date_start, String date_end, LoginInfo info);
	
	public PageData<CcNotify,Object> getItemsOrgPage(PageForm pageForm, String ntf_type, Integer org_id, String ntf_date, LoginInfo info);

	List<SelectBean> select();
	
	Map<String, String> getArr_ntf_type();
	
}
