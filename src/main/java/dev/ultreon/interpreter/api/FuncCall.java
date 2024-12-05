package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public interface FuncCall {
    Map<String, Value> kwargs();
    Value[] args();
}
