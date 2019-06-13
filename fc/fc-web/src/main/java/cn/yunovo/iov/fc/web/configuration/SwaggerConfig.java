package cn.yunovo.iov.fc.web.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket createRestApi() {
		
		ParameterBuilder tokenPar = new ParameterBuilder();  
        List<springfox.documentation.service.Parameter> pars = new ArrayList<>();  
        tokenPar.name("_iov_token_").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
		
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.yunovo.iov"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("流量卡中心  Apis")
            .version("0.1")
            .build();
    }

}