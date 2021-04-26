package com.redis_study.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author lyf
 */
@Configuration
@EnableSwagger2
@RestController
public class MySwaggerConfig {

    @Bean
    public Docket swaggerSetting() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.redis_study.controller"))
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Redis Study")
                .description("learning redis")
                .version("0.0")
                .build();
    }

    @RequestMapping("/")//重定向url
    public ModelAndView forwardSwagger() {
        ModelAndView mvc=new ModelAndView();
        mvc.setViewName("redirect:/swagger-ui.html");
        return mvc;
    }
}
