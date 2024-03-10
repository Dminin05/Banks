package com.minin.banks;

import com.minin.banks.properties.JwtProperties;
import com.minin.banks.properties.PatternProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        {JwtProperties.class, PatternProperties.class}
)
public class BanksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanksApplication.class, args);
    }

}
