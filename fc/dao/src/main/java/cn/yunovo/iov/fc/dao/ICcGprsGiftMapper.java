package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcGprsGift;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * GPRS流量赠送表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsGiftMapper extends BaseMapper<CcGprsGift> {

	List<CcGprsGift> getItemsPage(IPage<CcGprsGift> page, @Param("org_id")Integer org_id, @Param("card_iccid")String card_iccid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);

}
