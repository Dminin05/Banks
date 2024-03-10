package com.minin.banks.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.patterns")
@Data
public class PatternProperties {
    private String emailPattern;
}
