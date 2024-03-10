package com.minin.banks.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "spring.security")
@Data
public class JwtProperties {
    private String accessSecret;
    private Duration lifeTime;
}
