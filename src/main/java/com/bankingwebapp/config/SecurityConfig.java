package com.bankingwebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf().disable() // ✅ disable CSRF for APIs
            .authorizeRequests()
                .requestMatchers("home", "/default", "/index", "/", "/users").permitAll()  // Allow access to these paths
                .requestMatchers("/api/**").permitAll() 
                .requestMatchers("/api/users").permitAll() 
                //.anyRequest().authenticated()  // Require authentication for any other request
            .and()
            .formLogin()
                .loginPage("/login")  // Custom login page
                .permitAll()  // Allow everyone to access the login page
            .and()
            .logout()
                .permitAll();  // Allow everyone to log out

        return http.build();
    }
}
