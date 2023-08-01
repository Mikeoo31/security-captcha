package com.ithui.securitycaptcha.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@Log4j2
public class ImageController {

    @GetMapping("/code/image")
    public void image(HttpServletResponse response) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = lineCaptcha.getCode();//获取验证码

        String imageBase64Data = lineCaptcha.getImageBase64Data();

        //获取验证码图片
        response.getWriter().write(imageBase64Data);
    }
}
