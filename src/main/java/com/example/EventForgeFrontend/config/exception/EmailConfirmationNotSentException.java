package com.example.EventForgeFrontend.config.exception;

public class EmailConfirmationNotSentException extends RuntimeException{
    public EmailConfirmationNotSentException(String message){
        super(message);
    }
}
