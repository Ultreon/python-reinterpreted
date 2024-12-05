package dev.ultreon.interpreter.python3.lib.builtins;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PythonDict extends PythonBuiltin implements Map<String, Value> {
    private final Map<String, Value> dict = new HashMap<>();

    public PythonDict(PythonClass theClass) {
        super(theClass);
    }

    public PythonDict(Context context, Map<String, Value> map) {
        super(((PythonStdLib)context.getStdLib()).getBuiltins().getDictClass());
        dict.putAll(map);
    }

    @Override
    public int size() {
        return dict.size();
    }

    @Override
    public boolean isEmpty() {
        return dict.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return dict.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return dict.containsValue(value);
    }

    @Override
    public Value get(Object key) {
        return dict.get(key);
    }

    @Override
    public @Nullable Value put(String key, Value value) {
        return dict.put(key, value);
    }

    @Override
    public Value remove(Object key) {
        return dict.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Value> m) {
        dict.putAll(m);
    }

    @Override
    public void clear() {
        dict.clear();
    }

    @Override
    public @NotNull Set<String> keySet() {
        return dict.keySet();
    }

    @Override
    public @NotNull Collection<Value> values() {
        return dict.values();
    }

    @Override
    public @NotNull Set<Entry<String, Value>> entrySet() {
        return dict.entrySet();
    }
}
