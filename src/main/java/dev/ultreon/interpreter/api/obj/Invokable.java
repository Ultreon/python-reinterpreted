package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

@FunctionalInterface
public interface Invokable {
    Value invoke(Context context, Map<String, Value> kwargs, Value[] args);
}
