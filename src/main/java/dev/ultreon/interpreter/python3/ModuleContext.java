package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.context.FileContext;

public class ModuleContext extends FileContext {
    private final String path;

    public ModuleContext(Python3Interpreter interpreter, String path) {
        super(path, interpreter);
        this.getSymbolTable().putAll(interpreter.getOrCreateRootSymbolTable());
        this.path = path;
    }

    public String path() {
        return path;
    }
}
