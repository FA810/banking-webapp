package com.bankingwebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/", "/index", "/css/**", "/login", "/users/login").permitAll()
                .anyRequest().permitAll()  // other pages accessible without login
            .and()
            .formLogin()
                .loginPage("/login")                      // login page URL
                .defaultSuccessUrl("/", true) // redirect on success
                .failureUrl("/login?error=true")           // redirect on fail with error flag
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            .and()
            .csrf().disable();

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                UserDetails user = userDetailsService.loadUserByUsername(username);

                if (user != null && user.getPassword().equals(password)) {
                    return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
                } else {
                    throw new AuthenticationException("Invalid credentials") {};
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }
}
