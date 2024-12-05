package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.InterpParameter;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.python3.lib.builtins.PythonClass;
import dev.ultreon.interpreter.python3.lib.typing.PythonFunction;
import dev.ultreon.interpreter.python3.lib.builtins.PythonModule;
import dev.ultreon.interpreter.python3.lib.builtins.PythonObject;
import org.jetbrains.annotations.Nullable;

public class TypingModule extends PythonModule {
    private final PythonClass Function = new PythonClass(this, "function", (java.util.function.BiFunction<PythonClass, Context, PythonObject>) PythonFunction::new);

    public TypingModule(@Nullable Context context) {
        super("typing", context);

        Function.overrideFunction("__str__", new InterpParameter[]{new InterpParameter("self", Function.asType(), false)}, PythonFunction::__str__);
    }

    public PythonClass getFunctionClass() {
        return Function;
    }
}
