package com.x5.historyparser.exception;

import lombok.Getter;

@Getter
public class ExceptSupport extends RuntimeException {
    private final String code;

    public ExceptSupport(String code, String message) {
        super(message);
        this.code = code;
    }

}
