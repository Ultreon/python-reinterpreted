package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Type;

import java.util.function.Function;

public class InterpInstanceField extends InterpField {
    private Function<ScriptObject, Object> value;

    public InterpInstanceField(String name, Type type) {
        super(name, type, false, false);
    }

    public InterpInstanceField(String name, Type type, Function<ScriptObject, Object> value) {
        super(name, type, false, false);
        this.value = value;
    }

    @Override
    public Object get(ScriptObject instance) {
        return instance.getField(name);
    }

    @Override
    public void forceSet(ScriptObject instance, Object value) {
        instance.setField(name, value);
    }

    public void set(ScriptObject instance, Object value) {
        instance.setField(name, value);
    }

    public Object getDefaultValue(ScriptObject instance) {
        if (value == null) {
            return null;
        }
        return value.apply(instance);
    }
}
