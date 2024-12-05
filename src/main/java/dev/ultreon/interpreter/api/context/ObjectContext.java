package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.obj.*;
import dev.ultreon.interpreter.api.obj.vars.InterpVar;
import dev.ultreon.interpreter.api.obj.vars.VarBuilder;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Type;

import java.util.HashMap;
import java.util.Map;

public class ObjectContext extends BaseContext {
    private final Context thisContext;
    private final ScriptObject object;

    public ObjectContext(Context thisContext, ScriptObject object, Context parentContext) {
        super(parentContext);
        this.thisContext = thisContext;
        this.object = object;
    }

    @Override
    public Symbol getSymbol(String text) {
        Symbol symbol = thisContext.getSymbol(text);
        if (symbol != null) return symbol;

        if (parentContext != null) {
            symbol = parentContext.getSymbol(text);
            if (symbol != null) return symbol;
        }

        throw new InterpreterError("Unknown symbol: " + text);
    }

    public Context parentContext() {
        return parentContext;
    }

    public ScriptObject getObject() {
        return object;
    }
}
