package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.builtins.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class BuiltinsModule extends PythonModule {
    private final PythonClass str = new PythonClass(this, "str", PythonString::new);
    private final PythonClass list = new PythonClass(this, "list", (Function<PythonClass, PythonObject>) PythonList::new);
    private final PythonClass dict = new PythonClass(this, "dict", (Function<PythonClass, PythonObject>) PythonDict::new);
    private final PythonClass set = new PythonClass(this, "set", PythonSet::new);
    private final PythonClass tuple = new PythonClass(this, "tuple", (Function<PythonClass, PythonObject>) PythonTuple::new);
    private final PythonClass range = new PythonClass(this, "range", PythonRange::new);
    private final PythonClass bytes = new PythonClass(this, "bytes", (Function<PythonClass, PythonObject>) PythonBytes::new);
    private final PythonClass bytearray = new PythonClass(this, "bytearray", (Function<PythonClass, PythonObject>) PythonByteArray::new);
    private final PythonClass bool = new PythonClass(this, "bool", PythonBool::new);
    private final PythonClass int_ = new PythonClass(this, "int", PythonInt::new);
    private final PythonClass float_ = new PythonClass(this, "float", PythonFloat::new);
    private final PythonClass complex = new PythonClass(this, "complex", PythonComplex::new);
    private final PythonClass object = new PythonClass(this, "object", (Function<PythonClass, PythonObject>) PythonObject::new);
    private final PythonClass iter = new PythonClass(this, "iter", (Function<PythonClass, PythonObject>) PythonIterator::new);

    public BuiltinsModule(Context interpreter) {
        super("builtins", interpreter);

        str.overrideFunction("__add__", new InterpParameter[]{new InterpParameter("self", str.asType(), false), new InterpParameter("other", interpreter.getInterpreter().anyType(), false)}, PythonString::__add__);
        str.overrideFunction("__repr__", new InterpParameter[]{new InterpParameter("self", str.asType(), false)}, PythonString::__repr__);
        str.overrideFunction("__str__", new InterpParameter[]{new InterpParameter("self", str.asType(), false)}, PythonString::__str__);
        str.overrideFunction("__len__", new InterpParameter[]{new InterpParameter("self", str.asType(), false)}, PythonString::__len__);
        str.overrideFunction("__iter__", new InterpParameter[]{new InterpParameter("self", str.asType(), false)}, PythonString::__iter__);

        int_.overrideFunction("__add__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__add__);
        int_.overrideFunction("__sub__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__sub__);
        int_.overrideFunction("__mul__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__mul__);
        int_.overrideFunction("__truediv__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__truediv__);
        int_.overrideFunction("__floordiv__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__floordiv__);
        int_.overrideFunction("__mod__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__mod__);
        int_.overrideFunction("__repr__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__repr__);
        int_.overrideFunction("__str__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__str__);
        int_.overrideFunction("__bool__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__bool__);
        int_.overrideFunction("__lt__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__lt__);
        int_.overrideFunction("__le__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__le__);
        int_.overrideFunction("__gt__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__gt__);
        int_.overrideFunction("__ge__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__ge__);
        int_.overrideFunction("__eq__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__eq__);
        int_.overrideFunction("__ne__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__ne__);
        int_.overrideFunction("__and__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__and__);
        int_.overrideFunction("__or__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__or__);
        int_.overrideFunction("__xor__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__xor__);
        int_.overrideFunction("__lshift__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__lshift__);
        int_.overrideFunction("__rshift__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__rshift__);
        int_.overrideFunction("__pow__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false), new InterpParameter("other", int_.asType(), false)}, PythonInt::__pow__);
        int_.overrideFunction("__neg__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__neg__);
        int_.overrideFunction("__pos__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__pos__);
        int_.overrideFunction("__abs__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__abs__);
        int_.overrideFunction("__invert__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__invert__);
        int_.overrideFunction("__floor__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__floor__);
        int_.overrideFunction("__ceil__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__ceil__);
        int_.overrideFunction("__round__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__round__);
        int_.overrideFunction("__int__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__int__);
        int_.overrideFunction("__float__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__float__);
        int_.overrideFunction("__complex__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__complex__);
        int_.overrideFunction("__trunc__", new InterpParameter[]{new InterpParameter("self", int_.asType(), false)}, PythonInt::__trunc__);

        float_.overrideFunction("__add__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__add__);
        float_.overrideFunction("__sub__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__sub__);
        float_.overrideFunction("__mul__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__mul__);
        float_.overrideFunction("__truediv__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__truediv__);
        float_.overrideFunction("__mod__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__mod__);
        float_.overrideFunction("__repr__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__repr__);
        float_.overrideFunction("__str__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__str__);
        float_.overrideFunction("__bool__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__bool__);
        float_.overrideFunction("__lt__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__lt__);
        float_.overrideFunction("__le__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__le__);
        float_.overrideFunction("__gt__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__gt__);
        float_.overrideFunction("__ge__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__ge__);
        float_.overrideFunction("__eq__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__eq__);
        float_.overrideFunction("__ne__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__ne__);
        float_.overrideFunction("__pow__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false), new InterpParameter("other", float_.asType(), false)}, PythonFloat::__pow__);
        float_.overrideFunction("__floor__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__floor__);
        float_.overrideFunction("__ceil__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__ceil__);
        float_.overrideFunction("__round__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__round__);
        float_.overrideFunction("__int__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__int__);
        float_.overrideFunction("__float__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__float__);
        float_.overrideFunction("__complex__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__complex__);
        float_.overrideFunction("__trunc__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__trunc__);
        float_.overrideFunction("__abs__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__abs__);
        float_.overrideFunction("__neg__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__neg__);
        float_.overrideFunction("__pos__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__pos__);
        float_.overrideFunction("__hash__", new InterpParameter[]{new InterpParameter("self", float_.asType(), false)}, PythonFloat::__hash__);

        iter.overrideFunction("__next__", new InterpParameter[]{new InterpParameter("self", iter.asType(), false)}, PythonIterator::__next__);
    }

    protected Value print(Context context, Map<String, Value> stringValueMap, Value[] values) {
        Value value = stringValueMap.get("end");
        Value sep = stringValueMap.get("sep");
        Value file = stringValueMap.get("file");
        if (file != null) {
            throw new InterpreterError("File argument is not supported");
        }
        String join = String.join(sep == null ? " " : sep.toString(), Arrays.stream(values).map(Value::toString).toArray(String[]::new));
        System.out.print(join + (value == null ? "" : value.toString()));

        return context.getInterpreter().nullValue();
    }

    private final PythonClass none = new PythonClass(this, "NoneType", PythonNone::new);

    public PythonClass getStringClass() {
        return str;
    }

    public PythonClass getListClass() {
        return list;
    }

    public PythonClass getDictClass() {
        return dict;
    }

    public PythonClass getSetClass() {
        return set;
    }

    public PythonClass getTupleClass() {
        return tuple;
    }

    public PythonClass getRangeClass() {
        return range;
    }

    public PythonClass getBytesClass() {
        return bytes;
    }

    public PythonClass getByteArrayClass() {
        return bytearray;
    }

    public PythonClass getBoolClass() {
        return bool;
    }

    public PythonClass getIntClass() {
        return int_;
    }

    public PythonClass getFloatClass() {
        return float_;
    }

    public PythonClass getComplexClass() {
        return complex;
    }

    public PythonClass getObjectClass() {
        return object;
    }

    public PythonClass getNoneClass() {
        return none;
    }

    @Override
    public @NotNull Context context() {
        return super.context();
    }

    public PythonClass getIteratorClass() {
        return iter;
    }
}
