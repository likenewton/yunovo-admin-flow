package cn.yunovo.iov.fc.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCurrency;
import cn.yunovo.iov.fc.service.ICcCurrencyService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags="系统设置-货币管理")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CurrencyController extends BaseController{

	@Autowired
	private ICcCurrencyService iCcCurrencyService;
	
	@RequestMapping(path="/currency/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-货币查询")
	public Result<PageData<CcCurrency, Object>> getItemsPage(PageForm form) {
		
		PageData<CcCurrency, Object> data  = iCcCurrencyService.getItemsPage(form, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@RequestMapping(path="/currency/detail", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "currency_id", value = "货币id", required = true, dataType = "int",paramType = "query")
			})
	@ApiOperation(value="系统设置-货币详情")
	public Result<CcCurrency> detail(Integer currency_id) {
		CcCurrency data  = iCcCurrencyService.detail(currency_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	public void insert() {
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
	
}
