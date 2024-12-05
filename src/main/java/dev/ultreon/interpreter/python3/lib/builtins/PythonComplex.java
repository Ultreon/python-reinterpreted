package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;

public class PythonComplex extends PythonBuiltin {
    private final double real;
    private final double imag;

    public PythonComplex(Context context, double real, double imag) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getComplexClass());
        this.real = real;
        this.imag = imag;
    }

    public PythonComplex(PythonClass theClass) {
        super(theClass);
        this.real = 0;
        this.imag = 0;
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }
}
