package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.Interpreter;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;

public interface Context {

    Interpreter getInterpreter();

    Symbol getSymbol(String text);

    SymbolTable getSymbolTable();

    Value get(String text);

    Value set(String name, Value value);

    Context parentContext();

    default StdLib getStdLib() {
        return getInterpreter().getStdLib();
    }

    <T> T get(String other, Class<T> type);
}
