package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class InterpreterNotImplementedException extends InterpreterException {
    public InterpreterNotImplementedException(String message, ParserRuleContext ctx) {
        super(message, ctx);
    }

    public InterpreterNotImplementedException(String message, ParseTree ctx) {
        super(message, ctx);
    }

    public InterpreterNotImplementedException(String s) {
        super(s);
    }
}
