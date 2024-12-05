package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.Interpreter;

import java.nio.file.Path;

public class FileContext extends BaseContext {
    private final String path;
    private final Interpreter interpreter;

    public FileContext(String path, Interpreter interpreter) {
        super(null);
        this.path = path;
        this.interpreter = interpreter;
    }

    public FileContext(Path path, Interpreter interpreter) {
        super(null);
        this.path = path.toString();
        this.interpreter = interpreter;
    }

    public String path() {
        return path;
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }
}
