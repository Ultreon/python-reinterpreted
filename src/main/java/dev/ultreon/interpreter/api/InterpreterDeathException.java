package dev.ultreon.interpreter.api;

public class InterpreterDeathException extends Error {
    public InterpreterDeathException(String message) {
        super(message);
    }

    public InterpreterDeathException() {
        super();
    }
}
