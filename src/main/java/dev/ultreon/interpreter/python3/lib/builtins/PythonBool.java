package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

public class PythonBool extends PythonBuiltin implements Convertible {
    private boolean value;
    private boolean init = true;

    public PythonBool(PythonClass theClass, Context context) {
        super(theClass);
        this.value = false;
        this.init = false;
    }

    public PythonBool(boolean value, Context context) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getBoolClass());
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Value[] args = visit.args();
        if (args.length != 0) {
            if (args.length == 1) {
                Value arg = args[0];
                if (arg.ptr instanceof PythonBool) {
                    value = ((PythonBool) arg.ptr).value;
                } else if (arg.ptr instanceof PythonInt) {
                    value = ((PythonInt) arg.ptr).getValue() != 0;
                } else if (arg.ptr instanceof PythonFloat) {
                    value = ((PythonFloat) arg.ptr).getValue() != 0;
                } else if (arg.ptr instanceof PythonString) {
                    value = !((PythonString) arg.ptr).getValue().isEmpty();
                } else if (arg.ptr instanceof PythonNone) {
                    value = false;
                } else {
                    throw new InterpreterTypeException("Expected boolean, not " + arg.ptr.getClass().getName());
                }
            }
        }

        init = true;
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType == boolean.class) {
            return value;
        } else if (targetType == Boolean.class) {
            return value;
        } else {
            throw new InterpreterTypeException("Expected boolean, not " + targetType.getName());
        }
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return targetType == boolean.class || targetType == Boolean.class;
    }
}
