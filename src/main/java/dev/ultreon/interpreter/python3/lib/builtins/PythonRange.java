package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.InterpreterError;
import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class PythonRange extends PythonBuiltin implements Iterable<Value> {
    private Value start, stop, step;

    public PythonRange(PythonClass theClass) {
        super(theClass);
    }

    public PythonRange(PythonClass theClass, int start, int stop) {
        super(theClass);

        this.start = Value.of(thisContext(), start);
        this.stop = Value.of(thisContext(), stop);
    }

    public PythonRange(PythonClass theClass, int start, int stop, int step) {
        super(theClass);

        this.start = Value.of(thisContext(), start);
        this.stop = Value.of(thisContext(), stop);
        this.step = Value.of(thisContext(), step);
    }

    @Override
    public Value get(String name) {
        return switch (name) {
            case "start" -> start;
            case "stop" -> stop;
            case "step" -> step;
            default -> super.get(name);
        };
    }

    public Value set(String name, Value value) {
        switch (name) {
            case "start" -> start = value;
            case "stop" -> stop = value;
            case "step" -> step = value;
            default -> throw new InterpreterError("Unknown range attribute: " + name);
        }
        return value;
    }

    @Override
    public @NotNull Iterator<Value> iterator() {
        return new PythonRangeIterator(this);
    }

    public static class PythonRangeIterator implements Iterator<Value> {
        private final PythonRange range;
        private int index = 0;

        public PythonRangeIterator(PythonRange range) {
            this.range = range;
        }

        @Override
        public boolean hasNext() {
            return range.start.lessThan(range.stop).asBoolean();
        }

        @Override
        public Value next() {
            Value value = range.start;
            range.start = range.start.plus(range.step);
            return value;
        }
    }
}
