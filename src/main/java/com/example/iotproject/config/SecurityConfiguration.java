package com.example.iotproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*To tell Spring which config we want to use in order to make all the process work(filter,UserDetailsService
Validation,updating context...)but we are missing the BINDING*/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    /*At the application startup Spring Security will look for Bean of type SecurityFilerChain which is a Bean
    * responsible for configuring all the http security of our application*/
    private final JwtAuthenticationFilter  jwtAuthFiler;/*final to be auto injected by Spring*/
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*within the security we can choose what are the urls and the paths that we want to secure
        * but within every app we always have a whitelist:some endpoints do not require any authentication
        * like if we access login/create accout also when we want to login we dont need to pass a token
        * cuz we do not have one yet*/
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**")/*whiteList this list*/
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()/*configure session management : every request should be authenticated, we shouldn't store the
                authentication state or the session state*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)/*like that Spring will create a new session
                for each request*/
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }


}
