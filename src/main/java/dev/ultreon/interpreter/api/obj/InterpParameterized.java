package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public interface InterpParameterized {
    InterpParameter parameter(int index);
    InterpParameter[] parameters();

    default boolean matches(Context context, Map<String, Value> map, Value[] args) {
        InterpParameter[] parameters = parameters();
        boolean lengthMatches = parameters.length == args.length + (map == null ? 0 : map.size());
        if (!lengthMatches) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            boolean matches = parameters[i].matches(context, map, i < args.length ? args[i] : map.get(parameters[i].name()));
            if (!matches) {
                return false;
            }

        }
        return true;
    }
}
