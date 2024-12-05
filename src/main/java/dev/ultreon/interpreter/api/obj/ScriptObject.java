package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.*;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.context.ObjectContext;
import dev.ultreon.interpreter.api.obj.vars.InterpVar;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.ClassInstanceContext;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.ClassLike;
import dev.ultreon.interpreter.api.HostClass;
import dev.ultreon.interpreter.api.ObjectLike;

import java.lang.reflect.Proxy;
import java.util.*;

public abstract class ScriptObject implements ObjectLike {
    protected final ClassType theClass;
    protected final ClassInstanceContext thisContext;
    protected final Map<String, Value> members = new HashMap<>();
    private final Object proxy;
    private final Context parentContext;

    public ScriptObject(ClassType theClass, Context parentContext) {
        super();
        this.theClass = theClass;
        this.parentContext = parentContext;

        proxy = createProxyObject();
        thisContext = new ClassInstanceContext(parentContext, this);
    }

    public ClassType theClass() {
        return theClass;
    }

    public String name() {
        return theClass.name();
    }

    @Override
    public Value set(String name, Value value) {
        members.put(name, value);
        return value;
    }

    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Value[] args = visit.args();

        ClassConstructor matches = null;
        for (ClassConstructor value : theClass.constructors()) {
            if (value.matches(thisContext, visit.kwargs(), args)) {
                if (matches != null) {
                    throw new InterpreterError("Multiple constructors match arguments: " + Arrays.toString(args));
                }
                matches = value;
            }
        }

        if (matches == null) {
            throw new InterpreterError("No constructor matches arguments: " + Arrays.toString(args));
        }

        initMethods();

        ObjectContext context = createContext(thisContext);
        FunctionContentsContext functionContentsContext = new FunctionContentsContext(context, matches.getFunction(), matches.parameters());
        matches.invoke(functionContentsContext, visit.kwargs(), args);
    }

    protected abstract void initMethods();

    public Object getField(String name) {
        return members.get(name);
    }

    public void setField(String name, Object value) {
        members.put(name, Value.of(thisContext, value));
    }

    public ObjectContext createContext(Context currentContext) {
        return new ObjectContext(this.thisContext, this, thisContext);
    }

    public Value invokeMember(String name, Map<String, Value> kwargs, Value... args) {
        InterpMethod method = theClass.getMethod(name);
        Map<String, Value> callKwargs = new HashMap<>(kwargs);
        callKwargs.put("self", Value.of(thisContext, this));
        return method.invoke(thisContext, null, callKwargs, args);
    }

    @Deprecated
    public Map<String, ? extends InterpVar> variables() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public Map<String, ? extends FuncType> functions() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object toObject() {
        return this;
    }

    public Value get(String name) {
        Value value = members.get(name);
        if (value == null) {
            throw new InterpreterError("No such member: " + name);
        }
        return value;
    }

    public Context thisContext() {
        return thisContext;
    }

    public List<ClassLike> parents() {
        return theClass.parents();
    }

    private Object createProxyObject() {
//        List<HostClass> parents = new ArrayList<>();
//        for (ClassLike parent : theClass.parents()) {
//            if (parent instanceof HostClass hostClass) {
//                parents.add(hostClass);
//            } else {
//                findHosts(parent, parents);
//            }
//        }
//
//        if (parents.isEmpty()) {
//            parents = List.of(new HostClass(thisContext, Object.class));
//        }
//
//        return Proxy.newProxyInstance(theClass.getClass().getClassLoader(), new Class[]{}, (proxy, method, args) -> {
//            String name = method.getName();
//            if (members.containsKey(name)) {
//                Value[] callArgs = new Value[args.length];
//                for (int i = 0; i < args.length; i++) {
//                    callArgs[i] = Value.of(thisContext, args[i]);
//                }
//                return members.get(name).invoke(thisContext, Map.of(), callArgs);
//            }
//
//            throw new InterpreterError("No such member: " + name);
//        });

        if (theClass == null) {
            return null;
        }

        return Proxy.newProxyInstance(theClass.getClass().getClassLoader(), new Class[]{}, (proxy, method, args) -> {
            String name = method.getName();
            if (members.containsKey(name)) {
                Value[] callArgs = new Value[args.length];
                for (int i = 0; i < args.length; i++) {
                    callArgs[i] = Value.of(thisContext, args[i]);
                }
                return members.get(name).invoke(thisContext, Map.of(), callArgs);
            }

            throw new InterpreterError("No such member: " + name);
        });
    }

    private void findHosts(ClassLike type, List<HostClass> parents) {
        for (ClassLike parent : type.parents()) {
            if (parent instanceof HostClass hostClass) {
                parents.add(hostClass);
            } else {
                findHosts(parent, parents);
            }
        }
    }

    public Context parentContext() {
        return parentContext;
    }
}
