package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.api.HostObject;
import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FuncType;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.builtins.*;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PythonValue extends Value {
    public static final PythonValue None = new PythonValue(null);

    public PythonValue(Object value) {
        super(value);
        if (value instanceof PythonValue) {
            throw new InterpreterException("Cannot create PythonValue from PythonValue");
        }
        if (None == null) return;
        if (!(value instanceof PythonObject) && !(value instanceof PythonClass) && !(value instanceof PythonModule) && !(value instanceof HostObject)) {
            throw new InterpreterException("Cannot create PythonValue from " + value.getClass());
        }
    }

    @Override
    public boolean isNull() {
        return super.isNull() || this.ptr == null || this.ptr instanceof Value value && value.isNull();
    }

    @Override
    public String toString() {
        if (this.ptr == null || this.ptr instanceof Value value && value.isNull()) {
            return "NoneType";
        }

        if (this.ptr instanceof PythonObject object) {
            return object.toString();
        } else if (this.ptr instanceof PythonClass clazz) {
            try {
                return clazz.toString();
            } catch (Exception e) {
                return super.toString();
            }
        } else if (this.ptr instanceof PythonModule module) {
            return module.toString();
        } else if (this.ptr instanceof HostObject object) {
            return object.toString();
        } else if (this.ptr == null) {
            return "NoneType";
        } else {
            return super.toString();
        }
    }

    @Override
    public PythonValue xor(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__xor__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot xor because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue or(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__or__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot or because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue and(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__and__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot and because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue plus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__add__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot plus because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue negate() {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__neg__").invoke(object.thisContext(), Map.of("self", this));
        } else {
            throw new InterpreterException("Cannot negate because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue shl(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__shift__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot shift because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue shr(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__rshift__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot shift because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue times(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__mul__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot times because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue divide(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__div__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot divide because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue floordiv(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__floordiv__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot floordiv because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue rem(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__mod__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot rem because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue unaryPlus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__pos__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot unaryPlus because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public PythonValue unaryMinus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return (PythonValue) object.get("__neg__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot unaryMinus because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public Object pow(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("__pow__").invoke(object.thisContext(), Map.of("self", this), visit);
        } else {
            throw new InterpreterException("Cannot pow because this is a " + (this.ptr == null ? null : this.ptr.getClass()));
        }
    }

    public static PythonValue of(Context context, Object ptr) {
        if (ptr == null) return None;
        if (ptr instanceof PythonValue value) return value;
        if (ptr instanceof HostObject object) return new PythonValue(object);
        if (ptr instanceof PythonObject object) return object.createPythonValue();
        if (ptr instanceof PythonClass object) return object.createPythonValue();
        if (ptr instanceof PythonModule object) return object.createPythonValue();
        if (ptr instanceof FuncType object)
            return PythonValue.of(context, new PythonFunction(object, object.parameters(), context));
        if (ptr instanceof Value object) {
            if (object.isNull()) {
                return None;
            }
            if (object.ptr instanceof PythonValue) {
                return (PythonValue) object.ptr;
            }
            return PythonValue.of(context, object.ptr);
        }
        if (ptr instanceof String object) {
            return new PythonValue(new PythonString(object, context));
        }
        if (ptr instanceof Byte || ptr instanceof Short || ptr instanceof Integer || ptr instanceof Long) {
            return new PythonValue(new PythonInt(((Number) ptr).longValue(), context));
        }
        if (ptr instanceof Float || ptr instanceof Double) {
            return new PythonValue(new PythonFloat(((Number) ptr).doubleValue(), context));
        }
        if (ptr instanceof Boolean) {
            return new PythonValue(new PythonBool((Boolean) ptr, context));
        }
        if (ptr instanceof Character) {
            return new PythonValue(new PythonString(String.valueOf(ptr), context));
        }
        if (ptr instanceof BigInteger) {
            return new PythonValue(new PythonInt(((BigInteger) ptr).longValue(), context));
        }
        if (ptr instanceof BigDecimal) {
            return new PythonValue(new PythonFloat(((BigDecimal) ptr).doubleValue(), context));
        }

        if (ptr instanceof List<?>)
            return new PythonValue(new PythonList(context, ((List<?>) ptr).stream().map(ptr1 -> of(context, ptr1)).toList()));

        if (ptr instanceof Set<?>)
            return new PythonValue(new PythonSet(((Set<?>) ptr).stream().map(ptr1 -> of(context, ptr1)).collect(Collectors.toSet()), context));

        if (ptr instanceof Map<?, ?>) {
            Map<String, Value> map = ((Map<?, ?>) ptr).entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> PythonValue.of(context, e.getValue())));
            return new PythonValue(new PythonDict(context, map));
        }

        Class<?> aClass = ptr.getClass();
        Class<?>[] interfaces = aClass.getInterfaces();
        Class<?> theInterface = null;
        for (Class<?> anInterface : interfaces) {
            boolean annotationPresent = anInterface.isAnnotationPresent(FunctionalInterface.class);
            if (annotationPresent) {
                theInterface = anInterface;
            }
        }

        if (theInterface != null) {
            Method method = theInterface.getMethods()[0];
            Class<?> theFinalInterface = theInterface;
            int x = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
            InterpParameter[] params = new InterpParameter[x + method.getParameterCount()];
            Parameter[] parameters = method.getParameters();
            for (int i = x; i < method.getParameterCount() + x; i++) {
                params[i] = InterpParameter.of(context, parameters[i].getName(), theFinalInterface);
            }
            return new PythonFunction(theInterface.getName() + "()+" + System.identityHashCode(ptr), params, context, (ctx, kwargs, args) -> {
                try {
                    Object[] theArgs = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        theArgs[i] = args[i].ptr;
                        if (theArgs[i] instanceof Convertible convertible) {
                            theArgs[i] = convertible.toJava(context, method.getParameterTypes()[i]);
                        }
                    }
                    return PythonValue.of(ctx, method.invoke(ptr, args));
                } catch (InterpreterException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new InterpreterException("Function invocation failed");
                } catch (IllegalAccessException e) {
                    throw new InterpreterException("Function invocation failed, access denied.");
                }
            }).createPythonValue();
        }

        throw new InterpreterException("Object " + ptr + " is not a Python value.");
    }

    public Type asType(Context context) {
        if (this == None) {
            return context.getInterpreter().nullType();
        }
        if (this.ptr instanceof PythonClass object) {
            return object.asType();
        }
        if (this.ptr instanceof PythonModule object) {
            return object.asType();
        }
        throw new InterpreterException("Object " + this + " is not a type.");
    }


    public Object xor(Object value) {
        if (this.ptr instanceof HostObject) {
            if (value instanceof HostObject) {
                return ((HostObject) this.ptr).xor((HostObject) value);
            }
        }
        if (value instanceof Value) {
            return xor(((PythonValue) value).ptr);
        }


        throw new InterpreterException("Cannot xor with " + value.getClass());
    }
}
