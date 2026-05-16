package com.student.Dodo_Pizza_Project.exception;

public class InvalidProductPriceException extends RuntimeException{
    public InvalidProductPriceException(String message) {
        super(message);
    }
}
