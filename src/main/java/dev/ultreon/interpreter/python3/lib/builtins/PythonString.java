package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.Convertible;
import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.python3.PythonValue;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

public class PythonString extends PythonBuiltin implements Convertible {
    private String str;
    private boolean init = true;

    public PythonString(String str, Context parentContext) {
        super(((PythonStdLib) parentContext.getStdLib()).getBuiltins().getStringClass(), parentContext);
        this.str = str;
    }

    public PythonString(PythonClass theClass, Context parentContext) {
        super(theClass);
        init = false;
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        Value[] args = visit.args();
        if (args.length != 0) {
            if (args.length == 1) {
                Value arg = args[0];
                switch (arg.ptr) {
                    case PythonString pythonString -> str = pythonString.str;
                    case PythonInt pythonInt -> str = String.valueOf(pythonInt.getValue());
                    case PythonFloat pythonFloat -> str = String.valueOf(pythonFloat.getValue());
                    case PythonBool pythonBool -> str = String.valueOf(pythonBool.getValue());
                    case PythonNone pythonNone -> str = "NoneType";
                    case null, default -> {
                        if (arg.ptr != null) {
                            str = arg.ptr.toString();
                        } else {
                            throw new InterpreterTypeException("Expected string, not null");
                        }
                    }
                }
            }
        }

        init = true;
    }

    public String getValue() {
        return str;
    }

    @Override
    public Object toJava(@NotNull Context context, Class<?> targetType) {
        if (targetType == String.class) {
            return str;
        } else {
            throw new InterpreterTypeException("Expected string, not " + targetType.getName());
        }
    }

    @Override
    public boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType) {
        return targetType == String.class;
    }

    public static Value __add__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        Object other = context.get("other", Object.class);

        return switch (other) {
            case PythonString pythonString -> new PythonString(self.str + pythonString.str, context).createPythonValue();
            case String s -> new PythonString(self.str + other, context).createPythonValue();
            case Integer i -> new PythonString(self.str + other, context).createPythonValue();
            case Float v -> new PythonString(self.str + other, context).createPythonValue();
            case Boolean b -> new PythonString(self.str + other, context).createPythonValue();
            case null -> new PythonString(self.str + "NoneType", context).createPythonValue();
            case PythonObject pythonObject -> new PythonString(self.str + other, context).createPythonValue();
            default -> throw new InterpreterTypeException("Expected string, not " + other.getClass().getName());
        };
    }

    public static Value __repr__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        String sb = "\"" +
                    String.valueOf(strValue(values[0])) +
                    "\"";
        return new PythonString(sb, context).createPythonValue();
    }

    public static Value __str__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        return context.get("self", PythonString.class).createPythonValue();
    }

    private static char[] strValue(Value value) {
        StringBuilder sb = new StringBuilder();
        String str = value.toString();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"') {
                sb.append("\\\"");
            } else if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\t') {
                sb.append("\\t");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (c == '\f') {
                sb.append("\\f");
            } else if (c == '\b') {
                sb.append("\\b");
            } else if (c == '\0') {
                sb.append("\\0");
            } else if (c == '\'') {
                sb.append("\\'");
            } else if (c <= 31) {
                sb.append("\\x%02x".formatted((int) c));
            } else if (c == 127) {
                sb.append("\\x7f");
            } else if (c == 128) {
                sb.append("\\x80");
            } else if (c == 255) {
                sb.append("\\xff");
            } else if (c > 255) {
                sb.append("\\u%04x".formatted((int) c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString().toCharArray();
    }

    public static Value __len__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        return new PythonInt(self.str.length(), context).createPythonValue();
    }

    public static Value __getitem__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonInt index = context.get("index", PythonInt.class);
        return new PythonString(String.valueOf(self.str.charAt((int) index.getValue())), context).createPythonValue();
    }

    public static Value __eq__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.equals(other.str));
    }

    public static Value __ne__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, !self.str.equals(other.str));
    }

    public static Value __lt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.compareTo(other.str) < 0);
    }

    public static Value __gt__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.compareTo(other.str) > 0);
    }

    public static Value __le__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.compareTo(other.str) <= 0);
    }

    public static Value __ge__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.compareTo(other.str) >= 0);
    }

    public static Value __contains__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        PythonString other = context.get("other", PythonString.class);
        return PythonValue.of(context, self.str.contains(other.str));
    }

    public static Value __hash__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        return new PythonInt(self.str.hashCode(), context).createPythonValue();
    }

    public static Value __iter__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonString self = context.get("self", PythonString.class);
        return new PythonStringIter(context, self).createPythonValue();
    }

    private static class PythonStringIter extends PythonIterator implements Iterator<Value> {
        private final String str;
        private int index = 0;

        public PythonStringIter(Context context, PythonString self) {
            super(context);

            this.str = self.str;
        }

        public static Value createPythonValue(Context context, PythonString self) {
            return PythonValue.of(context, new PythonStringIter(context, self));
        }

        @Override
        public boolean hasNext() {
            return index < str.length();
        }

        @Override
        public Value next() {
            return new PythonString(String.valueOf(str.charAt(index++)), thisContext).createPythonValue();
        }
    }
}
