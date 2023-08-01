package com.ithui.securitycaptcha.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaVerifyException extends AuthenticationException {

    public CaptchaVerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaVerifyException(String msg) {
        super(msg);
    }
}

