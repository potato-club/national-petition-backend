package com.example.nationalpetition.config;


import com.example.nationalpetition.security.jwt.JwtAuthFilter;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.security.oauth2.CustomOAuth2Service;
import com.example.nationalpetition.security.oauth2.OAuth2SuccessHandler;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2Service customOAuth2UserService;
    private final TokenService tokenService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2Service customOAuth2UserService, TokenService tokenService, CorsConfigurationSource corsConfigurationSource) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.tokenService = tokenService;
        this.corsConfigurationSource = corsConfigurationSource;
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
                .cors().configurationSource(corsConfigurationSource);


        http
                .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }
}