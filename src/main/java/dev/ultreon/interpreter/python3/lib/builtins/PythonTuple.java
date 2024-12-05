package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PythonTuple extends PythonBuiltin implements Iterable<Value> {
    private final Value[] tuple;

    public PythonTuple(PythonClass theClass, Value... tuple) {
        super(theClass);
        this.tuple = tuple;
    }

    public PythonTuple(PythonClass theClass, List<Value> tuple) {
        super(theClass);
        this.tuple = tuple.toArray(new Value[0]);
    }

    public PythonTuple(PythonClass theClass, int size) {
        super(theClass);
        this.tuple = new Value[size];
    }

    public PythonTuple(PythonClass theClass) {
        super(theClass);
        this.tuple = new Value[0];
    }

    @Override
    public @NotNull Iterator<Value> iterator() {
        ArrayIterator iterator = new ArrayIterator();
        iterator.tuple = tuple;
        return iterator;
    }

    @Override
    public void forEach(Consumer<? super Value> action) {
        for (Value value : tuple) {
            action.accept(value);
        }
    }

    @Override
    public Spliterator<Value> spliterator() {
        return Arrays.spliterator(tuple);
    }

    public Stream<Value> stream() {
        return Arrays.stream(tuple);
    }

    public Value get(int index) {
        return tuple[index];
    }

    public int size() {
        return tuple.length;
    }

    public void set(int index, Value value) {
        tuple[index] = value;
    }

    public Value[] toArray() {
        return tuple;
    }

    private static class ArrayIterator implements Iterator<Value> {
        public Value[] tuple;
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < tuple.length;
        }

        @Override
        public Value next() {
            return tuple[index++];
        }
    }
}
