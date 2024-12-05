package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.ClassLike;
import dev.ultreon.interpreter.api.symbols.Value;

public class InterpClassRef {
    private final Context context;
    private String name;

    public InterpClassRef(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    public ClassLike getTheClass() {
        Context ctx = context;
        while (ctx != null) {
            Value value = ctx.get(name());
            if (!(value instanceof ClassLike theClass)) continue;
            return theClass;
        }
        throw new RuntimeException("Class isn't defined yet");
    }

    private String name() {
        return name;
    }
}
