package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public interface FieldInitializer {
    void initialize(Map<String, Value> members);
}
