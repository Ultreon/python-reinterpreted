package dev.ultreon.interpreter.api.symbols;

import dev.ultreon.interpreter.api.context.ClassDefinitionContext;
import dev.ultreon.interpreter.api.context.SymbolTable;
import dev.ultreon.interpreter.api.obj.ClassType;

public class ClassDefiningSymbolTable extends SymbolTable {
    private final ClassType classType;

    public ClassDefiningSymbolTable(ClassDefinitionContext classDefinitionContext, ClassType classType) {
        super();
        this.classType = classType;
    }

    @Override
    public void put(String name, Value symbol) {
        classType.declareStaticMember(name, symbol);
    }
}
