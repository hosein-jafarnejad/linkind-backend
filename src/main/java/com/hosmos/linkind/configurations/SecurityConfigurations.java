package ir.projects.learner.configurations;

import ir.projects.learner.security.AuthorityManager;
import ir.projects.learner.security.LoginUserDetailsService;
import ir.projects.learner.security.RestAuthenticationEntryPoint;
import ir.projects.learner.security.RestAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()

                // URL access management
                .authorizeRequests()
                .antMatchers("/user/new", "/user/activate/**").permitAll()
                .antMatchers("/accessnode/**").access("isAuthenticated() and authentication.principal.isAdmin()")
                .antMatchers("/user/**", "/role/**").authenticated()
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
        auth
                .userDetailsService(loginUserDetailsService()).passwordEncoder(bCryptPasswordEncoder());
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

    @Bean()
    @Order(1)
    public AuthorityManager authorityManager() {
        return new AuthorityManager();
    }

}
