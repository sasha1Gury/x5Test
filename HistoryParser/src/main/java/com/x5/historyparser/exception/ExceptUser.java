package com.x5.historyparser.exception;

public class ExceptUser extends Exception {
    private final String messageUser;

    public ExceptUser(String messageUser) {
        this.messageUser = messageUser;
    }
}
