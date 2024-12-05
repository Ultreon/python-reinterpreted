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

public class PythonFloat extends PythonBuiltin implements Convertible {
    private double value;
    private boolean init = true;

    public PythonFloat(float value, Context parentContext) {
        super(((PythonStdLib) parentContext.getStdLib()).getBuiltins().getFloatClass());
        this.value = value;
    }

    public PythonFloat(double value, Context parentContext) {
        super(((PythonStdLib) parentContext.getStdLib()).getBuiltins().getFloatClass());
        this.value = value;
    }

    public PythonFloat(PythonClass theClass, Context parentContext) {
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
                    value = ((PythonInt) arg.ptr).getValue();
                } else if (arg.ptr instanceof PythonFloat) {
                    value = ((PythonFloat) arg.ptr).value;
                } else {
                    throw new InterpreterTypeException("Expected int, long, float, or double, not " + arg.ptr.getClass().getName());
                }
            } else {
                throw new InterpreterTypeException("Expected 0 or 1 arguments, not " + args.length);
            }
        }

        init = true;
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType == float.class) {
            return (float) value;
        } else if (targetType == double.class) {
            return value;
        } else if (targetType == Float.class) {
            return (float) value;
        } else if (targetType == Double.class) {
            return value;
        } else {
            throw new InterpreterTypeException("Expected " + targetType.getName() + ", not float or double");
        }
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return targetType == float.class || targetType == double.class || targetType == Float.class || targetType == Double.class;
    }

    public double getValue() {
        return value;
    }

    public static PythonValue __add__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value + other.value);
    }

    public static PythonValue __sub__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value - other.value);
    }

    public static PythonValue __mul__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value * other.value);
    }

    public static PythonValue __truediv__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value / other.value);
    }

    public static PythonValue __mod__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value % other.value);
    }

    public static PythonValue __pow__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, Math.pow(self.value, other.value));
    }

    public static PythonValue __neg__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return PythonValue.of(context, -self.value);
    }

    public static PythonValue __lt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value < other.value);
    }

    public static PythonValue __gt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value > other.value);
    }

    public static PythonValue __le__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value <= other.value);
    }

    public static PythonValue __ge__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value >= other.value);
    }

    public static PythonValue __eq__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value == other.value);
    }

    public static PythonValue __ne__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        PythonFloat other = context.get("other", PythonFloat.class);
        return PythonValue.of(context, self.value != other.value);
    }

    public static PythonValue __bool__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return PythonValue.of(context, self.value != 0);
    }

    public static PythonValue __str__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return PythonValue.of(context, String.valueOf(self.value));
    }

    public static PythonValue __repr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return PythonValue.of(context, "float(" + String.valueOf(self.value) + ")");
    }

    public static PythonValue __hash__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return PythonValue.of(context, Double.hashCode(self.value));
    }

    public static Value __floor__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, (int) Math.floor(self.value));
    }

    public static Value __ceil__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, (int) Math.ceil(self.value));
    }

    public static Value __round__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, (int) Math.round(self.value));
    }

    public static Value __trunc__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, (int) self.value);
    }

    public static Value __int__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, (int) self.value);
    }

    public static Value __float__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, self.value);
    }

    public static Value __complex__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, new PythonComplex(context, self.value, 0));
    }

    public static Value __abs__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, Math.abs(self.value));
    }

    public static Value __len__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, Double.toString(self.value).length());
    }

    public static Value __pos__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonFloat self = PythonFloat.getSelf(stringValueMap, values, PythonFloat.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected float, not " + values[0].ptr.getClass().getName());
        }

        return Value.of(context, self.value < 0 ? -self.value : self.value);
    }
}
