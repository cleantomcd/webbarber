package com.webbarber.webbarber.exception;

public class BarberNotFoundException extends RuntimeException {
    public BarberNotFoundException(String message) {
        super(message);
    }
}
