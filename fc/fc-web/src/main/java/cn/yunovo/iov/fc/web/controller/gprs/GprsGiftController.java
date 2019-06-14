package cn.yunovo.iov.fc.web.controller.gprs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsGift;
import cn.yunovo.iov.fc.service.ICcGprsGiftService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-流量赠送接口列表")
@RequestMapping(path="/api/gprs/gift", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GprsGiftController extends BaseController{

	@Autowired
	private ICcGprsGiftService iCcGprsGiftService;
	
	@ApiOperation(value="业务管理-流量赠送历史查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "重置时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "重置时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsGift, Object>> list(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end) {
		
		PageData<CcGprsGift, Object>  data = iCcGprsGiftService.getItemsPage(form, org_id, card_iccid, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
}
