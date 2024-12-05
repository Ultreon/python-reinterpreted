package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.Namespace;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.ClassLike;
import dev.ultreon.interpreter.api.ObjectLike;
import dev.ultreon.interpreter.python3.PythonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PythonModule extends Namespace implements ClassLike {
    public PythonModule(String name, @Nullable Context context) {
        super(name, context);
    }

    public PythonValue createPythonValue() {
        return new PythonValue(this);
    }

    @Override
    public Value cast(Value value) {
        if (value instanceof PythonValue pythonValue && pythonValue.get() instanceof PythonModule) {
            return value;
        }
        throw new InterpreterTypeException("Cannot cast " + value + " to " + this);
    }

    @Override
    public ObjectLike construct() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInstance(ObjectLike scriptObject) {
        return false;
    }

    @Override
    public Type asType() {
        return Type.class_(this);
    }

    @Override
    public List<ClassLike> parents() {
        return List.of(this);
    }

    public Value set(String text, Value value) {
        getSymbolTable().put(text, value);
        return value;
    }

    @Override
    public Context createInvokeContents() {
        throw new InterpreterException("Cannot invoke module");
    }

    @Override
    public @NotNull Context context() {
        return this;
    }
}
