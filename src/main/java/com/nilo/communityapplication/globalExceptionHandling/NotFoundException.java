package com.nilo.communityapplication.globalExceptionHandling;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
