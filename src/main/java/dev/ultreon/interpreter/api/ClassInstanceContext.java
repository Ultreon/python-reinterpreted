package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.obj.InterpField;
import dev.ultreon.interpreter.api.obj.InterpMethod;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.BaseContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.obj.vars.InterpVar;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClassInstanceContext extends BaseContext {
    private final ScriptObject instance;

    public ClassInstanceContext(@Nullable Context context, ScriptObject instance) {
        super(context);
        this.instance = instance;
    }

    public Context getParentContext() {
        return null;
    }

    public ScriptObject getInstance() {
        return instance;
    }

    public Object invoke(InterpMethod method, Value... args) {
        if (method.isStatic()) {
            return method.invoke(this, null, Collections.emptyMap(), args);
        }
        return method.invoke(this, instance, Collections.emptyMap(), args);
    }

    public Value get(String name) {
        return instance.get(name);
    }

    @Override
    public Context parentContext() {
        return instance.parentContext();
    }
}
