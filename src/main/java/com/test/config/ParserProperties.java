package com.test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "parser")
@PropertySource(value = "classpath:parser.properties", encoding = "UTF-8")
public class ParserProperties {
    private String regexp;
}
