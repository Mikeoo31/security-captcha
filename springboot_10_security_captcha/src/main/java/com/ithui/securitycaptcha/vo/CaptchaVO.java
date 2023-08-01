package com.ithui.securitycaptcha.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {

    //唯一id
    private String id;

    //图片编码
    private String imageCoding;
}
