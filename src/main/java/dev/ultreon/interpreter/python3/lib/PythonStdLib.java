package dev.ultreon.interpreter.python3.lib;

import dev.ultreon.interpreter.api.context.StdLib;
import dev.ultreon.interpreter.api.obj.Namespace;
import dev.ultreon.interpreter.python3.BuiltinsModule;
import dev.ultreon.interpreter.python3.Python3Interpreter;
import dev.ultreon.interpreter.python3.TypingModule;

public class PythonStdLib implements StdLib {
    private TypingModule typing;
    private BuiltinsModule builtins;
    private final Python3Interpreter interpreter;

    public PythonStdLib(Python3Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void init() {
        builtins = new BuiltinsModule(interpreter.createNativeModuleContext("builtins.pyd"));
        typing = new TypingModule(interpreter.createNativeModuleContext("typing.pyd"));
    }

    public BuiltinsModule getBuiltins() {
        return builtins;
    }

    public TypingModule getTypingModule() {
        return typing;
    }

    @Override
    public Namespace use(String name) {
        if (name.equals("typing")) {
            return typing;
        } else if (name.equals("builtins")) {
            return builtins;
        } else {
            return null;
        }
    }
}
