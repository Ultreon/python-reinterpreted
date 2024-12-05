package dev.ultreon.interpreter.api.obj;

import java.util.Map;

public record InterpConstruction(
    ClassConstructor constructor,
    Object[] args,
    Map<String, Object> kwargs
) {
}
