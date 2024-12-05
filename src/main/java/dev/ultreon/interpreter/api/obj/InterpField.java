package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.symbols.Type;

public abstract class InterpField implements InterpClassMember {
    public final String name;
    public final Type type;
    public final boolean isStatic;
    public final boolean readOnly;

    public InterpField(String name, Type type, boolean isStatic, boolean readOnly) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.readOnly = readOnly;
    }

    public abstract Object get(ScriptObject instance);

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return type;
    }

    public void forceSet(ScriptObject instance, Object value) {
        if (readOnly) {
            throw new InterpreterException("Cannot assign to read-only variable.", null);
        }
    }
}
