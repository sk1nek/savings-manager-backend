package me.mjaroszewicz.exceptions;

public class UserNotFoundException extends Exception{

    private final String message;

    public UserNotFoundException(String msg){
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
