package com.example.EventForgeFrontend.exception;

public class EmailConfirmationNotSentException extends RuntimeException{
    public EmailConfirmationNotSentException(String message){
        super(message);
    }
}
