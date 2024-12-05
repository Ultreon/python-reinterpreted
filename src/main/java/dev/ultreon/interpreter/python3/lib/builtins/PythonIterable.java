package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.FieldInitializer;
import dev.ultreon.interpreter.api.InterpInitializerList;

public class PythonIterable extends PythonBuiltin {
    public PythonIterable(PythonClass theClass) {
        super(theClass);
    }

    public PythonIterable(Context context, PythonClass functionClass) {
        super(functionClass, context);
    }

    @Override
    public void initialize(InterpInitializerList visit, FieldInitializer fieldInitializer) {
        super.initialize(visit, fieldInitializer);
    }
}
