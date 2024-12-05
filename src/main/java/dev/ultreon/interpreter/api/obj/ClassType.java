package dev.ultreon.interpreter.api.obj;

import com.google.common.base.Preconditions;
import dev.ultreon.interpreter.api.ClassLike;
import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.ObjectLike;
import dev.ultreon.interpreter.api.context.ClassDefinitionContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClassType implements Symbol, ClassLike {
    private final String name;
    private final List<ClassLike> parents = new ArrayList<>();
    public final Map<String, List<Value>> staticMembers = new HashMap<>();
    private final @NotNull Context context;

    public ClassType(@NotNull Context context, String name, ClassDefinitionContext classDefinitionContext) {
        Preconditions.checkNotNull(classDefinitionContext, "ClassDefinitionContext cannot be null");
        this.name = name;
        this.context = context;
    }

    public ClassType(@NotNull Context context, String name) {
        Preconditions.checkNotNull(context, "Interpreter cannot be null");
        this.name = name;
        this.context = context;
    }

    protected void declareConstructor(InterpFuncProxy proxy, FuncType funcType, InterpParameter... parameters) {
        ClassConstructor ptr = new ClassConstructor(this, funcType, parameters);
        ptr.setProxy(proxy);
        staticMembers.computeIfAbsent(getConstructorName(), k -> new ArrayList<>()).addLast(Value.of(context, ptr));
    }

    public void declareStaticMember(String name, Invokable proxy, InterpParameter... parameters) {
        declareConstructor(proxy, parameters);
        staticMembers.computeIfAbsent(name, k -> new ArrayList<>()).addLast(Value.of(context, proxy));
    }

    private void declareConstructor(Invokable proxy, InterpParameter[] parameters) {
        ClassConstructor ptr = new ClassConstructor(this, proxy, parameters);
        staticMembers.computeIfAbsent(getConstructorName(), k -> new ArrayList<>()).addLast(Value.of(context, ptr));
    }

    public void declareStaticMember(String name, Value value) {
        if (name.equals(getConstructorName())) {
            Object ptr = value.ptr;
            if (ptr instanceof FuncType funcType) {
                declareConstructor(funcType.getProxy(), funcType, funcType.parameters());
            } else if (ptr instanceof PythonFunction pythonFunction) {
                try {
                    declareConstructor(pythonFunction.getCallable(), pythonFunction.parameters());
                } catch (InterpreterException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new InterpreterException("Constructor must be a function");
            }
        }
        staticMembers.computeIfAbsent(name, k -> new ArrayList<>()).addLast(value);
    }

    protected String getConstructorName() {
        return "{{INIT}}";
    }

    public InterpMethod getMethod(String name) {
        throw new UnsupportedOperationException();
    }

    public InterpField getField(String name) {
        throw new UnsupportedOperationException();
    }

    public ClassConstructor getConstructor(String name) {
        throw new UnsupportedOperationException();
    }

    public String name() {
        return name;
    }

    @Override
    public Value cast(Value value) {
        throw new UnsupportedOperationException();
    }

    public Object cast(Object value) {
        throw new UnsupportedOperationException();
    }

    public Value get(String visit) {
        if (staticMembers.containsKey(visit)) {
            return staticMembers.get(visit).getLast();
        }

        return null;
    }

    public abstract ScriptObject construct();

    public boolean isInstance(ObjectLike objectLike) {
        if (!(objectLike instanceof ScriptObject scriptObject)) {
            return false;
        }
        ClassType classType = scriptObject.theClass();
        for (ClassLike parent : classType.parents) {
            if (parent == this) {
                return true;
            }

            if (parent.isInstance(scriptObject)) {
                return true;
            }
        }

        return false;
    }

    public List<ClassLike> parents() {
        return parents;
    }

    @Override
    public @NotNull Context context() {
        return context;
    }

    public List<ClassConstructor> constructors() {
        return staticMembers.getOrDefault(getConstructorName(), List.of()).stream().map(Value::get).map(ClassConstructor.class::cast).toList();
    }

    public void setDecorators(List<Value> decoratorsList) {
        decoratorsList.forEach(Value::get);
    }

    public Object interpreter() {
        return context;
    }
}
