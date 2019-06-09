package com.hosmos.linkind.configurations;

import com.hosmos.linkind.security.RestAuthenticationEntryPoint;
import com.hosmos.linkind.security.LoginUserDetailsService;
import com.hosmos.linkind.security.RestAuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//
//                .exceptionHandling()
//                .authenticationEntryPoint(restAuthenticationEntryPoint())
//                .and()

                // URL access management
                .authorizeRequests()
                .antMatchers("/user/new", "/user/activate/**").permitAll()
//                .antMatchers("/accessnode/**").access("isAuthenticated() and authentication.principal.isAdmin()")
//                .antMatchers("/user/**", "/role/**").authenticated()
//                .access("hasAnyRole('ROLE_ADMIN_LEVEL_ONE','ROLE-ADMIN_LEVEL_TWO') or authentication.principal.isAdmin()")

                .anyRequest().permitAll()
                .and()

                // Login
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password")
                .successHandler(restAuthenticationSuccessHandler())
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .permitAll()
                .and()

                // Logout settings
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginUserDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    @Order(1004)
    public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginUserDetailsService loginUserDetailsService() {
        return new LoginUserDetailsService();
    }

    @PostConstruct
    public void init() {
        logger.info("Security initialized.");
    }

}
