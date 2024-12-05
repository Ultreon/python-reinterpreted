package dev.ultreon.interpreter.api;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class InterpreterException extends RuntimeException {
    private final String message;
    public ParserRuleContext ctx;

    public InterpreterException(String message, ParserRuleContext ctx) {
        super(message + " (" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + ")");
        this.message = message;
        this.ctx = ctx;
    }

    public InterpreterException(String message, ParseTree ctx) {
        super(message);
        this.message = message;
        this.ctx = null;
    }

    public InterpreterException(String s) {
        super(s);
        this.message = s;
        this.ctx = null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
