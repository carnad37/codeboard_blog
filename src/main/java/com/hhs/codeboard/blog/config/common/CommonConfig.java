package com.hhs.codeboard.blog.config.common;


import com.hhs.codeboard.blog.config.anno.CodeboardBlogConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    @ConfigurationProperties(prefix = "codeboard.init")
    public CodeboardBlogConfig codeboardBlogConfig() {
        return new CodeboardBlogConfig();
    }


}
