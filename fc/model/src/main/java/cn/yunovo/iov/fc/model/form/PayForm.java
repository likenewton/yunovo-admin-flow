package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;

import cn.yunovo.iov.fc.model.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class PayForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="支付方式,ex: 支付宝支付 = alipay")
	private String type;
	
	@ApiModelProperty(value="支付宝支付-签约账号")
	private String entry_partner;
	
	@ApiModelProperty(value="支付宝支付-收款支付宝账号")
	private String entry_seller_id;
	
	@ApiModelProperty(value="支付宝支付-商户支付密钥")
	private String entry_key;
	
	//----------------------------------------------------
	@ApiModelProperty(value="微信支付-支付APPID")
	private String entry_appid;
	
	@ApiModelProperty(value="微信支付-微信支付商户号")
	private String entry_mchid;
	
	@ApiModelProperty(value="微信支付-商户支付密钥")
	private String entry_mchkey;
	
	@ApiModelProperty(value="微信支付-微信支付公众号SECERT")
	private String entry_appsecret;
	
	@ApiModelProperty(value="微信支付-CERT证书绝对路径")
	private String entry_sslcert_path;
	
	@ApiModelProperty(value="微信支付-KEY证书绝对路径")
	private String entry_sslkey_path;
	
}
