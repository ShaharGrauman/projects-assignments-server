package com.grauman.amdocs.errors.custom;

public class NotContentExistException extends RuntimeException {
	public NotContentExistException(String message){
        super(message);
    }
}
