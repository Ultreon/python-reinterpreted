package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

public class PythonBytes extends PythonBuiltin implements Convertible {
    private byte[] bytes;
    private boolean init = true;

    public PythonBytes(Context context, byte[] bytes) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getBytesClass());
        this.bytes = bytes;
    }

    public PythonBytes(Context context, int length) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getBytesClass());
        this.bytes = new byte[length];
    }

    public PythonBytes(Context context) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getBytesClass());
        this.bytes = new byte[0];
    }


    public PythonBytes(PythonClass theClass) {
        super(theClass);
        init = false;
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Value[] args = visit.args();
        if (args.length != 0) {
            if (args.length == 1) {
                Value arg = args[0];
                if (arg.ptr instanceof PythonBytes) {
                    bytes = ((PythonBytes) arg.ptr).bytes;
                } else if (arg.ptr instanceof PythonString) {
                    bytes = ((PythonString) arg.ptr).getValue().getBytes();
                } else {
                    throw new InterpreterTypeException("Expected bytes or string, not " + arg.ptr.getClass().getName());
                }
            }
        }

        init = true;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType == byte[].class) {
            return bytes;
        } else {
            throw new InterpreterTypeException("Expected bytes, not " + targetType.getName());
        }
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return targetType == byte[].class;
    }
}
