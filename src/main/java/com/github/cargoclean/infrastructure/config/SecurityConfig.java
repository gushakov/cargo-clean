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

    1. WebSecurityCustomizer, JavaDoc
    2. Spring Security Configuration, Java: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html
    3. Spring Security, override HttpSecurity example: https://github.com/spring-projects/spring-security-samples/blob/main/servlet/java-configuration/max-sessions/src/main/java/example/SecurityConfiguration.java
    4. Spring Security Documentation, Multiple security configurations: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#_multiple_httpsecurity
    5. Spring, password encoders: https://mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/
    6. Spring, "org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder", JavaDoc
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
                User.withUsername("agent")
                        .password("{bcrypt}$2a$10$Zxwpz/RLcEo.S/PkO99gHOTbRaI/m3SZtfWzKYvRavFmD6XRDDjwi") // "test" (encrypted)
                        .authorities(ROLE_CARGO_AGENT)
                        .build());

        userDetailsManager.createUser(
                User.withUsername("manager")
                        .password("{bcrypt}$2a$10$XNr3hwkgDpNPEFtQl4q6E.xS92OEwiOd3tx0/4ovNMkw1mIXeoAW6") // "test" (encrypted)
                        .authorities(ROLE_CARGO_AGENT, ROLE_CARGO_MANAGER)
                        .build());

        return userDetailsManager;

    }


}
