package com.hhs.codeboard.blog.config.common;


import com.hhs.codeboard.blog.config.anno.CodeboardBlogConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@Configuration
public class CommonConfig {

    @Bean
    @ConfigurationProperties(prefix = "codeboard.init")
    public CodeboardBlogConfig codeboardBlogConfig() {
        return new CodeboardBlogConfig();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
