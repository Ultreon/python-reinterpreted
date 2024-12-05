package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.obj.Invokable;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Value> symbols = new HashMap<>();

    public SymbolTable() {

    }

    public Value get(String name) {
        if (!symbols.containsKey(name))
            return null;
        return symbols.get(name);
    }

    public void put(String name, Value symbol) {
        if (symbol == null)
            throw new InterpreterException("Symbol " + name + " cannot be null");
        symbols.put(name, symbol);
    }

    public void remove(String name) {
        symbols.remove(name);
    }

    public void clear() {
        symbols.clear();
    }

    public Value invoke(Context context, String name, Map<String, Value> kwargs, Value... args) {
        Symbol symbol = symbols.get(name);
        if (symbol == null)
            throw new InterpreterException("Symbol " + name + " not found");
        if (symbol instanceof Invokable invokable)
            return invokable.invoke(context, Collections.unmodifiableMap(kwargs), args);
        throw new InterpreterException("Symbol " + name + " cannot be invoked");
    }

    public SymbolTable copy() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.symbols.putAll(symbols);
        return symbolTable;
    }

    public Map<String, Value> all() {
        return Collections.unmodifiableMap(symbols);
    }

    public void putAll(Map<String, Value> all) {
        symbols.putAll(all);
    }

    public void putAll(SymbolTable rootSymbolTable) {
        symbols.putAll(rootSymbolTable.symbols);
    }

    public boolean has(String text) {
        return symbols.containsKey(text);
    }
}
