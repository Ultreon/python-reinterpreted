package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.BaseContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Symbol;
import org.jetbrains.annotations.Nullable;

public class Namespace extends BaseContext implements Symbol {
    private final String name;

    public Namespace(String name, @Nullable Context context) {
        super(context);
        this.name = name;
    }

    public Namespace(@Nullable Context context) {
        super(context);
        this.name = null;
    }

    public @Nullable String name() {
        return name;
    }
}
