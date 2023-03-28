package com.hhs.codeboard.blog.config.security;

import com.hhs.codeboard.blog.web.service.member.MemberService;
import com.hhs.codeboard.blog.web.service.member.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@DependsOn("webClient")
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**"
        );
    }

    @Bean
    protected SecurityFilterChain filter(WebClient webClient, HttpSecurity http) throws Exception {

        //공통 사용
        AuthenticationSuccessHandler successHandler = loginSuccessHandler();

        http.authorizeRequests()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/private/**").permitAll()
//            .requestMatchers("/private/**").authenticated()
            .anyRequest().denyAll()
            .and()
            .httpBasic().disable()
            .formLogin().disable()
//                .cors().disable()
//                .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionFixation().none()
            .and()
            .addFilterBefore(new JWTLoginFilter(webClient), UsernamePasswordAuthenticationFilter.class)
//            .and()
//            .exceptionHandling()
//        	.accessDeniedHandler("/denied")
//            .and()
//                .rememberMe().key("dontReadKey")
//                .userDetailsService(memberService())
//                .authenticationSuccessHandler(successHandler);
            ;
        return http.build();
    }

    /**
     * 따로 해당 클래스에서 Component나 Service로 등록하지않고
     * 일괄적으로 Bean 등록
     */
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl();
    }

    @Bean
    public LogoutHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderImpl();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    
}