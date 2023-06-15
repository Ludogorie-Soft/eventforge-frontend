package com.example.EventForgeFrontend.config.exception;

public class InvalidEmailConfirmationLinkException extends RuntimeException{

    public InvalidEmailConfirmationLinkException(String message){
        super(message);
    }
}
