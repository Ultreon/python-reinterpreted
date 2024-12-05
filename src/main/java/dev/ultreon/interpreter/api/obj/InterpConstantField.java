package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Type;

public class InterpConstantField extends InterpField {
    private Object value;

    public InterpConstantField(String name, Type type, Object value) {
        super(name, type, true, true);
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
}
