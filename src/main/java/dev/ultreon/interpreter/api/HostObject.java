package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class HostObject implements ObjectLike, Convertible {
    private @NotNull Context context;
    protected Object host;
    protected Context thisContext;
    private boolean init = true;

    protected HostObject(@NotNull Context context, Object host) {
        super();
        this.context = context;
        this.host = host;
        this.thisContext = new HostedThisContext(host, context.getInterpreter());
    }

    HostObject(@NotNull Context context, Class<?> aClass, boolean notInit) {
        this.context = context;
        this.host = aClass;
        this.init = !notInit;
        if (!notInit) {
            this.thisContext = new HostedThisContext(host, context.getInterpreter());
        }
    }

    public Object getHost() {
        return host;
    }

    public static HostObject of(@NotNull Context context, Object o) {
        if (o instanceof HostObject) {
            return (HostObject) o;
        }
        if (o instanceof Class<?>) {
            return new HostClass(context, (Class<?>) o);
        }
        return new HostObject(context, o);
    }

    public ClassLike asClass() {
        return (HostClass) host;
    }

    @Override
    public Object toObject() {
        return host;
    }

    @Override
    public Value set(String name, Value value) {
        thisContext.set(name, value);
        return value;
    }

    @Override
    public Value get(String name) {
        return thisContext.get(name);
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        if (init) {
            return;
        }

        Class<?> ptr1 = (Class<?>) this.host;
        Value[] args = visit.args();
        Class<?>[] args2 = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            args2[i] = args[i].ptr.getClass();
        }
        Constructor<?>[] constructors = ptr1.getConstructors();
        ctorLoop:
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == args.length) {
                Object[] constructorArgs = new Object[parameterTypes.length];
                for (int i = 0; i < args.length; i++) {
                    Class<?> parameterType = parameterTypes[i];

                    constructorArgs[i] = args[i].ptr;
                    if (constructorArgs[i] instanceof Convertible convertible) {
                        if (convertible.isConvertibleToJava(context, parameterType))
                            constructorArgs[i] = convertible.toJava(context, parameterType);
                        else
                            continue ctorLoop;
                    } else if (!parameterType.isInstance(constructorArgs[i])) {
                        continue ctorLoop;
                    }
                }

                try {
                    this.host = constructor.newInstance(constructorArgs);
                    this.thisContext = new HostedThisContext(host, context.getInterpreter());
                    return;
                } catch (Exception e) {
                    throw new InterpreterException("Cannot initialize " + ptr1.getName() + " with " + Arrays.toString(args));
                }
            }
        }

        throw new InterpreterException("Cannot initialize " + ptr1.getName() + " with " + Arrays.toString(args));
    }

    public Object xor(HostObject value) {
        if (this.get("xor") != null) {
            return this.get("xor");
        }

        throw new InterpreterException("Cannot xor with " + value.getClass());
    }

    public Object or(HostObject value) {
        if (this.get("or") != null) {
            return this.get("or");
        }

        throw new InterpreterException("Cannot or with " + value.getClass());
    }

    public Object and(HostObject value) {
        if (this.get("and") != null) {
            return this.get("and");
        }

        throw new InterpreterException("Cannot and with " + value.getClass());
    }

    public Object neq(HostObject value) {
        if (this.get("neq") != null) {
            return this.get("neq");
        }

        if (this.get("eq") != null) {
            return HostObject.of(context, this.get("eq")).invert();
        }

        throw new InterpreterException("Cannot neq with " + value.getClass());
    }

    private Object invert() {
        if (this.get("unaryMinus") != null) {
            return this.get("unaryMinus");
        }

        throw new InterpreterException("Cannot invert with " + this.getClass());
    }

    public Object lt(HostObject value) {
        if (this.get("compareTo") != null) {
            this.get("compareTo");
        }

        throw new InterpreterException("Cannot lt with " + value.getClass());
    }

    public Object eq(HostObject value) {
        if (this.get("eq") != null) {
            return this.get("eq");
        }

        throw new InterpreterException("Cannot eq with " + value.getClass());
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType.isInstance(host)) {
            return host;
        }

        return host;
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return targetType.isInstance(host);
    }
}
