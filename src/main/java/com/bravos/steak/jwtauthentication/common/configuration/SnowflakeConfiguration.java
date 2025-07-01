package com.bravos.steak.jwtauthentication.common.configuration;

import com.bravos.steak.jwtauthentication.common.service.SnowflakeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfiguration {

    @Bean
    public SnowflakeService snowflakeService() {
        return new SnowflakeService(1);
    }

}
