package dev.ultreon.interpreter.api.context;

import dev.ultreon.interpreter.api.*;
import dev.ultreon.interpreter.api.obj.*;
import dev.ultreon.interpreter.api.obj.vars.FieldBuilder;
import dev.ultreon.interpreter.api.obj.vars.InterpVar;
import dev.ultreon.interpreter.api.symbols.ClassDefiningSymbolTable;
import dev.ultreon.interpreter.api.symbols.Symbol;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.python3.lib.builtins.PythonClass;

import java.util.HashMap;
import java.util.Map;

public class ClassDefinitionContext extends BaseContext {
    public Map<String, InterpField> fields = new HashMap<>();
    public Map<String, InterpMethod> methods = new HashMap<>();
    public Map<String, ClassConstructor> constructors = new HashMap<>();

    private final Map<String, FieldBuilder> declaringFields = new HashMap<>();
    public boolean isConstructor = false;
    public boolean isStatic = false;
    private String name;
    private final Context parentContext;
    private PythonClass type;
    private SymbolTable symbolTable;

    public ClassDefinitionContext(Context parentContext, PythonClass classType) {
        super(parentContext);
        this.parentContext = parentContext;
        this.name = classType.name();
        this.type = classType;
        this.symbolTable = new ClassDefiningSymbolTable(this, classType);
    }

    @Override
    public Symbol getSymbol(String text) {
        return super.getSymbol(text);
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Context parentContext() {
        return parentContext;
    }
}
