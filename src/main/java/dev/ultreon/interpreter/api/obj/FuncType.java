package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.TypedSymbol;
import dev.ultreon.interpreter.api.symbols.Value;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FuncType implements TypedSymbol, Invokable, InterpParameterized {
    private final String name;
    private final InterpParameter[] parameters;
    private final Type type;
    private InterpMethod method;
    private ClassType theClass;
    private InterpFuncProxy proxy;
    private List<Value> decorators;
    private Object instance;
    private Context context;

    public FuncType(String name, InterpParameter[] parameters, Type type, Context declarationContext) {
        this.name = name;
        this.parameters = parameters;
        this.type = type;
        this.context = declarationContext;
    }

    public FuncType(ScriptObject object, InterpMethod method, Context declarationContext) {
        this.name = method.name();
        this.parameters = method.parameters();
        this.type = method.type();
        this.context = declarationContext;
        this.proxy = InterpFuncProxy.of(object, method);
    }

    public FuncType(ClassType classType, InterpMethod value, Context declarationContext) {
        this.name = value.name();
        this.parameters = value.parameters();
        this.type = value.type();
        this.context = declarationContext;
        this.proxy = InterpFuncProxy.of(classType, value);
        this.theClass = classType;
        this.method = value;
    }

    public FuncType(Class<?> clazz, Method[] method, Object instance, Context context) {
        this.name = method[0].getName();
        this.parameters = InterpMethod.parameterTypes(context, method);
        this.type = context.getInterpreter().anyType();
        this.instance = instance;
        this.context = context;
        this.proxy = InterpFuncProxy.of(clazz, method, instance);
    }

    public void setPtr(InterpFuncProxy proxy) {
        this.proxy = proxy;
        if (theClass != null) {
            method.setPtr(proxy);
        }
    }

    public void setProxy(InterpFuncProxy proxy) {
        this.proxy = proxy;
    }

    public InterpFuncProxy getProxy() {
        return proxy;
    }

    public Object invoke(Context context, Value... args) {
        return proxy.invoke(context, args);
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

    @Override
    public InterpParameter parameter(int index) {
        return parameters[index];
    }

    @Override
    public InterpParameter[] parameters() {
        return parameters;
    }

    public void setDecorators(List<Value> decoratorsList) {
        decorators = decoratorsList;
    }

    public List<Value> getDecorators() {
        return decorators;
    }

    @Override
    public Value invoke(Context context, Map<String, Value> kwargs, Value[] args) {
        return proxy.invoke(context, kwargs, args);
    }

    public FunctionContentsContext createFuncContents() {
        Context declarationContext1 = this.context;
        return new FunctionContentsContext(declarationContext1, this, parameters);
    }
}
