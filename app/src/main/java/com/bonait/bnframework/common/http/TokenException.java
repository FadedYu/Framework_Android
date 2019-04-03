package com.bonait.bnframework.common.http;

/**
 * Created by LY on 2019/4/3.
 */
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = 8394950517718638426L;


    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }
}
