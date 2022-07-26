package com.kamillo.task.scheduler.security;

import com.kamillo.task.scheduler.security.users.repo.CustomeOtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomeOtpRepo customeOtpRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("customeUserServiceImpl")
    private UserDetailsService userDetailsService;

    public SecurityConfig( CustomeOtpRepo customeOtpRepo, PasswordEncoder passwordEncoder) {

        this.customeOtpRepo = customeOtpRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/public/**")
                    .authorizeRequests().anyRequest().permitAll();
    }

}
