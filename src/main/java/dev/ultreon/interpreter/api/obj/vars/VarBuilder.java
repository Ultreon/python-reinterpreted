package dev.ultreon.interpreter.api.obj.vars;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ClassType;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.primitives.Primitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VarBuilder {
    private final String name;
    private @Nullable Type type;
    private Object value;
    private boolean constant = false;

    public VarBuilder(String name) {
        this.name = name;
    }

    public VarBuilder type(ClassType type) {
        this.type = Type.class_(type);
        return this;
    }

    public VarBuilder type(Type type) {
        this.type = type;
        return this;
    }

    public VarBuilder type(Primitive type, @NotNull Context context) {
        this.type = Type.primitive(type, context);
        return this;
    }

    public VarBuilder value(Object value) {
        this.value = value;
        return this;
    }

    public VarBuilder constant() {
        this.constant = true;
        return this;
    }

    public InterpVar build() {
        if (constant) {
            return new InterpVar(name, type, value);
        }
        return new MutableInterpVar(name, type, value);
    }
}
