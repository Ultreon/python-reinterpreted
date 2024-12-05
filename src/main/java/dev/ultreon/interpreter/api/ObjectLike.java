package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.InterpInitializerList;

public interface ObjectLike {
    Object toObject();

    Value set(String name, Value value);

    Value get(String name);

    void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer);
}
