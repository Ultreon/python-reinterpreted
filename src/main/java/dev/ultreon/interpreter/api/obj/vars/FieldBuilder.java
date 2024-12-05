package dev.ultreon.interpreter.api.obj.vars;

import dev.ultreon.interpreter.api.obj.InterpConstantField;
import dev.ultreon.interpreter.api.obj.InterpField;
import dev.ultreon.interpreter.api.obj.InterpInstanceField;
import dev.ultreon.interpreter.api.obj.InterpStaticField;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Type;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FieldBuilder {
    private final String name;
    private @Nullable Type type;
    private Object value;
    private boolean staticField = false;
    private boolean constant = false;

    public FieldBuilder(String name) {
        this.name = name;
    }

    public FieldBuilder type(@Nullable Type type) {
        this.type = type;
        return this;
    }

    public FieldBuilder value(Object value) {
        this.value = value;
        return this;
    }

    public FieldBuilder valueTemplate(Function<ScriptObject, Object> value) {
        this.value = value;
        return this;
    }

    public FieldBuilder constant() {
        this.constant = true;
        return this;
    }

    public FieldBuilder staticField() {
        this.staticField = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public InterpField build() {
        if (staticField) {
            return new InterpStaticField(name, type);
        }
        if (constant) {
            return new InterpConstantField(name, type, value);
        }
        return new InterpInstanceField(name, type, (Function<ScriptObject, Object>) value);
    }
}
