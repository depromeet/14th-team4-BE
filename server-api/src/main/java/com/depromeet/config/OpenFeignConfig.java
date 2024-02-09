package com.depromeet.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.depromeet")
public class OpenFeignConfig {
}
