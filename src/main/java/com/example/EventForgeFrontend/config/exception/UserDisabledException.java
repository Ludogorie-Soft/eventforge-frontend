package com.example.EventForgeFrontend.config.exception;

public class UserDisabledException extends RuntimeException{
    public UserDisabledException(String message){
        super(message);
    }
}
