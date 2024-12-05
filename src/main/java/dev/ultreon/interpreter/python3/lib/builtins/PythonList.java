package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.python3.PythonValue;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("SlowListContainsAll")
public class PythonList extends PythonBuiltin implements List<PythonValue> {
    private final List<PythonValue> list = new ArrayList<>();

    public PythonList(Context context) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getListClass());
    }

    public PythonList(Context context, Collection<PythonValue> list) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getListClass());
        this.list.addAll(list);
    }

    public PythonList(Context context, PythonValue... values) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getListClass());
        this.list.addAll(Arrays.asList(values));
    }

    public PythonList(PythonClass pythonClass) {
        super(pythonClass);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public @NotNull Iterator<PythonValue> iterator() {
        return list.iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return list.toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(PythonValue pythonValue) {
        return list.add(pythonValue);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends PythonValue> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends PythonValue> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public PythonValue get(int index) {
        return list.get(index);
    }

    @Override
    public PythonValue set(int index, PythonValue element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, PythonValue element) {
        list.add(index, element);
    }

    @Override
    public PythonValue remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<PythonValue> listIterator() {
        return list.listIterator();
    }

    @Override
    public @NotNull ListIterator<PythonValue> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public @NotNull List<PythonValue> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
