package com.hhs.codeboard.blog.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/dist/**")
//            .addResourceLocations("classpath:/dist/")
//            .setCachePeriod(20);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setWriteAcceptCharset(false);
        converters.add(converter);
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> URL_PATTERNS = Arrays.asList("/main/**", "/board/**","/menu/**", "/category/**");
//        registry
                //.addInterceptor(handlerInterceptor())
//        	.addPathPatterns(URL_PATTERNS)
//            .excludePathPatterns("/open/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

//    @Bean
//    public HandlerInterceptor handlerInterceptor() {
//        return new SiteInterceptorHandler();
//    }

    /**
     * @since 2021.07.17
     * @see com.hhs.codeboard.config.web.WebControllerBindConfig#initBinder(WebDataBinder)
     * deprecated된 initBinder를 포함한 WebBindingInitializer를 구현하므로 ControllerAdvice 식으로 대체함.
     */

//    @Bean
//    public ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        //DefaultFormattingConversionService가 없으면 dto로 맵핑이 안됨.
//
//        initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
//            propertyEditorRegistry.registerCustomEditor(Integer.class,
//                    new CustomIntegerEditor(0, false));
//        });
//        return initializer;
//    }

}