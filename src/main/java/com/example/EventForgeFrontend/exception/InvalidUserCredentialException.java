package com.example.EventForgeFrontend.exception;

public class InvalidUserCredentialException extends RuntimeException{
    public InvalidUserCredentialException(String message){
        super(message);
    }
}
