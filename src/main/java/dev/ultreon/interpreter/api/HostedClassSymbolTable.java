package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.SymbolTable;
import dev.ultreon.interpreter.api.obj.FuncType;
import dev.ultreon.interpreter.api.symbols.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HostedClassSymbolTable extends SymbolTable {
    private final Class<?> aClass;
    private final Context context;

    public HostedClassSymbolTable(Class<?> aClass, Context context) {
        this.aClass = aClass;
        this.context = context;
    }

    @Override
    public Value get(String name) {
        try {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    return new Value(field.get(null));
                }
            }

            List<Method> methods = new ArrayList<>();
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.getName().equals(name)) {
                    methods.add(method);
                }
            }
            return new Value(new FuncType(aClass, methods.toArray(new Method[0]), null, context));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
