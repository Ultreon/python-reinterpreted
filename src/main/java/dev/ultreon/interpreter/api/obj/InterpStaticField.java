package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Type;

public class InterpStaticField extends InterpField {
    public Object value;

    public InterpStaticField(String name, Type type) {
        super(name, type, true, false);
    }

    public InterpStaticField(String name, Type type, Object value) {
        super(name, type, true, false);
        this.value = value;
    }

    @Override
    public Object get(ScriptObject instance) {
        return value;
    }

    @Override
    public void forceSet(ScriptObject instance, Object value) {
        super.forceSet(instance, value);
        this.value = value;
    }

    public void set(Object value) {
        this.value = value;
    }
}
