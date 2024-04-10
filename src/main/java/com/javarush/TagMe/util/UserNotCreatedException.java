package com.javarush.TagMe.util;

public class UserNotCreatedException extends RuntimeException {
    public  UserNotCreatedException (String msg){
        super(msg);
    }
}
