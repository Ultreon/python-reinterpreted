package dev.ultreon.interpreter.python3.lib.typing;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.context.ObjectContext;
import dev.ultreon.interpreter.api.obj.FuncType;
import dev.ultreon.interpreter.api.obj.InterpCallable;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.Invokable;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import dev.ultreon.interpreter.python3.lib.builtins.PythonClass;
import dev.ultreon.interpreter.python3.lib.builtins.PythonObject;

import java.util.Map;

public class PythonFunction extends PythonObject implements Invokable {
    private String name;
    private Invokable callable;
    private InterpParameter[] parameters;

    public PythonFunction(InterpParameter[] parameters, Context context) {
        super(context, ((PythonStdLib)context.getStdLib()).getTypingModule().getFunctionClass());
        this.parameters = parameters;
    }

    public PythonFunction(PythonClass pythonClass, InterpParameter[] parameters, Context context) {
        super(context, pythonClass);
        this.parameters = parameters;
    }

    public PythonFunction(InterpCallable callable, InterpParameter[] parameters, Context context) {
        super(context, ((PythonStdLib)context.getStdLib()).getTypingModule().getFunctionClass());

        this.callable = callable;
        this.parameters = parameters;
    }

    public PythonFunction(String name, InterpParameter[] parameters, Context context, Invokable callable) {
        super(context, null);
        this.name = name;
        this.parameters = parameters;

        this.callable = callable;
    }

    public PythonFunction(FuncType object, InterpParameter[] parameters, Context context) {
        super(context, ((PythonStdLib)context.getStdLib()).getTypingModule().getFunctionClass());
        this.callable = object;
        this.parameters = parameters;
    }

    public PythonFunction(String name, Invokable callable, InterpParameter[] parameters, Context context) {
        super(((PythonStdLib)context.getStdLib()).getTypingModule().getFunctionClass());
        this.name = name;
        this.callable = callable;
        this.parameters = parameters;
    }

    public PythonFunction(PythonClass pythonClass, Context context) {
        super(context, pythonClass);
        this.name = pythonClass.name();
        this.callable = (context1, kwargs, args) -> {
            throw new InterpreterException("Invalid function call");
        };
    }

    @Override
    public Value invoke(Context context, Map<String, Value> kwargs, Value[] args) {
        if (!(context instanceof FunctionContentsContext contentsContext)) throw new InterpreterException("Invalid function call");
        contentsContext.setArguments(kwargs, args);
        return callable.invoke(context, kwargs, args);
    }

    @Override
    protected void initMethods() {

    }

    public FunctionContentsContext createFuncContents(Map<String, Value> kwargs, Value[] args) {
        return new FunctionContentsContext(thisContext.parentContext(), callable, parameters);
    }

    public Invokable getCallable() {
        return callable;
    }

    public InterpParameter parameter(int index) {
        return parameters[index];
    }

    public InterpParameter[] parameters() {
        return parameters;
    }

    @Override
    public String name() {
        return name;
    }

    public static Value __str__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonObject self = PythonObject.getSelf(stringValueMap, values, PythonObject.class);
        if (self instanceof PythonFunction pythonFunction) {
            return Value.of(context, "<function %s at %08x>".formatted(pythonFunction.name(), System.identityHashCode(pythonFunction)));
        } else {
            return Value.of(context, "<unknown function at %08x>".formatted(System.identityHashCode(self)));
        }
    }
}
