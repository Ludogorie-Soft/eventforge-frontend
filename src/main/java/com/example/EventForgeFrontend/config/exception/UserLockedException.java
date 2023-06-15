package com.example.EventForgeFrontend.config.exception;

public class UserLockedException extends RuntimeException{

    public UserLockedException(String message){
        super(message);
    }
}
