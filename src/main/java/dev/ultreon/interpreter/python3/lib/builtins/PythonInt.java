package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.python3.PythonValue;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PythonInt extends PythonBuiltin implements Convertible {
    private long value;
    private boolean init = true;

    public PythonInt(long value, Context parentContext) {
        super(((PythonStdLib) parentContext.getStdLib()).getBuiltins().getIntClass());
        this.value = value;
    }

    public PythonInt(PythonClass theClass, Context parentContext) {
        super(theClass);
        this.value = 0;
        this.init = false;
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Value[] args = visit.args();
        if (args.length != 0) {
            if (args.length == 1) {
                Value arg = args[0];
                if (arg.ptr instanceof PythonInt) {
                    value = ((PythonInt) arg.ptr).value;
                } else if (arg.ptr instanceof PythonFloat) {
                    value = (long) ((PythonFloat) arg.ptr).getValue();
                } else {
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + arg.ptr.getClass().getName());
                }
            }
        }

        init = true;
    }

    public static PythonInt of(Context context, int value) {
        return new PythonInt(value, context);
    }

    public static PythonInt of(Context context, byte value) {
        return new PythonInt(value, context);
    }

    public static PythonInt of(Context context, short value) {
        return new PythonInt(value, context);
    }

    public static PythonInt of(Context context, char value) {
        return new PythonInt(value, context);
    }

    public static PythonInt of(Context context, float value) {
        return new PythonInt((long) value, context);
    }

    public static PythonInt of(Context context, double value) {
        return new PythonInt((long) value, context);
    }

    public static PythonInt of(Context context, long value) {
        return new PythonInt(value, context);
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType == int.class) {
            return (int) value;
        } else if (targetType == long.class) {
            return value;
        } else if (targetType == byte.class && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return (byte) value;
        } else if (targetType == short.class && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return (short) value;
        } else if (targetType == Integer.class && value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
            return (int) value;
        } else if (targetType == Long.class) {
            return value;
        } else if (targetType == Byte.class && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return (byte) value;
        } else if (targetType == Short.class && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return (short) value;
        } else {
            throw new InterpreterTypeException("Cannot convert to " + targetType);
        }
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return (targetType == int.class && value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) ||
                (targetType == long.class) ||
                (targetType == byte.class && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) ||
                (targetType == short.class && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) ||
                (targetType == Integer.class && value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) ||
                (targetType == Long.class) ||
                (targetType == Byte.class && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) ||
                (targetType == Short.class && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE);
    }

    public static PythonValue __add__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() + pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() + pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() + convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __sub__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() - pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() - pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() - convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __mul__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() * pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() * pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() * convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __truediv__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() / pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() / pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() / convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __floordiv__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, Math.floorDiv(self.getValue(), pythonInt.value));
            case Convertible convertible ->
                    PythonValue.of(context, Math.floorDiv(self.getValue(), convertible.castedToJava(context, long.class)));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __repr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, "int(" + self.getValue() + ")");
    }

    public static PythonValue __str__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, String.valueOf(self.getValue()));
    }

    public static PythonValue __bool__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, self.getValue() != 0);
    }

    public static PythonValue __lt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() < pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() < pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() < convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __gt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() > pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() > pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() > convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __le__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() <= pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() <= pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() <= convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __ge__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() >= pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() >= pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() >= convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __eq__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() == pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() == pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() == convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __ne__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() != pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() != pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() != convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __mod__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() % pythonInt.value);
            case PythonFloat pythonFloat -> PythonValue.of(context, self.getValue() % pythonFloat.getValue());
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() % convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __pow__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, (long) Math.pow(self.getValue(), pythonInt.value));
            case PythonFloat pythonFloat -> PythonValue.of(context, Math.pow(self.getValue(), pythonFloat.getValue()));
            case Convertible convertible ->
                    PythonValue.of(context, Math.pow(self.getValue(), convertible.castedToJava(context, long.class)));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __lshift__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() << pythonInt.value);
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() << convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __rshift__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() >> pythonInt.value);
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() >> convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __and__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() & pythonInt.value);
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() & convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __or__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() | pythonInt.value);
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() | convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }


    public static PythonValue __xor__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        Object other = context.get("other").ptr;
        return switch (other) {
            case PythonInt pythonInt -> PythonValue.of(context, self.getValue() ^ pythonInt.value);
            case Convertible convertible ->
                    PythonValue.of(context, self.getValue() ^ convertible.castedToJava(context, long.class));
            case null, default ->
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + other.getClass().getName());
        };
    }

    public static PythonValue __neg__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, -self.getValue());
    }

    public static PythonValue __pos__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, self.getValue() < 0 ? -self.getValue() : self.getValue());
    }

    public static PythonValue __abs__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, Math.abs(self.getValue()));
    }

    public static PythonValue __invert__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, ~self.getValue());
    }

    public static PythonValue __round__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, Math.round(self.getValue()));
    }

    public static PythonValue __trunc__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
//        return PythonValue.of(context, Math.truncate(self.getValue()));
        throw new InterpreterNotImplementedException("Not implemented");
    }

    public static PythonValue __floor__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, (int) Math.floor(self.getValue()));
    }

    public static PythonValue __ceil__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, (int) Math.ceil(self.getValue()));
    }

    public static PythonValue __len__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, 1);
    }

    public static PythonValue __int__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, self.getValue());
    }

    public static PythonValue __float__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, self.getValue());
    }

    public static PythonValue __complex__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonInt self = PythonObject.getSelf(stringValueMap, values, PythonInt.class);
        return PythonValue.of(context, new PythonComplex(context, self.getValue(), 0));
    }
}
