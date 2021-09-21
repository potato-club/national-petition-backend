package com.example.nationalpetition.config;

import com.example.nationalpetition.security.jwt.JwtAuthFilter;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.security.oauth2.CustomOAuth2Service;
import com.example.nationalpetition.security.oauth2.OAuth2SuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2Service customOAuth2UserService;
    private final TokenService tokenService;

    public SecurityConfig(OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2Service customOAuth2UserService, TokenService tokenService) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.tokenService = tokenService;
    }

    @Override
    public void configure(WebSecurity web)
    {
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
                        .userInfoEndpoint().userService(customOAuth2UserService);

        http
                .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }
}