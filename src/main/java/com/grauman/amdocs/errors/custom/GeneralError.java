package com.grauman.amdocs.errors.custom;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralError {
    private int status;
    private String message;
}
