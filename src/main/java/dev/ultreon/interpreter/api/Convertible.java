package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import org.jetbrains.annotations.NotNull;

public interface Convertible {
    Object toJava(@NotNull Context context, Class<?> targetType);

    boolean isConvertibleToJava(@NotNull Context context, Class<?> targetType);

    default <T> T castedToJava(@NotNull Context context, Class<T> targetType) {
        Object java = toJava(context, targetType);
        if (!targetType.isInstance(java)) {
            throw new InterpreterError("Cannot use " + java + " as " + targetType + " in the Java context");
        }
        return targetType.cast(java);
    }
}
