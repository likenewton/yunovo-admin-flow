package cn.yunovo.iov.fc.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import cn.yunovo.iov.fc.model.BaseForm;
import cn.yunovo.iov.fc.model.ValidateGroup;
import cn.yunovo.iov.fc.model.entity.CcSetting;
import cn.yunovo.iov.fc.model.form.PayForm.AlipayValidateGroup;
import cn.yunovo.iov.fc.model.form.PayForm.WxpayValidateGroup;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class SystemParamsForm extends BaseForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String GROUP = "config";

	@ApiModelProperty("当前编辑标签页,编辑项目(tab-general)、系统设置(tab-store)、本地化配置(tab-local)、邮件协议(tab-mail)、服务器设置(tab-server)")
	private String tabName;
	
	@NotEmpty(message="请输入系统名称",groups=TabGeneralValidateGroup.class)
	@Size(max=32, min=2, message="系统名称必须在2至32个字符之间！",groups=TabGeneralValidateGroup.class)
	@ApiModelProperty("编辑项目-系统名称")
	private String config_name;
	
	@NotEmpty(message="请输入系统拥有者",groups=TabGeneralValidateGroup.class)
	@Size(max=64, min=3, message="系统拥有者必须在3至64个字符之间！",groups=TabGeneralValidateGroup.class)
	@ApiModelProperty("编辑项目-系统拥有者")
	private String config_owner;
	
	@NotEmpty(message="请输入联系地址",groups=TabGeneralValidateGroup.class)
	@Size(max=256, min=10, message="联系地址必须在10到256个字符之间！",groups=TabGeneralValidateGroup.class)
	@ApiModelProperty("编辑项目-联系地址")
	private String config_address;
	
	@NotEmpty(message="请输入电子邮箱",groups=TabGeneralValidateGroup.class)
	@Email(regexp="(^$)|(^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$)", groups= {TabGeneralValidateGroup.class}, message="无效的电子邮箱！")
	@ApiModelProperty("编辑项目-电子邮箱")
	private String config_email;
	
	@NotEmpty(message="请输入联系电话",groups=TabGeneralValidateGroup.class)
	@ApiModelProperty("编辑项目-联系电话")
	private String config_telephone;
	
	@ApiModelProperty("编辑项目-传真号码")
	private String config_fax;
	
	//--------------------------------------------
	@NotEmpty(message="请输入首页标题",groups=TabStoreValidateGroup.class)
	@Size(max=32, min=2, message="首页标题必须在2至32个字符之间！",groups=TabStoreValidateGroup.class)
	@ApiModelProperty("系统设置-首页标题")
	private String config_title;
	
	@ApiModelProperty("系统设置-SEO设置")
	private String config_meta_description;
	
	@ApiModelProperty("系统设置-模板设置")
	private String config_template;
	
	//--------------------------------------------
	@ApiModelProperty("本地化设置-前台语言")
	private String config_language;
	
	@ApiModelProperty("本地化设置-后台语言")
	private String config_admin_language;
	
	@ApiModelProperty("本地化设置-货币设置")
	private String config_currency;
	
	@ApiModelProperty("本地化设置-货币更新")
	private String config_currency_auto;
	
	@NotEmpty(message="请输入每页默认产品数量 (前台管理)", groups=TabLocalValidateGroup.class)
	@Pattern(regexp="^\\d+$", message="每页默认产品数量 (前台管理)该字段必须是一个正整数", groups=TabLocalValidateGroup.class)
	@ApiModelProperty("本地化设置-每页默认产品数量 (前台管理)")
	private String config_catalog_limit;
	
	@NotEmpty(message="请输入每页默认产品数量 (后台管理)", groups=TabLocalValidateGroup.class)
	@Pattern(regexp="^\\d+$", message="每页默认产品数量 (后台管理)该字段必须是一个正整数", groups=TabLocalValidateGroup.class)
	@ApiModelProperty("本地化设置-每页默认产品数量 (后台管理)")
	private String config_admin_limit;
	
	//--------------------------------------------
	@ApiModelProperty("邮件协议-邮件协议")
	private String config_mail_protocol;
	
	@ApiModelProperty("邮件协议-邮件参数")
	private String config_mail_parameter;
	
	@ApiModelProperty("邮件协议-SMTP 主机")
	private String config_smtp_host;
	
	@ApiModelProperty("邮件协议-SMTP 用户")
	private String config_smtp_username;
	
	@ApiModelProperty("邮件协议-SMTP 密码")
	private String config_smtp_password;
	
	@ApiModelProperty("邮件协议-SMTP 端口")
	private String config_smtp_port;
	
	@ApiModelProperty("邮件协议-SMTP 超时")
	private String config_smtp_timeout;
	
	//--------------------------------------------
	@ApiModelProperty("服务器设置-使用 SSL")
	private String config_use_ssl;
	
	@ApiModelProperty("服务器设置-最大登录次数")
	private String config_login_count_max;
	
	@ApiModelProperty("服务器设置-登录失败锁定小时")
	private String config_login_locked_hours;
	
	@ApiModelProperty("服务器设置-允许上传文件的扩展名")
	private String config_file_extension_allowed;
	
	@ApiModelProperty("服务器设置-允许上传文件的类型")
	private String config_file_mime_allowed;
	
	@ApiModelProperty("服务器设置-系统维护模式")
	private String config_maintenance;
	
	@ApiModelProperty("服务器设置-加密密钥")
	private String config_encryption;
	
	@ApiModelProperty("服务器设置-输出压缩等级")
	private String config_compression;
	
	@ApiModelProperty("服务器设置-显示错误")
	private String config_error_display;
	
	@ApiModelProperty("服务器设置-日志错误")
	private String config_error_log;
	
	@NotEmpty(message="请输入错误日志文件名",groups=TabServerValidateGroup.class)
	@ApiModelProperty("服务器设置-错误日志文件名")
	private String config_error_filename;
	
	@ApiModelProperty("服务器设置-Google Analytics Code")
	private String config_google_analytics;
	
	public List<CcSetting> build(){
		
		List<CcSetting> settings = new ArrayList<>();
		if(StringUtils.equals("tab-general", this.tabName)) {
			
			settings.add(new CcSetting(GROUP,"config_name",this.config_name));
			settings.add(new CcSetting(GROUP,"config_owner",this.config_owner));
			settings.add(new CcSetting(GROUP,"config_address",this.config_address));
			settings.add(new CcSetting(GROUP,"config_email",this.config_email));
			settings.add(new CcSetting(GROUP,"config_telephone",this.config_telephone));
			settings.add(new CcSetting(GROUP,"config_fax",this.config_fax));
		}else if(StringUtils.equals("tab-store", this.tabName)){
		
			settings.add(new CcSetting(GROUP,"config_title",this.config_title));
			settings.add(new CcSetting(GROUP,"config_meta_description",this.config_meta_description));
			settings.add(new CcSetting(GROUP,"config_template",this.config_template));
		}else if(StringUtils.equals("tab-local", this.tabName)){
		
			settings.add(new CcSetting(GROUP,"config_language",this.config_language));
			settings.add(new CcSetting(GROUP,"config_admin_language",this.config_admin_language));
			settings.add(new CcSetting(GROUP,"config_currency",this.config_currency));
			settings.add(new CcSetting(GROUP,"config_currency_auto",this.config_currency_auto));
			settings.add(new CcSetting(GROUP,"config_catalog_limit",this.config_catalog_limit));
			settings.add(new CcSetting(GROUP,"config_admin_limit",this.config_admin_limit));
		}else if(StringUtils.equals("tab-mail", this.tabName)){
		
			settings.add(new CcSetting(GROUP,"config_mail_protocol",this.config_mail_protocol));
			settings.add(new CcSetting(GROUP,"config_mail_parameter",this.config_mail_parameter));
			settings.add(new CcSetting(GROUP,"config_smtp_host",this.config_smtp_host));
			settings.add(new CcSetting(GROUP,"config_smtp_username",this.config_smtp_username));
			settings.add(new CcSetting(GROUP,"config_smtp_password",this.config_smtp_password));
			settings.add(new CcSetting(GROUP,"config_smtp_port",this.config_smtp_port));
			settings.add(new CcSetting(GROUP,"config_smtp_timeout",this.config_smtp_timeout));
		}else if(StringUtils.equals("tab-server", this.tabName)){
		
			settings.add(new CcSetting(GROUP,"config_use_ssl",this.config_use_ssl));
			settings.add(new CcSetting(GROUP,"config_login_count_max",this.config_login_count_max));
			settings.add(new CcSetting(GROUP,"config_login_locked_hours",this.config_login_locked_hours));
			settings.add(new CcSetting(GROUP,"config_file_extension_allowed",this.config_file_extension_allowed));
			settings.add(new CcSetting(GROUP,"config_file_mime_allowed",this.config_file_mime_allowed));
			settings.add(new CcSetting(GROUP,"config_maintenance",this.config_maintenance));
			settings.add(new CcSetting(GROUP,"config_encryption",this.config_encryption));
			settings.add(new CcSetting(GROUP,"config_compression",this.config_compression));
			settings.add(new CcSetting(GROUP,"config_error_display",this.config_error_display));
			settings.add(new CcSetting(GROUP,"config_error_log",this.config_error_log));
			settings.add(new CcSetting(GROUP,"config_error_filename",this.config_error_filename));
			settings.add(new CcSetting(GROUP,"config_google_analytics",this.config_google_analytics));
		}
		
		return settings;
	}

	@SuppressWarnings("unchecked")
	public void validate() {
		
		if(StringUtils.equals("tab-general", this.tabName)) {
			
			super.validate(TabGeneralValidateGroup.class);
		}else if(StringUtils.equals("tab-store", this.tabName)){
			super.validate(TabStoreValidateGroup.class);
		}else if(StringUtils.equals("tab-local", this.tabName)){
			super.validate(TabLocalValidateGroup.class);
		}else if(StringUtils.equals("tab-mail", this.tabName)){
			super.validate(TabMailValidateGroup.class);
		}else if(StringUtils.equals("tab-server", this.tabName)){
			super.validate(TabServerValidateGroup.class);
		}
		
	}
	
	interface TabGeneralValidateGroup extends ValidateGroup{}
	
	interface TabStoreValidateGroup extends ValidateGroup{}
	
	interface TabMailValidateGroup extends ValidateGroup{}
	
	interface TabLocalValidateGroup extends ValidateGroup{}
	
	interface TabServerValidateGroup extends ValidateGroup{}
	
	
	
}
