package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许携带cookie
        config.addAllowedOriginPattern("*"); // 允许所有域名进行跨域调用
        config.addAllowedHeader("*"); // 放行全部原始头信息
        config.addAllowedMethod("*"); // 允许所有请求方法跨域调用
        source.registerCorsConfiguration("/**", config); // 对所有接口生效
        return new CorsFilter(source);
    }
}