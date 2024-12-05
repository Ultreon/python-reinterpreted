package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.*;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.context.ObjectContext;
import dev.ultreon.interpreter.api.obj.*;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.MemberRemovable;
import dev.ultreon.interpreter.python3.PythonValue;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PythonObject extends ScriptObject implements Invokable, MemberRemovable {
    public PythonObject(PythonClass theClass) {
        super(theClass, theClass.context());
        initMethods();
    }

    public PythonObject(Context context, PythonClass functionClass) {
        super(functionClass, context);
        initMethods();
    }

    protected static <T extends PythonObject> T getSelf(Map<String, Value> stringValueMap, Value[] values, Class<T> clazz) {
        Value self = stringValueMap.get("self");
        if (self == null) {
            if (values.length == 0) {
                throw new InterpreterError("self is not defined");
            }
            self = values[0];
        }
        if (self == null) {
            throw new InterpreterError("self is not defined");
        }
        Object o = self.get();
        if (clazz.isInstance(o)) {
            return clazz.cast(o);
        }
        throw new InterpreterError("self is not PythonObject");
    }

    @Override
    public Value invoke(Context context, Map<String, Value> kwargs, Value[] args) {
        return get("__call__").invoke(context, kwargs, args);
    }

    @Override
    protected void initMethods() {
        for (List<Value> method : theClass.staticMembers.values()) {
            if (method == null) continue;
            for (Value value : method) {
                if (value.ptr instanceof PythonFunction method1) {
                    InterpParameter[] parameters = method1.parameters();
                    if (parameters.length == 0) continue;
                    if (parameters[0].name().equals("self")) {
                        set(method1.name(), Value.of(thisContext, method1));
                    }
                } else if (value.ptr instanceof InterpMethod method1) {
                    InterpParameter[] parameters = method1.parameters();
                    if (parameters[0].name().equals("self")) {
                        set(method1.name(), Value.of(thisContext, method1));
                    }
                }
            }
        }
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Map<String, List<Value>> staticMembers = theClass.staticMembers;

        for (Map.Entry<String, List<Value>> entry : staticMembers.entrySet()) {
            if (entry.getValue() == null) continue;
            if (!entry.getKey().equals("__init__")) continue;
            for (Value value : entry.getValue()) {
                if (value.ptr instanceof ClassConstructor method) {
                    if (!method.matches(thisContext, visit.kwargs(), visit.args())) continue;

                    declareMethods(staticMembers);

                    ObjectContext objectContext = new ObjectContext(thisContext, this, thisContext);
                    FunctionContentsContext functionContentsContext = new FunctionContentsContext(objectContext, method.getFunction(), method.parameters());
                    functionContentsContext.setArguments(visit.kwargs(), visit.args());
                    method.invoke(functionContentsContext, visit.kwargs(), visit.args());
                    return;
                }
            }
        }

        throw new InterpreterError("No constructor matches arguments: " + Arrays.toString(visit.args()) + " with kwargs: " + visit.kwargs() + " for class " + theClass.name());
    }

    private void declareMethods(Map<String, List<Value>> staticMembers) {
        for (Map.Entry<String, List<Value>> entry : staticMembers.entrySet()) {
            if (entry.getValue() == null) continue;
            for (Value value : entry.getValue()) {
                if (value.ptr instanceof FuncType method) {
                    this.members.put(entry.getKey(), Value.of(thisContext, method));
                }
            }
        }
    }

    @Override
    public void remove(String name) {
        members.remove(name);
    }

    public PythonValue createPythonValue() {
        return new PythonValue(this);
    }

    @Override
    public String toString() {
        if (!members.containsKey("__str__")) {
            return "<%s object at 0x%08x>".formatted(name(), System.identityHashCode(this));
        }

        try {
            Object o = get("__str__").invoke(thisContext, Map.of(), this.createPythonValue()).get();
            if (o instanceof String s) {
                return s;
            } else if (o instanceof PythonString s) {
                return s.getValue();
            } else {
                return "<%s object at 0x%08x>".formatted(name(), System.identityHashCode(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<error in __str__>";
        }
    }
}
