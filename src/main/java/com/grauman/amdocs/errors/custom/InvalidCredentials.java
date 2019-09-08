package com.grauman.amdocs.errors.custom;

public class InvalidCredentials extends RuntimeException{
    public InvalidCredentials(String message) {
        super(message);
    }
}
