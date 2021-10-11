package com.example.nationalpetition.config;

import com.example.nationalpetition.security.oauth2.CustomOAuth2Service;
import com.example.nationalpetition.security.oauth2.OAuth2FailureHandler;
import com.example.nationalpetition.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;


@RequiredArgsConstructor
@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2Service customOAuth2UserService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .authorizeRequests().anyRequest().permitAll()

                .and()
                    .oauth2Login()
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler)
                    .userInfoEndpoint().userService(customOAuth2UserService);

        http
                .cors().configurationSource(corsConfigurationSource);
    }

}