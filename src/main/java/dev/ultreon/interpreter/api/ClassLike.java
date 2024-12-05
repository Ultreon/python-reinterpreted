package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ClassLike {
    String name();

    Value cast(Value value);

    ObjectLike construct();

    boolean isInstance(ObjectLike scriptObject);

    Value get(String text);

    Type asType();

    List<ClassLike> parents();

    Context createInvokeContents();

    @NotNull Context context();

    Value set(String text, Value v);
}
