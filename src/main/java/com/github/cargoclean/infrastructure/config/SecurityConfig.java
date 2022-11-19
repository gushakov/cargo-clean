package com.github.cargoclean.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.github.cargoclean.infrastructure.adapter.security.CargoSecurityAdapter.ROLE_CARGO_AGENT;
import static com.github.cargoclean.infrastructure.adapter.security.CargoSecurityAdapter.ROLE_CARGO_MANAGER;
import static org.springframework.security.config.Customizer.withDefaults;

/*
    References:
    ----------

    1. WebSecurityCustomizer, JavaDocs
    2. Spring Security Configuration, Java: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html
    3. Spring Security, override HttpSecurity example: https://github.com/spring-projects/spring-security-samples/blob/main/servlet/java-configuration/max-sessions/src/main/java/example/SecurityConfiguration.java
    4. Spring Security Documentation, Multiple security configurations: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#_multiple_httpsecurity
 */


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
        Point of interest:
        -----------------
        This is for illustration only. We shall do all security checks
        exclusively in the use cases. So we do not require any
        authorizations for any requests here. Normally, some
        restrictions should be configured for additional security.
     */

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        // security profile for REST endpoint (record handling events),
        // uses HTTP Basic authentication

        http.csrf().disable()
                .antMatcher("/recordEvent/**")
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {

        // security profile for the web application, uses standard
        // form-based login

        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/");

        return http.build();
    }

    /*
        Set up users and roles for the application.
        Anonymous:  can see arrivals report
        Agent:      can book cargoes, can route cargoes, can track cargos
        Manager:    can do anything
     */

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(
                User.withDefaultPasswordEncoder()
                        .username("agent")
                        .password("test")
                        .authorities(ROLE_CARGO_AGENT)
                        .build());

        userDetailsManager.createUser(
                User.withDefaultPasswordEncoder()
                        .username("manager")
                        .password("test")
                        .authorities(ROLE_CARGO_AGENT, ROLE_CARGO_MANAGER)
                        .build());

        return userDetailsManager;

    }


}
