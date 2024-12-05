package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.ObjectContext;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.ClassLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

public class InterpMethod implements InterpFunctionTemplate, InterpClassMember, Symbol, InterpParameterized {
    private static final InterpParameter[] ANY_PARAMETERS = new InterpParameter[]{InterpParameter.VARARGS, InterpParameter.KWARGS};
    private final String name;
    private final InterpParameter[] parameters;
    private final boolean isStatic;
    private final Type type;
    private InterpClassMemberProxy proxy;
    private final Context parentContext;

    public InterpMethod(String name, InterpParameter[] parameters, boolean isStatic, Type type, Context parentContext) {
        this.name = name;
        this.parameters = parameters;
        this.isStatic = isStatic;
        this.type = type;
        this.parentContext = parentContext;
    }

    public static InterpParameter[] parameterTypes(@NotNull Context context, Method[] method) {
        return ANY_PARAMETERS;
    }

    public static Type returnType(@NotNull Context context, Method method) {
        return Type.of(context, method.getReturnType());
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return type;
    }

    public void setProxy(InterpClassMemberProxy proxy) {
        this.proxy = proxy;
    }

    public InterpClassMemberProxy getProxy() {
        return proxy;
    }

    public Value invoke(Context context, @Nullable ScriptObject instance, Map<String, Value> kwargs, Value... args) {
        if (proxy == null) {
            throw new InterpreterError("Method " + name + " is not implemented");
        }

        if (args == null) {
            args = new Value[0];
        }

        if (instance == null && !isStatic) throw new InterpreterError("Method " + name + " is an instance method");
        if (instance != null && isStatic) throw new InterpreterError("Method " + name + " is a static method");

        if (!matches(context, kwargs, args)) {
            throw new InterpreterError("Method " + name + " does not match arguments: " + Arrays.toString(args));
        }

        return proxy.invoke(new ObjectContext(context, instance, context), instance, kwargs, args);
    }

    @Override
    public InterpParameter parameter(int index) {
        return parameters[index];
    }

    @Override
    public InterpParameter[] parameters() {
        return parameters;
    }

    public Context getParentContext() {
        return parentContext;
    }

    public ClassLike theClass() {
        return type.theClass();
    }

    public void setPtr(InterpFuncProxy proxy) {
        this.proxy = ((context, instance, kwargs, args) -> proxy.invoke(context, kwargs, args));
    }
}
