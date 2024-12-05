package dev.ultreon.interpreter.api.obj;

import com.google.common.base.Preconditions;
import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public class ClassConstructor implements InterpClassMember, InterpParameterized {
    private final ClassType theClass;
    private final InterpParameter[] parameters;
    private Invokable proxy;
    private FuncType funcType;

    public ClassConstructor(ClassType theClass, FuncType funcType, InterpParameter[] parameters) {
        Preconditions.checkNotNull(theClass);
        Preconditions.checkNotNull(funcType);
        Preconditions.checkNotNull(parameters);
        this.theClass = theClass;
        this.parameters = parameters;
        this.proxy = funcType.getProxy();
        this.funcType = funcType;
    }

    public ClassConstructor(ClassType theClass, Invokable proxy, InterpParameter[] parameters) {
        Preconditions.checkNotNull(theClass);
        Preconditions.checkNotNull(proxy);
        Preconditions.checkNotNull(parameters);
        this.theClass = theClass;
        this.parameters = parameters;
        this.proxy = proxy;
        this.funcType = new FuncType("__init__", parameters, theClass.context().getInterpreter().nullType(), theClass.context());
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public Type type() {
        return null;
    }

    public void invoke(FunctionContentsContext context, Value... args) {
        proxy.invoke(context, Map.of(), args);
    }

    public void invoke(FunctionContentsContext context, Map<String, Value> kwargs, Value... args) {
        proxy.invoke(context, kwargs, args);
    }

    public void setProxy(Invokable o) {
        this.proxy = o;
    }

    public Invokable getProxy() {
        return proxy;
    }

    @Override
    public InterpParameter parameter(int index) {
        return parameters[index];
    }

    @Override
    public InterpParameter[] parameters() {
        return parameters;
    }

    public FuncType getFunction() {
        return funcType;
    }
}
