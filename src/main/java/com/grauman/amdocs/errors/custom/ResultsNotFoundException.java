package com.grauman.amdocs.errors.custom;

public class ResultsNotFoundException extends RuntimeException{

    public ResultsNotFoundException(String message){
        super(message);
    }
}
