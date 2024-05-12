package com.nilo.communityapplication.globalExceptionHandling;

public class WrongInputTypeException extends RuntimeException {
    public WrongInputTypeException(String message){
        super(message);
    }
}
