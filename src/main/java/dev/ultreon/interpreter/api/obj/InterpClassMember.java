package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.symbols.Type;

public interface InterpClassMember {
    String name();
    Type type();
}
