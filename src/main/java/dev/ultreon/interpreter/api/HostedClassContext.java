package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.SymbolTable;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;

public class HostedClassContext implements Context {
    private final Class<?> aClass;
    private final HostedClassSymbolTable symbolTable;
    private Context parentContext;

    public HostedClassContext(Class<?> aClass, Context parentContext) {
        this.aClass = aClass;
        this.symbolTable = new HostedClassSymbolTable(aClass, this);
        this.parentContext = parentContext;
    }

    public Interpreter getInterpreter() {
        return parentContext.getInterpreter();
    }

    @Override
    public Symbol getSymbol(String text) {
        if (symbolTable.has(text)) {
            return symbolTable.get(text);
        }
        return null;
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Value get(String text) {
        return null;
    }

    @Override
    public Value set(String name, Value value) {
        this.symbolTable.put(name, value);
        return value;
    }

    @Override
    public Context parentContext() {
        return parentContext;
    }

    @Override
    public <T> T get(String other, Class<T> type) {
        Value value = symbolTable.get(other);
        if (value == null) {
            throw new InterpreterError("No such member: " + other);
        }

        if (type.isInstance(value.get())) {
            return type.cast(value.get());
        }

        throw new InterpreterError("Cannot cast " + value.get() + " to " + type);
    }
}
