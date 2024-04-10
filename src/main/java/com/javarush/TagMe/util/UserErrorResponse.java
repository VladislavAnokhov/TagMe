package com.javarush.TagMe.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserErrorResponse extends RuntimeException{
    private String message;
    public Long timestamp;
}
