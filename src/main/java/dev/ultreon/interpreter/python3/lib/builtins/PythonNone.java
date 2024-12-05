package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.python3.InterpreterNoAttrException;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

public class PythonNone extends PythonBuiltin implements Convertible {
    private PythonNone(Context context) {
        super(((PythonStdLib) context.getStdLib()).getBuiltins().getNoneClass());
    }

    public PythonNone(PythonClass theClass, Context context) {
        super(theClass);
    }

    @Override
    public Value get(String name) {
        throw new InterpreterNoAttrException("Member " + name + " not found.");
    }

    @Override
    public Value set(String name, Value value) {
        throw new InterpreterNoAttrException("Member " + name + " not found.");
    }

    @Override
    public void remove(String name) {
        throw new InterpreterNoAttrException("Member " + name + " not found.");
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        return null;
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return true;
    }
}
