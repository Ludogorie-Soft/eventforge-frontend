package com.example.EventForgeFrontend.exception;

public class InvalidEmailConfirmationLinkException extends RuntimeException{

    public InvalidEmailConfirmationLinkException(String message){
        super(message);
    }
}
