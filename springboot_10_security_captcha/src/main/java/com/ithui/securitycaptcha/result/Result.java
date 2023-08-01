package com.ithui.securitycaptcha.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    public static  <T> Result success(T object){
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }
}
