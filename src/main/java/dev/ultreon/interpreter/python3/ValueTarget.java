package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.symbols.Value;

import java.util.function.Consumer;

public record ValueTarget(String name, Consumer<Value> setter) {
}
