package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.entity.CcApkWhitelistUsedReportInfo;
import cn.yunovo.iov.fc.model.form.api.WhitelistsReportForm;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bill
 * @since 2019-07-25
 */
public interface ICcApkWhitelistUsedReportInfoService extends IService<CcApkWhitelistUsedReportInfo> {

	void report(WhitelistsReportForm form);

}
