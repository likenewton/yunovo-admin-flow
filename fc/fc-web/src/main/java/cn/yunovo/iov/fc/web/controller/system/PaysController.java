package cn.yunovo.iov.fc.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.result.PayInfoBean;
import cn.yunovo.iov.fc.service.ICcExtensionService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="系统设置-支付方式")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaysController extends BaseController{

	@Autowired
	private ICcExtensionService iCcExtensionService;
	
	@RequestMapping(path="/pays/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式查询")
	public Result<List<PayInfoBean>> getItems() {
		
		List<PayInfoBean> info = iCcExtensionService.getPaymentItems(this.getLoginBaseInfo());
		return ResultUtil.success(info);
		
	}
	
}
