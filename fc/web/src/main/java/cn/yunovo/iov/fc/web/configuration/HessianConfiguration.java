package cn.yunovo.iov.fc.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.sunshine.dcda.view.system.viewcomponent.ICooperateOrganViewComponent;
import org.sunshine.dcda.view.system.viewcomponent.ISystemResourceViewComponent;
import org.sunshine.dcda.view.system.viewcomponent.ISystemUserViewComponent;

@Configuration
public class HessianConfiguration {

	@Value("${hessian.path.usercenter}")
    private String usercenter;
	
	@Value("${hessian.path.mpk}")
    private String mpk;
	
	@Bean(name = "systemUserViewComponent")
	public HessianProxyFactoryBean systemUserViewComponentClient() {
		HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
		factory.setServiceUrl(usercenter+"/remote/systemUserViewComponent");
		factory.setServiceInterface(ISystemUserViewComponent.class);
		return factory;
	}

	@Bean(name = "systemResourceViewComponent")
	public HessianProxyFactoryBean systemResourceViewComponent() {
		HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
		factory.setServiceUrl(usercenter+"/remote/systemResourceViewComponent");
		factory.setServiceInterface(ISystemResourceViewComponent.class);
		return factory;
	}
	
	@Bean(name = "cooperateOrganViewComponent")
	public HessianProxyFactoryBean cooperateOrganViewComponent() {
		HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
		factory.setServiceUrl(usercenter+"/remote/cooperateOrganViewComponent");
		factory.setServiceInterface(ICooperateOrganViewComponent.class);
		return factory;
	}
}
