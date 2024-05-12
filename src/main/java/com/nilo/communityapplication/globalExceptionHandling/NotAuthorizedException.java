package com.nilo.communityapplication.globalExceptionHandling;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(String message){
        super(message);
    }
}
