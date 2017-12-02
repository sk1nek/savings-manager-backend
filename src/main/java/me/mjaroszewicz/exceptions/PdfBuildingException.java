package me.mjaroszewicz.exceptions;

public class PdfBuildingException extends Exception {

    private String message;

    public PdfBuildingException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
