package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.context.SymbolTable;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;

public class HostedThisContext implements Context {
    private Interpreter interpreter;
    private SymbolTable symbolTable;

    public HostedThisContext(Object host, Interpreter interpreter) {
        this.interpreter = interpreter;

        symbolTable = new HostedObjectSymbolTable(interpreter.getMainContext(), host);
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }

    @Override
    public Symbol getSymbol(String text) {
        if (text.equals("this")) {
            return Symbol.THIS;
        }
        return null;
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Value get(String text) {
        return getSymbolTable().get(text);
    }

    @Override
    public Value set(String name, Value value) {
        getSymbolTable().put(name, value);
        return value;
    }

    @Override
    public Context parentContext() {
        return null;
    }

    @Override
    public <T> T get(String other, Class<T> type) {
        Value value = get(other);
        if (value == null) {
            throw new InterpreterError("No such member: " + other);
        }
        if (type.isInstance(value.ptr)) {
            return type.cast(value.ptr);
        }

        throw new InterpreterTypeException("Cannot cast " + value + " to " + type);
    }
}
