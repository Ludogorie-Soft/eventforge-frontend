package com.example.EventForgeFrontend.config.exception;

public class InvalidUserCredentialException extends RuntimeException{
    public InvalidUserCredentialException(String message){
        super(message);
    }
}
