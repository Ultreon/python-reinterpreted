package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.Interpreter;
import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BaseContext implements Context {
    private final SymbolTable symbolTable = new SymbolTable();
    private final List<SymbolTable> parentTables = new ArrayList<>();
    protected final @Nullable Context parentContext;

    public BaseContext(@Nullable Context parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public Interpreter getInterpreter() {
        return Objects.requireNonNull(parentContext).getInterpreter();
    }

    @Override
    public Symbol getSymbol(String text) {
        if (symbolTable.has(text)) {
            return symbolTable.get(text);
        }

        if (!parentTables.isEmpty()) {
            for (SymbolTable table : parentTables) {
                if (table.has(text)) {
                    return table.get(text);
                }
            }
        }

        Context parent = parentContext();
        if (parent != null) return parent.getSymbol(text);

        return Value.nullptr();
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Value get(String text) {
        if (symbolTable.has(text)) {
            return symbolTable.get(text);
        }

        for (SymbolTable table : parentTables) {
            if (table.has(text)) {
                return table.get(text);
            }
        }

        Context context = parentContext();
        if (context != null) {
            return context.get(text);
        }

        return null;
    }

    @Override
    public Value set(String name, Value value) {
        symbolTable.put(name, value);
        return value;
    }

    @Override
    public Context parentContext() {
        return parentContext;
    }

    @Override
    public <T> T get(String other, Class<T> type) {
        Value value = get(other);
        if (value == null) {
            return null;
        }
        if (value.isNull()) {
            throw new InterpreterTypeException("Type mismatch: null != " + type.getName());
        }
        if (!type.isInstance(value.ptr)) {
            throw new InterpreterTypeException("Type mismatch: " + value.ptr.getClass().getName() + " != " + type.getName());
        }
        return type.cast(value.ptr);
    }

    protected void mergeContext(Context currentContext) {
        if (currentContext == null) {
            return;
        }
        parentTables.add(currentContext.getSymbolTable());
        Context parent = currentContext.parentContext();
        int i = 0;
        while (parent != null) {
            parentTables.add(parent.getSymbolTable());
            parent = parent.parentContext();
            if (i++ > 100) {
                throw new InterpreterError("Too many parent contexts");
            }
        }
    }
}
