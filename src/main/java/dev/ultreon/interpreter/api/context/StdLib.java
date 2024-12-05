package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.obj.Namespace;

public interface StdLib {
    Namespace use(String name);
}
