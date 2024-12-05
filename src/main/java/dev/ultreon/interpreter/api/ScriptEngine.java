package dev.ultreon.interpreter.api;

import java.io.IOException;
import java.nio.file.Path;

public interface ScriptEngine {
    Object eval(String code);
    Object eval(String code, String filename);

    Object eval(Path path) throws IOException;
}
