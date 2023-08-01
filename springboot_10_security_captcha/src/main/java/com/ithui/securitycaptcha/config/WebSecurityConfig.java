package com.ithui.securitycaptcha.config;

import com.ithui.securitycaptcha.filter.CaptchaVerifyFilter;
import com.ithui.securitycaptcha.handler.JsonAuthenticationFailureHandler;
import com.ithui.securitycaptcha.handler.LoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Log4j2
public class WebSecurityConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(https->https
                .requestMatchers("/login.html","/css/login.css").permitAll()
                .anyRequest()
                .authenticated()
        );

        log.info("websecurity1...");

        http.formLogin(https->https
                .loginPage("/static/login.html")
                .loginProcessingUrl("/custom/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new JsonAuthenticationFailureHandler())
        );

        log.info("websecurity2...");

        http.httpBasic();
        //在某个过滤器之前添加一个过滤器

        log.info("websecurity3...");

        http.addFilterBefore(new CaptchaVerifyFilter(new JsonAuthenticationFailureHandler(), stringRedisTemplate),
                UsernamePasswordAuthenticationFilter.class);

        log.info("websecurity4...");
        http.csrf().disable();


        return http.build();
    }
}
