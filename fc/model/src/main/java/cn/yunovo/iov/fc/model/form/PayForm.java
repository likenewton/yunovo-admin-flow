package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.ValidateGroup;
import cn.yunovo.iov.fc.model.entity.CcSetting;
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
	@NotEmpty(message="支付方式填写有误！",groups= {AlipayValidateGroup.class, WxpayValidateGroup.class})
	private String type;
	
	@NotEmpty(message="请填写签约账号!", groups=AlipayValidateGroup.class)
	@ApiModelProperty(value="支付宝支付-签约账号")
	private String alipay_key;
	
	@NotEmpty(message="请填写收款支付宝账号!", groups=AlipayValidateGroup.class)
	@ApiModelProperty(value="支付宝支付-收款支付宝账号")
	private String alipay_seller_id;
	
	@NotEmpty(message="请填写商户支付密钥!", groups=AlipayValidateGroup.class)
	@ApiModelProperty(value="支付宝支付-商户支付密钥")
	private String alipay_partner;
	
	//----------------------------------------------------
	@NotEmpty(message="请填写支付APPID!", groups=WxpayValidateGroup.class)
	@ApiModelProperty(value="微信支付-支付APPID")
	private String wxpay_appid;
	
	@NotEmpty(message="请填写微信支付商户号!", groups=WxpayValidateGroup.class)
	@ApiModelProperty(value="微信支付-微信支付商户号")
	private String wxpay_mchid;
	
	@NotEmpty(message="请填写商户支付密钥!", groups=WxpayValidateGroup.class)
	@ApiModelProperty(value="微信支付-商户支付密钥")
	private String wxpay_mchkey;
	
	@NotEmpty(message="请填写微信支付公众号SECERT!", groups=WxpayValidateGroup.class)
	@ApiModelProperty(value="微信支付-微信支付公众号SECERT")
	private String wxpay_appsecret;
	
	@ApiModelProperty(value="微信支付-CERT证书绝对路径")
	private String wxpay_sslcert_path;
	
	@ApiModelProperty(value="微信支付-KEY证书绝对路径")
	private String wxpay_sslkey_path;
	
	public List<CcSetting> build(){
		
		List<CcSetting> settings = new ArrayList<>();
		if(StringUtils.equals("alipay", this.type)) {
			
			settings.add(new CcSetting(this.type,"alipay_key",this.alipay_key));
			settings.add(new CcSetting(this.type,"alipay_seller_id",this.alipay_seller_id));
			settings.add(new CcSetting(this.type,"alipay_partner",this.alipay_partner));
			
		}else if(StringUtils.equals("wxpay", this.type)){
		
			settings.add(new CcSetting(this.type,"wxpay_appid",this.wxpay_appid));
			settings.add(new CcSetting(this.type,"wxpay_mchid",this.wxpay_mchid));
			settings.add(new CcSetting(this.type,"wxpay_mchkey",this.wxpay_mchkey));
			settings.add(new CcSetting(this.type,"wxpay_appsecret",this.wxpay_appsecret));
			settings.add(new CcSetting(this.type,"wxpay_sslcert_path",this.wxpay_sslcert_path));
			settings.add(new CcSetting(this.type,"wxpay_sslkey_path",this.wxpay_sslkey_path));
		}
		
		return settings;
	}

	@SuppressWarnings("unchecked")
	public void validate() {
		
		if(StringUtils.equals("alipay", this.type)) {
			super.validate(AlipayValidateGroup.class);
		}else if(StringUtils.equals("wxpay", this.type)){
			super.validate(WxpayValidateGroup.class);
		}
		
	}
	
	interface AlipayValidateGroup extends ValidateGroup{}
	
	interface WxpayValidateGroup extends ValidateGroup{}
	
}
