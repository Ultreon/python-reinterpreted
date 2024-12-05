package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Value;

public interface ScriptValue {
    Object get();
    void setPtr(Object ptr);

    Value xor(Value visit);

    Value copy();
}
