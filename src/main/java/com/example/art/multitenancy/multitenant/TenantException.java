package com.example.art.multitenancy.multitenant;

public class TenantException extends RuntimeException{
    public TenantException(String message){
        super(message);
    }
}
