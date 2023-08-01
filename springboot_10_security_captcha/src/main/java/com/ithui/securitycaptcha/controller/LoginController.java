package com.ithui.securitycaptcha.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.ithui.securitycaptcha.result.Result;
import com.ithui.securitycaptcha.vo.CaptchaVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
//@RequestMapping("/login")
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public Result<CaptchaVO> getCaptcha(HttpServletResponse response){
        //定义验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = lineCaptcha.getCode();//获取验证码

        //获取验证码图片
        String imageBase64Data = lineCaptcha.getImageBase64Data();
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setId(UUID.randomUUID().toString());
        captchaVO.setImageCoding(imageBase64Data);
        //将验证码保存到缓存中,并设置有效时间为10分钟
        stringRedisTemplate.opsForValue().set(captchaVO.getId(),code,10, TimeUnit.MINUTES);
        return Result.success(captchaVO);
    }

    @GetMapping("/logins")
    public String getLoin(){

        return "static/login";
    }
}
