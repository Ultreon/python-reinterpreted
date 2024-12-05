package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public interface InterpCallable extends Invokable {
    Value invoke(Context context, Map<String, Value> kwargs, Value... args);
}
