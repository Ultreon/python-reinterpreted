package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.PrimitiveValue;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.HostClass;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public interface InterpFuncProxy extends Invokable {
    static InterpFuncProxy of(ScriptObject object, InterpMethod method) {
        return (context, kwargs, args) -> method.invoke(context, object, kwargs, args);
    }

    static InterpFuncProxy of(ClassType classType, InterpMethod value) {
        return (context, kwargs, args) -> value.invoke(context, null, kwargs, args);
    }

    static InterpFuncProxy of(Class<?> clazz, Method[] method, Object instance) {
        return (context, kwargs, args) -> {
            try {
                methodLoop: for (Method m : method) {
                    if (m.getParameterCount() == args.length) {
                        Object[] callArgs = new Object[m.getParameterCount()];
                        for (int i = 0; i < args.length; i++) {
                            callArgs[i] = args[i].ptr;
                            if (callArgs[i] instanceof Convertible convertible) {
                                if (convertible.isConvertibleToJava(context, m.getParameterTypes()[i]))
                                    callArgs[i] = convertible.toJava(context, m.getParameterTypes()[i]);
                                else
                                    continue methodLoop;
                            } else if (!m.getParameterTypes()[i].isInstance(callArgs[i])) {
                                continue methodLoop;
                            }
                        }

                        Object invoke = m.invoke(instance, callArgs);
                        return switch (invoke) {
                            case null -> Value.nullptr();
                            case Number number -> new PrimitiveValue(context, invoke);
                            case Boolean b -> new PrimitiveValue(context, invoke);
                            case String s -> new PrimitiveValue(context, invoke);
                            case Character c -> new PrimitiveValue(context, invoke);
                            case Class<?> type -> new PrimitiveValue(context, new HostClass(context.getInterpreter().getMainContext(), type));
                            default -> Value.of(context, invoke);
                        };
                    }
                }

                throw new InterpreterError("Invalid arguments for method " + clazz.getName() + "." + method[0].getName() + ": " + Arrays.toString(args));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    default Value invoke(Context context, Value... args) {
        return invoke(context, null, args);
    }

    Value invoke(Context context, @Nullable Map<String, Value> kwargs, Value... args);
}
