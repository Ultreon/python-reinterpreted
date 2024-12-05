package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record InterpParameter(String name, Type type, boolean constant) {
    public static InterpParameter VARARGS = new InterpParameter("*", null, false);
    public static InterpParameter KWARGS = new InterpParameter("**", null, false);

    public static InterpParameter of(@NotNull Context context, String name, Class<?> parameterType) {
        return new InterpParameter(name, Type.of(context, parameterType), false);
    }

    public boolean matches(Context context, Map<String, Value> map, Value arg) {
        Object o = arg.get();
        if (map != null && map.containsKey(name)) {
            if (o instanceof ScriptObject scriptObject) {
                return type == context.getInterpreter().anyType() || type.isInstance(scriptObject);
            } else {
                return type == context.getInterpreter().anyType() || type.isInstance(arg);
            }
        } else if (o instanceof ScriptObject scriptObject) {
            return type == context.getInterpreter().anyType() || type.isInstance(scriptObject);
        } else {
            return type == context.getInterpreter().anyType() || type.isInstance(arg);
        }
    }
}
