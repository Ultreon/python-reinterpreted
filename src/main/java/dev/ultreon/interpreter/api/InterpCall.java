package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.InterpParameter;

public record InterpCall(String name, InterpParameter[] parameters) {
}
