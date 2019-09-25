package com.grauman.amdocs.errors.custom;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class GeneralError {
    private HttpStatus status;
    private String message;
}
