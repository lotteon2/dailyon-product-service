package com.dailyon.productservice.common.config;

import com.dailyon.productservice.common.filter.SimpleAuthorizeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// TODO : 게이트웨이 설정 이후 삭제
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Bean
    public FilterRegistrationBean<SimpleAuthorizeFilter> authorizeFilter() {
        FilterRegistrationBean<SimpleAuthorizeFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new SimpleAuthorizeFilter());
        filterRegistrationBean.addUrlPatterns("/admin/*");

        return filterRegistrationBean;
    }
}