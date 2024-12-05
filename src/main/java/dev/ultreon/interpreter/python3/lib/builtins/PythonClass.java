package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.ClassDefinitionContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.*;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.python3.PythonValue;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PythonClass extends ClassType implements Invokable, Convertible {
    private BiFunction<PythonClass, Context, PythonObject> constructor;
    private Context parentContext;

    public PythonClass(@NotNull Context context, String name, ClassDefinitionContext definitionContext) {
        super(context, name);
        this.parentContext = definitionContext.parentContext();

        createDefaultMethods(context, definitionContext);
    }

    private void createDefaultMethods(@NotNull Context context, ClassDefinitionContext definitionContext) {
        this.declareFunction("__init__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__init__);

        this.declareFunction("__new__", definitionContext, new InterpParameter[]{new InterpParameter("cls", asType(), false)}, this::__new__);
        this.declareFunction("__call__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("args", Type.of(context, PythonIterable.class), false)}, this::__call__);
        this.declareFunction("__add__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__add__);
        this.declareFunction("__sub__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__sub__);
        this.declareFunction("__mul__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__mul__);
        this.declareFunction("__truediv__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__truediv__);
        this.declareFunction("__floordiv__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__floordiv__);
        this.declareFunction("__mod__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__mod__);
        this.declareFunction("__pow__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__pow__);
        this.declareFunction("__lshift__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__lshift__);
        this.declareFunction("__rshift__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__rshift__);
        this.declareFunction("__and__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__and__);
        this.declareFunction("__xor__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__xor__);
        this.declareFunction("__or__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__or__);
        this.declareFunction("__neg__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__neg__);
        this.declareFunction("__pos__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__pos__);
        this.declareFunction("__invert__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__invert__);
        this.declareFunction("__complex__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__complex__);
        this.declareFunction("__abs__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__abs__);

        this.declareFunction("__bool__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__bool__);
        this.declareFunction("__int__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__int__);
        this.declareFunction("__float__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__float__);
        this.declareFunction("__complex__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__complex__);
        this.declareFunction("__str__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__str__);
        this.declareFunction("__bytes__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__bytes__);
        this.declareFunction("__bytearray__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__bytearray__);
        this.declareFunction("__memoryview__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__memoryview__);
        this.declareFunction("__dict__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__dict__);
        this.declareFunction("__weakref__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__weakref__);
        this.declareFunction("__dir__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__dir__);
        this.declareFunction("__repr__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__repr__);
        this.declareFunction("__hash__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__hash__);

        this.declareFunction("__lt__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__lt__);
        this.declareFunction("__le__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__le__);
        this.declareFunction("__eq__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__eq__);
        this.declareFunction("__ne__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__ne__);
        this.declareFunction("__gt__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__gt__);
        this.declareFunction("__ge__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("other", asType(), false)}, this::__ge__);

        this.declareFunction("__len__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__len__);
        this.declareFunction("__floor__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__floor__);
        this.declareFunction("__ceil__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__ceil__);
        this.declareFunction("__round__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__round__);
        this.declareFunction("__trunc__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__trunc__);
        this.declareFunction("__iter__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__iter__);
        this.declareFunction("__next__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__next__);
        this.declareFunction("__reversed__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false)}, this::__reversed__);
        this.declareFunction("__contains__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("item", asType(), false)}, this::__contains__);
        this.declareFunction("__getitem__", definitionContext, new InterpParameter[]{new InterpParameter("self", asType(), false), new InterpParameter("item", asType(), false)}, this::__getitem__);

        this.get("__new__").invoke(context, new HashMap<>(), this.createPythonValue());
    }

    private Value __trunc__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __round__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __floor__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __ceil__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __lt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __le__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __eq__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __ne__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __gt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __ge__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __add__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __sub__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __mul__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __truediv__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __floordiv__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __mod__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __pow__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __lshift__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __rshift__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __and__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __xor__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __or__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __neg__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __pos__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __invert__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __complex__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __abs__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __call__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __bool__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __int__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __float__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __str__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value self = stringValueMap.get("self") == null ? null : stringValueMap.get("self");
        if (self == null) {
            if (values.length != 1) {
                throw new InterpreterError("__str__ expects 1 argument, got 0");
            }
            self = values[0];
        }
        if (self == null) {
            throw new InterpreterError("__str__ expects 1 argument, got <invalid>");
        }
        return Value.of(context, "<%s object at 0x%08x>".formatted(name(), System.identityHashCode(this)));
    }

    private Value __bytes__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __bytearray__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __memoryview__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __dict__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __weakref__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __format__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __dir__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __repr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return Value.of(context, toString());
    }

    private Value __hash__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return Value.of(context, hashCode());
    }

    private Value __getattr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value name = stringValueMap.get("name");
        if (name == null) {
            if (values.length == 1) {
                name = values[0];
            } else {
                throw new InterpreterError(name() + " expects 1 argument, got " + values.length);
            }
        }
        throw new InterpreterError(name() + " has no attribute " + name);
    }

    private Value __setattr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value name = stringValueMap.get("name");
        if (name == null) {
            if (values.length == 2) {
                name = values[0];
            } else {
                throw new InterpreterError(name() + " expects 2 arguments, got " + values.length);
            }
        }
        throw new InterpreterError(name() + " has no attribute " + name);
    }

    private Value __delattr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value name = stringValueMap.get("name");
        if (name == null) {
            if (values.length == 1) {
                name = values[0];
            } else {
                throw new InterpreterError(name() + " expects 1 argument, got " + values.length);
            }
        }
        throw new InterpreterError(name() + " has no attribute " + name);
    }

    private Value __getitem__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        throw new InterpreterError(name() + " is not subscriptable");
    }

    private Value __setitem__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        throw new InterpreterError(name() + " is not subscriptable");
    }

    private Value __delitem__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        throw new InterpreterError(name() + " is not subscriptable");
    }

    private Value __iter__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        throw new InterpreterError(name() + " is not iterable");
    }

    private Value __new__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value cls = stringValueMap.get("cls");
        if (cls == null) cls = values[0];
        if (cls == null) throw new InterpreterError("cls is null");
        Object o = cls.get();
        if (o instanceof PythonClass pythonClass) {
            return context.getInterpreter().nullValue();
        } else {
            if (context.getInterpreter().isAssert()) throw new AssertionError("cls is not PythonClass");
        }
        return context.getInterpreter().nullValue();
    }

    private Value __init__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value self = context.get("self");
        if (self == null) throw new InterpreterError("self is null");
        Object o = self.get();
        if (o instanceof PythonObject pythonObject) {

        } else {
            if (context.getInterpreter().isAssert()) throw new AssertionError("self is not PythonObject");
        }
        return context.getInterpreter().nullValue();
    }

    private Value __len__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __nonzero__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __contains__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __reversed__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __enter__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __exit__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    private Value __next__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.getInterpreter().nullValue();
    }

    public void declareFunction(String name, Context ctx, InterpParameter[] parameters, Invokable invokable) {
        PythonFunction function = new PythonFunction(name, parameters, context(), invokable);
        declareStaticMember(name, PythonValue.of(ctx, function));
    }

    public PythonClass(@NotNull Context context, String name, Function<PythonClass, PythonObject> constructor) {
        super(context, name);
        this.constructor = (pythonClass, ctx) -> constructor.apply(pythonClass);

        createDefaultMethods(context, new ClassDefinitionContext(context, this));
    }

    public PythonClass(@NotNull Context context, String name, BiFunction<PythonClass, Context, PythonObject> constructor) {
        super(context, name);
        this.constructor = constructor;

        createDefaultMethods(context, new ClassDefinitionContext(context, this));
    }

    public PythonClass(@NotNull Context context, String name) {
        super(context, name);

        createDefaultMethods(context, new ClassDefinitionContext(context, this));
    }

    @Override
    public void declareStaticMember(String name, Value value) {
        if (value.ptr instanceof FuncType funcType) {
            declareFunction(name, this.context(), funcType.parameters(), funcType.getProxy());
            return;
        }
        super.declareStaticMember(name, value);
    }

    @Override
    protected String getConstructorName() {
        return "__init__";
    }

    @Override
    public PythonObject construct() {
        if (constructor != null) return constructor.apply(this, parentContext);
        return new PythonObject(this);
    }

    @Override
    public Type asType() {
        return Type.class_(this);
    }

    @Override
    public Context createInvokeContents() {
        return new ClassDefinitionContext(parentContext, this);
    }

    @Override
    public Value set(String text, Value v) {
        this.staticMembers.put(text, new ArrayList<>()).add(v);
        return v;
    }

    @Override
    public Value invoke(Context context, Map<String, Value> kwargs, Value[] args) {
        ScriptObject construct = construct();
        Map<String, Value> callKwargs = new HashMap<>(kwargs);
        callKwargs.put("self", Value.of(context, construct));
        construct.initialize(new InterpInitializerList(callKwargs, args, null), members -> { });
        return Value.of(context, construct);
    }

    public Context parentContext() {
        return parentContext;
    }

    @Override
    public Value toJava(@NotNull Context context, Class<?> targetType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return false;
    }

    public PythonValue createPythonValue() {
        return new PythonValue(this);
    }

    @Override
    public String toString() {
        return "<class '" + name() + "'>";
    }

    public void overrideFunction(String name, InterpParameter[] parameters, Invokable invokable) {
        if (!(staticMembers.containsKey(name))) throw new InterpreterError("Function " + name + " does not exist");
        staticMembers.remove(name);
        declareFunction(name, new ClassDefinitionContext(context(), this), parameters, invokable);
    }
}
