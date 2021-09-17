package com.example.nationalpetition.config;

import com.example.nationalpetition.security.oauth2.CustomOAuth2Service;
import com.example.nationalpetition.security.oauth2.OAuth2SuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2Service customOAuth2UserService;

    public SecurityConfig(OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2Service customOAuth2UserService) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                    .authorizeRequests()
                    .anyRequest().permitAll()

                .and()
                    .oauth2Login()
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint().userService(customOAuth2UserService);
    }
}