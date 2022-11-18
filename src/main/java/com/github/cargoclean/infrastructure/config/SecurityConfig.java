package com.github.cargoclean.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.github.cargoclean.infrastructure.adapter.security.CargoSecurityAdapter.ROLE_CARGO_AGENT;
import static com.github.cargoclean.infrastructure.adapter.security.CargoSecurityAdapter.ROLE_CARGO_MANAGER;

/*
    References:
    ----------

    1. WebSecurityCustomizer, JavaDocs
    2. Spring Security Configuration, Java: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html
    3. Spring Security, override HttpSecurity example: https://github.com/spring-projects/spring-security-samples/blob/main/servlet/java-configuration/max-sessions/src/main/java/example/SecurityConfiguration.java
 */


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*
            Point of interest:
            -----------------
            We do not specify authorized requests for any of URLs here.
            It will be the use cases which will decide whether a user
            executing the use case is allowed or not to proceed.
         */


        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin();

        return http.build();
    }

    /*
        Set up users and roles for the application.
        Anonymous: can see arrivals report and track cargoes
        Agent: cannot create new locations and cannot route cargoes through Oceania
        Manager: can do anything
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
