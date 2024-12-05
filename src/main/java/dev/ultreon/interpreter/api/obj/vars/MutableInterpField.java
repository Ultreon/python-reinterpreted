package dev.ultreon.interpreter.api.obj.vars;

import dev.ultreon.interpreter.api.obj.InterpField;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Type;
import org.jetbrains.annotations.Nullable;

public class MutableInterpField extends InterpField {
    private Object value;

    public MutableInterpField(String name, @Nullable Type type, Object value) {
        super(name, type, false, false);
        this.value = value;
    }

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
