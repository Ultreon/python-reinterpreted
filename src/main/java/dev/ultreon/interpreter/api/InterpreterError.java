package dev.ultreon.interpreter.api;

import org.antlr.v4.runtime.ParserRuleContext;

public class InterpreterError extends Error {
    public InterpreterError(String message) {
        super(message);
    }

    public InterpreterError(String message, ParserRuleContext ctx) {
        super(message + " (" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + ")");
    }
}
