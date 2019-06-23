package com.hosmos.linkind.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:springSecurityConfig.xml")
@Import(value = {TransactionConfigurations.class, SecurityConfigurations.class, BatchConfigurations.class})
public class RootConfigurations {

}
