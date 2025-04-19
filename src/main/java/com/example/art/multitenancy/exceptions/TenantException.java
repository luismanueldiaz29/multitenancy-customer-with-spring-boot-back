package com.example.art.multitenancy.exceptions;

public class TenantException extends RuntimeException{
    public TenantException(String message){
        super(message);
    }
}
