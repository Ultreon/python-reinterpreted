package dev.ultreon.interpreter.api.obj.vars;

import dev.ultreon.interpreter.api.obj.InterpField;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Symbol;

import javax.annotation.Nullable;

@Deprecated
public class InterpVar implements Symbol {
    private final String name;
    private final Type type;
    private ScriptObject instance;
    private InterpField field;
    protected Object value;

    public InterpVar(String name, @Nullable Type type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public InterpVar(ScriptObject instance, InterpField field) {
        this.name = field.name();
        this.type = field.type();
        this.field = field;
        this.instance = instance;
    }

    public String name() {
        return name;
    }

    public @Nullable Type type() {
        return type;
    }

    public Object get() {
        if (field != null) {
            return field.get(instance);
        }
        return value;
    }

    public void print() {
        System.out.println(name + ": " + value);
    }
}
