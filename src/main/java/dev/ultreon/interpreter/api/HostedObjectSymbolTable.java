package dev.ultreon.interpreter.api;

import com.google.common.base.CaseFormat;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.SymbolTable;
import dev.ultreon.interpreter.api.obj.FuncType;
import dev.ultreon.interpreter.api.symbols.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HostedObjectSymbolTable extends SymbolTable {
    private final Context context;
    private final Object host;
    private final Class<? extends Object> theClass;

    public HostedObjectSymbolTable(Context context, Object host) {
        this.context = context;
        this.host = host;
        this.theClass = host.getClass();
    }

    @Override
    public Value get(String name) {
        try {
            Field field = theClass.getDeclaredField(name);
            return Value.of(context, field.get(host));
        } catch (NoSuchFieldException e) {
            try {
                String expectedJavaName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
                Method method = theClass.getDeclaredMethod("get" + expectedJavaName);
                return Value.of(context, method.invoke(host));
            } catch (InvocationTargetException ex) {
                throw new InterpreterException("Invocation error when calling getter method named " + name + " in java class " + theClass.getName());
            } catch (IllegalAccessException ex) {
                throw new InterpreterError("Access violation when accessing getter method named " + name + " in java class " + theClass.getName());
            } catch (NoSuchMethodException ex) {
                List<Method> methods = new ArrayList<>();
                for (Method method : theClass.getDeclaredMethods()) {
                    if (method.getName().equals(name)) {
                        methods.add(method);
                    }
                }
                if (methods.isEmpty()) {
                    throw new InterpreterError("No field or getter method named " + name + " in java class " + theClass.getName());
                }

                return Value.of(context, new FuncType(theClass, methods.toArray(new Method[0]), host, context));
            }
        } catch (IllegalAccessException e) {
            throw new InterpreterError("No field or getter method named " + name + " in java class " + theClass.getName());
        }
    }

    @Override
    public void put(String name, Value symbol) {
        try {
            Field field = theClass.getDeclaredField(name);
            field.set(host, symbol.get());
        } catch (NoSuchFieldException e) {
            String expectedJavaName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
            try {
                Method method = null;
                Object value = symbol.get();
                for (Method m : theClass.getMethods()) {
                    if (m.getName().equals("set" + expectedJavaName)) {
                        if (m.getParameterCount() != 1) continue;
                        if (symbol.get() instanceof Convertible convertible &&
                            convertible.isConvertibleToJava(context, m.getParameters()[0].getType())
                        ) {
                            method = m;
                            value = convertible.toJava(context, m.getParameters()[0].getType());
                        }
                        break;
                    }
                }
                if (method == null) {
                    throw new InterpreterError("No field or setter method named " + name + " in java class " + theClass.getName() + " that accepts " + symbol.get().getClass().getName());
                }
                method.invoke(host, value);
            } catch (InvocationTargetException ex) {
                throw new InterpreterException("Invocation error when calling setter method named " + name + " in java class " + theClass.getName());
            } catch (IllegalAccessException ex) {
                throw new InterpreterError("Access violation when accessing setter method named " + name + " in java class " + theClass.getName());
            }
        } catch (IllegalAccessException e) {
            throw new InterpreterError("No field or setter method named " + name + " in java class " + theClass.getName());
        }
    }
}
