package org.lpz.graduationprojectbackend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

// 标明是配置类
@Configuration
// 开启Swagger功能
@EnableSwagger2WebMvc
//@Profile("prod")
public class SwaggerConfig {
    /**
     * 构建一个Docket Bean
     * @return
     */
    @Bean
    public Docket createRestapi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 页面展示信息
                .apiInfo(apiInfo())
                // 返回一个ApiSelectorBuilder实例，用来控制接口被Swagger做成文档
                .select()
                // 用于指定扫描哪个包下的接口
                .apis(RequestHandlerSelectors.basePackage("org.lpz.graduationprojectbackend.controller"))
                // 选择所有的API，如果你只想为部分API生成文档，可以配置这里
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 定义api文档主页面的基本信息
     * 访问地址：http://项目实际地址/swagger-ui.html
     * 或者Knife4j：http://localhost:8080/api/doc.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("社区老人健康服务系统")
                .description("社区老人健康服务系统接口文档")
                .contact("lpz")
                // 版本号
                .version("1.0")
                .build();
    }
}