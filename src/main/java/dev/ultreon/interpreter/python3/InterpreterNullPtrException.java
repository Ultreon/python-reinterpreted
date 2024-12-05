package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.Interpreter;

public class InterpreterNullPtrException extends RuntimeException {
    public InterpreterNullPtrException(Interpreter interpreter, String message) {
        super(message.replace("${type}", interpreter.getClassSymbolName()));
    }

    public InterpreterNullPtrException(String s) {
        super(s);
    }
}
