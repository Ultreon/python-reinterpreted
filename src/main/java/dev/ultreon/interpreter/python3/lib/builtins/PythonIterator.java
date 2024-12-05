package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.InterpreterTypeException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;

import java.util.Iterator;
import java.util.Map;

public class PythonIterator extends PythonBuiltin implements Iterator<Value> {
    private Iterator<Value> iterator;

    public PythonIterator(PythonClass theClass) {
        super(theClass, theClass.context());
    }

    public PythonIterator(PythonClass functionClass, Context context) {
        super(functionClass, context);
    }

    public PythonIterator(Iterator<Value> iterator, Context context) {
        super(((PythonStdLib) context.getStdLib()).getBuiltins().getIteratorClass(), context);
        this.iterator = iterator;
    }

    protected PythonIterator(Context context) {
        super(((PythonStdLib) context.getStdLib()).getBuiltins().getIteratorClass(), context);
        this.iterator = this;
    }

    public static Value __next__(Context context, Map<String, Value> stringValueMap, Value[] values) {
        PythonIterator self = PythonIterator.getSelf(stringValueMap, values, PythonIterator.class);
        if (self == null) {
            throw new InterpreterTypeException("Expected iterator, not " + values[0].ptr.getClass().getName());
        }
        if (!self.iterator.hasNext()) {
            throw new InterpreterError("Iterator is empty");
        }
        return self.iterator.next();
    }

    @Override
    public boolean hasNext() {
        if (iterator == this) throw new InterpreterError("Iterator isn't implemented");
        return iterator.hasNext();
    }

    @Override
    public Value next() {
        if (iterator == this) throw new InterpreterError("Iterator isn't implemented");
        return iterator.next();
    }
}
