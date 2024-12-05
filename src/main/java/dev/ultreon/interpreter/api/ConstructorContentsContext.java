package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.obj.ClassConstructor;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.BaseContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.context.ObjectContext;
import dev.ultreon.interpreter.api.symbols.Symbol;
import org.jetbrains.annotations.Nullable;

public class ConstructorContentsContext extends BaseContext implements ClassMethodContentsContext {
    private final ObjectContext instance;
    private final ClassConstructor constructor;

    public ConstructorContentsContext(ObjectContext instance, ClassConstructor constructor, @Nullable Context context) {
        super(context);
        this.instance = instance;
        this.constructor = constructor;
    }

    @Override
    public Symbol getSymbol(String text) {
        if (getSymbolTable().get(text) != null) {
            return getSymbolTable().get(text);
        } else if (instance.getSymbolTable().get(text) != null) {
            return instance.getSymbolTable().get(text);
        } else {
            throw new InterpreterError("Symbol table is non-existent");
        }
    }

    @Override
    public Context parentContext() {
        return instance;
    }

    @Override
    public ScriptObject getInstance() {
        return instance.getObject();
    }

    public ObjectContext getInstanceContext() {
        return instance;
    }

    public ClassConstructor getConstructor() {
        return constructor;
    }
}
