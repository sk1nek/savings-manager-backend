package me.mjaroszewicz.exceptions;

public class RegistrationException extends Exception{

    private final String message;

    public RegistrationException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
