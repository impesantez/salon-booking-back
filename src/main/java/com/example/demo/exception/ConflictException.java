package com.example.demo.exception;


@SuppressWarnings("serial")
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
