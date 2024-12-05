package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.obj.Invokable;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.ClassInstanceContext;
import dev.ultreon.interpreter.api.FuncCall;

import javax.annotation.concurrent.Immutable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FunctionContentsContext extends BaseContext {
    private final Invokable function;
    public Value returnedValue = null;
    private final InterpParameter[] parameters;

    public FunctionContentsContext(Context parentContext, Invokable function, InterpParameter[] parameters) {
        super(parentContext);
        this.function = function;
        this.parameters = parameters;
        this.mergeContext(parentContext);
    }

    public FunctionContentsContext(ClassInstanceContext currentContext, Invokable function, InterpParameter[] parameters) {
        this((Context) currentContext, function, parameters);
        this.mergeContext(currentContext);
    }

    public Invokable getFunction() {
        return function;
    }

    public void setArguments(FuncCall funcCall) {
        setArguments(funcCall.kwargs(), funcCall.args());
    }

    public void setArguments(Map<String, Value> kwargs, Value[] args) {
        Set<String> names = new HashSet<>();
        int di = 0;
        for (InterpParameter parameter : parameters) {
            String name = parameter.name();
            if (names.contains(name)) {
                throw new InterpreterError("Duplicate argument name: " + name);
            }
            names.add(name);
            if (kwargs.containsKey(name)) {
                Value value = kwargs.get(name);
                this.set(name, value);
                continue;
            }
            if (di < args.length) {
                Value arg = args[di];
                if (arg != null) {
                    this.set(name, arg);
                }
            }
            di++;
        }
    }
}
