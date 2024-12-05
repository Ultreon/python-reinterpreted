package dev.ultreon.interpreter.api;

import dev.ultreon.interpreter.api.context.FileContext;
import dev.ultreon.interpreter.api.context.StdLib;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import org.antlr.v4.runtime.tree.ParseTree;

public interface Interpreter {
    Object visit(ParseTree tree);

    FileContext getMainContext();

    String getClassSymbolName();

    StdLib getStdLib();

    Type nullType();

    Type anyType();

    Value nullValue();

    boolean isAssert();
}
