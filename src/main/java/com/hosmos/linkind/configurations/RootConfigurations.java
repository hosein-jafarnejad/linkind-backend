package com.hosmos.linkind.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:springSecurityConfig.xml")

@Import(value = {SecurityConfigurations.class, MvcConfigurations.class})
public class RootConfigurations {
}
