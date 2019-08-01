package cn.yunovo.iov.fc.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.yunovo.iov.fc.model.entity.CcApkWhitelistLastreportInfo;

/**
 * <p>
 * 流量卡最后一次应用白名单消耗上报信息 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-07-29
 */
public interface ICcApkWhitelistLastreportInfoMapper extends BaseMapper<CcApkWhitelistLastreportInfo> {

	public int updateInfoById(@Param("info")CcApkWhitelistLastreportInfo info);
	
}
