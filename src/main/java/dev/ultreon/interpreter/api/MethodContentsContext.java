package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.obj.InterpMethod;
import dev.ultreon.interpreter.api.context.BaseContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import org.jetbrains.annotations.Nullable;

public class MethodContentsContext extends BaseContext implements ClassMethodContentsContext {
    private final Context context;
    @Nullable
    private final ScriptObject instance;
    private final InterpMethod method;

    public MethodContentsContext(@Nullable ScriptObject instance, InterpMethod method, Context context) {
        super(context);
        this.context = method.getParentContext();
        this.instance = instance;
        this.method = method;

        this.getSymbolTable().putAll(context.getSymbolTable().all());
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Context parentContext() {
        return context;
    }

    public InterpMethod getMethod() {
        return method;
    }

    @Override
    public @Nullable ScriptObject getInstance() {
        return instance;
    }
}
