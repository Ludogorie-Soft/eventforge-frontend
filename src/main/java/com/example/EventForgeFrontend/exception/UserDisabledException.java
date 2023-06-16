package com.example.EventForgeFrontend.exception;

public class UserDisabledException extends RuntimeException{
    public UserDisabledException(String message){
        super(message);
    }
}
