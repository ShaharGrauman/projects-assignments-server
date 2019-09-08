package com.grauman.amdocs.errors.custom;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
