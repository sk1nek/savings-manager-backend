package me.mjaroszewicz.exceptions;

public class StorageException extends Exception {

    private final String message;

    public StorageException(String msg){
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
