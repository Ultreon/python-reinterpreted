package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public record InterpInitializerList(Map<String, Value> kwargs, Value[] args, Object visit) {
}
