package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PythonSet extends PythonBuiltin implements Set<Value> {
    private final Set<Value> set = new HashSet<>();

    public PythonSet(PythonClass theClass, Context parentContext) {
        super(theClass);
    }

    public PythonSet(Collection<Value> values, Context parentContext) {
        super(((PythonStdLib)parentContext.getStdLib()).getBuiltins().getSetClass());
        set.addAll(values);
    }

    @Override
    public boolean add(Value value) {
        return set.add(value);
    }

    @Override
    public boolean remove(Object value) {
        return set.remove(value);
    }

    @Override
    public boolean contains(Object value) {
        return set.contains(value);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Value> c) {
        return set.addAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public @NotNull Iterator<Value> iterator() {
        return set.iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return set.toArray(a);
    }
}
