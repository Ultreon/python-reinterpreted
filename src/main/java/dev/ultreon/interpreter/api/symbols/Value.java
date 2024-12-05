package dev.ultreon.interpreter.api.symbols;

import dev.ultreon.interpreter.api.*;
import dev.ultreon.interpreter.api.context.FunctionContentsContext;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.*;
import dev.ultreon.interpreter.api.ClassInstanceContext;
import dev.ultreon.interpreter.python3.*;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Value implements Symbol, ScriptValue, DeepCopyable {
    public static final Value TRUE = new Value(true);
    public static final Value FALSE = new Value(false);
    private static final Object NULL = new Value(null);
    public Object ptr;

    public Value(Object ptr) {
        this.ptr = ptr;
    }

    public boolean isNull() {
        return this == NULL;
    }

    @Override
    public Object get() {
        return ptr;
    }

    @Override
    public void setPtr(Object ptr) {
        this.ptr = ptr;
    }

    @Override
    public String toString() {
        if (this.isNull()) {
            return "null";
        }

        return ptr.toString();
    }

    public Value invoke(Context context, Map<String, Value> kwargs, Value... args) {
        if (!(ptr instanceof Invokable invokable))
            throw new InterpreterException("Symbol is not callable");

        FunctionContentsContext callContext = createFuncContents(kwargs, args);
        return invokable.invoke(callContext, kwargs, args);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Value value) {
            return ptr.equals(value.ptr);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ptr.hashCode();
    }

    @Override
    public Value copy() {
        return new Value(ptr);
    }

    public Value deepCopy() {
        if (ptr instanceof DeepCopyable deepCopyable) {
            return new Value(deepCopyable.deepCopy());
        }
        return new Value(ptr);
    }

    public static Value nullptr() {
        return (Value) NULL;
    }

    public static Value of(@NotNull Context context, Object ptr) {
        return switch (ptr) {
            case null -> nullptr();
            case Value value -> value;
            case ScriptObject object -> new Value(object);
            case FuncType object -> new Value(object);
            case ClassLike object -> new Value(object);
            case HostObject object -> new Value(object);
            case ClassConstructor object -> new Value(object);
            default -> new Value(HostObject.of(context, ptr));
        };

    }

    @Override
    public Value xor(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("xor").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot xor with " + visit.getClass());
        }
    }

    public Value or(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("or").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot or with " + visit.getClass());
        }
    }

    public Value and(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("and").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot and with " + visit.getClass());
        }
    }

    public Value plus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("plus").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot plus with " + visit.getClass());
        }
    }

    public Value negate() {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("negate").invoke(object.thisContext(), Map.of(), this);
        } else {
            throw new InterpreterException("Cannot negate with " + this.getClass());
        }
    }

    public Value shl(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("shl").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot shift with " + visit.getClass());
        }
    }

    public Value shr(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("shr").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot shift with " + visit.getClass());
        }
    }

    public Value times(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("times").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot times with " + visit.getClass());
        }
    }

    public Value divide(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("divide").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot divide with " + visit.getClass());
        }
    }

    public Value floordiv(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("floordiv").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot floordiv with " + visit.getClass());
        }
    }

    public Value rem(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("rem").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot rem with " + visit.getClass());
        }
    }

    public Value unaryPlus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("unaryPlus").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot unaryPlus with " + visit.getClass());
        }
    }

    public Value unaryMinus(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("unaryMinus").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot unaryMinus with " + visit.getClass());
        }
    }

    public Object pow(Value visit) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("pow").invoke(object.thisContext(), Map.of(), this, visit);
        } else {
            throw new InterpreterException("Cannot pow with " + visit.getClass());
        }
    }

    public FunctionContentsContext createFuncContents(Map<String, Value> kwargs, Value[] args) {
        if (this.ptr instanceof PythonFunction pythonFunction) {
            return pythonFunction.createFuncContents(kwargs, args);
        }
        if (this.ptr instanceof ScriptObject object) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        if (this.ptr instanceof FuncType funcType) {
            return funcType.createFuncContents();
        }

        if (this.ptr instanceof ClassLike classLike) {
            return null;
        }

        throw new InterpreterException("Object " + this + " is not a function.");
    }

    public Type asType() {
        if (this == NULL) {
            throw new InterpreterNullPtrException("Cannot convert null to type.");
        }

        if (ptr instanceof ClassLike classLike) {
            return Type.class_(classLike);
        }
        throw new InterpreterException("Object " + this + " is not a type.");
    }

    public Object invoke(FuncCall visit) {
        if (this.ptr instanceof FCInvokable fcInvokable) {
            return fcInvokable.invoke(createFuncContents(visit.kwargs(), visit.args()), visit);
        }
        if (this.ptr instanceof ClassLike classLike && this.ptr instanceof Invokable invokable) {
            return invokable.invoke(classLike.createInvokeContents(), visit.kwargs(), visit.args());
        }
        if (this.ptr instanceof PythonFunction pythonFunction) {
            Context context = pythonFunction.thisContext();
            if (context instanceof ClassInstanceContext) {
                visit.kwargs().put("self", PythonValue.of(context, ((ClassInstanceContext) context).getInstance()));
            }
            FunctionContentsContext funcContents = pythonFunction.createFuncContents(visit.kwargs(), visit.args());
            return pythonFunction.invoke(funcContents, visit.kwargs(), visit.args());
        }
        if (this.ptr instanceof Invokable invokable) {
            FunctionContentsContext funcContents = createFuncContents(visit.kwargs(), visit.args());
            if (funcContents == null) {
                throw new InterpreterException("Object " + this + " is not a function.");
            }
            funcContents.setArguments(visit);
            return invokable.invoke(funcContents, visit.kwargs(), visit.args());
        }
        if (this instanceof Invokable invokable) {
            FunctionContentsContext funcContents = createFuncContents(visit.kwargs(), visit.args());
            if (funcContents == null) {
                throw new InterpreterException("Object " + this + " is not a function.");
            }
            funcContents.setArguments(visit);
            return invokable.invoke(funcContents, visit.kwargs(), visit.args());
        }
        throw new InterpreterException("Object " + this + " is not a function.");
    }

    public Value lessThan(Value stop) {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("lessThan").invoke(object.thisContext(), Map.of(), this, stop);
        } else {
            throw new InterpreterException("Cannot compare with " + stop.getClass());
        }
    }

    public boolean asBoolean() {
        if (this.ptr instanceof ScriptObject object) {
            return object.get("asBoolean").invoke(object.thisContext(), Map.of(), this).asBoolean();
        } else {
            throw new InterpreterException("Cannot compare with " + this.getClass());
        }
    }
}
