package com.ithui.securitycaptcha.filter;

import com.ithui.securitycaptcha.exception.CaptchaVerifyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Log4j2
public class CaptchaVerifyFilter extends GenericFilterBean {

    //请求匹配器
    private static AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/custom/login","POST");

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //相当于注入实现类JsonAuthenticationFailureHandler
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    public CaptchaVerifyFilter(AuthenticationFailureHandler authenticationFailureHandler,
                               StringRedisTemplate stringRedisTemplate){
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.stringRedisTemplate = stringRedisTemplate;
        log.info("websecurity5...");
    }
//    public CaptchaVerifyFilter(){}


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //首先判断请求路径是否匹配
        if(ANT_PATH_REQUEST_MATCHER.matches((HttpServletRequest) request)){
            try{
                //获取请求参数
                String formCaptchaKey = request.getParameter("FORM_CAPTCHA_KEY");
                String formCaptchaIdKey = request.getParameter("FORM_CAPTCHA_ID_KEY");
                if(formCaptchaKey == null || formCaptchaIdKey == null){
                    throw new CaptchaVerifyException("验证码为空不能为空！");
                }
                String captchaData = stringRedisTemplate.opsForValue().get(formCaptchaIdKey);
                if(captchaData == null){
                    throw new CaptchaVerifyException("验证码已过期");
                }
                if(!captchaData.equals(formCaptchaKey)){
                    throw new CaptchaVerifyException("验证码错误");
                }
                //验证通过，放行
                filterChain.doFilter(request,response);
            }catch (AuthenticationException exception){
                authenticationFailureHandler.onAuthenticationFailure((HttpServletRequest) request,(HttpServletResponse) response,exception);
            }
        }else {
            filterChain.doFilter(request,response);
        }

    }
}
