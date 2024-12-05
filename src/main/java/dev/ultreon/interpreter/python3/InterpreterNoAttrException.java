package dev.ultreon.interpreter.python3;

public class InterpreterNoAttrException extends RuntimeException {
    public InterpreterNoAttrException(String s) {
        super(s);
    }

    public InterpreterNoAttrException(String s, Throwable cause) {
        super(s, cause);
    }

    public InterpreterNoAttrException(Throwable cause) {
        super(cause);
    }
}
