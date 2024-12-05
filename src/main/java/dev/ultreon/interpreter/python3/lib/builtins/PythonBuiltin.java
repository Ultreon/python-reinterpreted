package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.InterpInitializerList;

public class PythonBuiltin extends PythonObject {
    public PythonBuiltin(PythonClass theClass) {
        super(theClass);
    }

    public PythonBuiltin(PythonClass functionClass, Context context) {
        super(context, functionClass);
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
