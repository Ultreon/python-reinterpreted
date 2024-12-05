package dev.ultreon.interpreter.api.obj.vars;

import dev.ultreon.interpreter.api.symbols.Type;

public class MutableInterpVar extends InterpVar {
    public MutableInterpVar(String name, Type type, Object value) {
        super(name, type, value);
    }

    public MutableInterpVar(String name, Type type) {
        super(name, type, null);
    }

    public void forceSet(Object value) {
        this.value = value;
    }
}
