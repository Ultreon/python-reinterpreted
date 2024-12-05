package dev.ultreon.interpreter.api.primitives;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.symbols.Value;
import org.jetbrains.annotations.NotNull;

public enum Primitive {
    BOOL,
    INT8,
    INT16,
    INT32,
    INT64,
    INT128,
    UINT8,
    UINT16,
    UINT32,
    UINT64,
    UINT128,
    FLOAT32,
    FLOAT64,
    FLOAT128,
    CHAR,
    WCHAR,
    STRING,
    WSTRING,
    BOOL_ARRAY,
    INT8_ARRAY,
    INT16_ARRAY,
    INT32_ARRAY,
    INT64_ARRAY,
    INT128_ARRAY,
    UINT8_ARRAY,
    UINT16_ARRAY,
    UINT32_ARRAY,
    UINT64_ARRAY,
    UINT128_ARRAY,
    FLOAT32_ARRAY,
    FLOAT64_ARRAY,
    FLOAT128_ARRAY,
    CHAR_ARRAY,
    WCHAR_ARRAY,
    STRING_ARRAY,
    WSTRING_ARRAY,
    VOID,
    ;

    public Value cast(@NotNull Context context, Value value) {
        return Value.of(context, switch (this) {
            case BOOL -> (boolean) value.get();
            case INT8, UINT8 -> (byte) value.get();
            case INT16, UINT16 -> (short) value.get();
            case INT32, UINT32 -> (int) value.get();
            case INT64, UINT64 -> (long) value.get();
            case INT128, UINT128 -> (Int128) value.get();
            case FLOAT32 -> (float) value.get();
            case FLOAT64 -> (double) value.get();
            case FLOAT128 -> (Float128) value.get();
            case CHAR -> (char) value.get();
            case WCHAR -> (char) value.get();
            case STRING -> (String) value.get();
            case WSTRING -> (String) value.get();
            case BOOL_ARRAY -> (boolean[]) value.get();
            case INT8_ARRAY -> (byte[]) value.get();
            case INT16_ARRAY -> (short[]) value.get();
            case INT32_ARRAY -> (int[]) value.get();
            case INT64_ARRAY -> (long[]) value.get();
            case INT128_ARRAY -> (long[]) value.get();
            case UINT8_ARRAY -> (byte[]) value.get();
            case UINT16_ARRAY -> (short[]) value.get();
            case UINT32_ARRAY -> (int[]) value.get();
            case UINT64_ARRAY -> (long[]) value.get();
            case UINT128_ARRAY -> (long[]) value.get();
            case FLOAT32_ARRAY -> (float[]) value.get();
            case FLOAT64_ARRAY -> (double[]) value.get();
            case FLOAT128_ARRAY -> (double[]) value.get();
            case CHAR_ARRAY -> (char[]) value.get();
            case WCHAR_ARRAY -> (char[]) value.get();
            case STRING_ARRAY -> (String[]) value.get();
            case WSTRING_ARRAY -> (String[]) value.get();
            case VOID -> null;
        });
    }

    public boolean isInstance(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (arg instanceof ScriptObject scriptObject) {
            throw new IllegalArgumentException("Argument cannot be an InterpObject");
        }

        if (arg instanceof Byte) {
            return this == Primitive.INT8 || this == Primitive.UINT8;
        }

        if (arg instanceof Short) {
            return this == Primitive.INT16 || this == Primitive.UINT16;
        }

        if (arg instanceof Integer) {
            return this == Primitive.INT32 || this == Primitive.UINT32;
        }

        if (arg instanceof Long) {
            return this == Primitive.INT64 || this == Primitive.UINT64;
        }

        if (arg instanceof Float) {
            return this == Primitive.FLOAT32;
        }

        if (arg instanceof Double) {
            return this == Primitive.FLOAT64;
        }

        if (arg instanceof Character) {
            return this == Primitive.CHAR;
        }

        if (arg instanceof String) {
            return this == Primitive.STRING || this == Primitive.WSTRING;
        }

        if (arg instanceof Boolean) {
            return this == Primitive.BOOL;
        }

        if (arg instanceof byte[]) {
            return this == Primitive.INT8_ARRAY || this == Primitive.UINT8_ARRAY;
        }

        if (arg instanceof short[]) {
            return this == Primitive.INT16_ARRAY || this == Primitive.UINT16_ARRAY;
        }

        if (arg instanceof int[]) {
            return this == Primitive.INT32_ARRAY || this == Primitive.UINT32_ARRAY;
        }

        if (arg instanceof long[]) {
            return this == Primitive.INT64_ARRAY || this == Primitive.UINT64_ARRAY;
        }

        if (arg instanceof float[]) {
            return this == Primitive.FLOAT32_ARRAY;
        }

        if (arg instanceof double[]) {
            return this == Primitive.FLOAT64_ARRAY;
        }
        return false;
    }

    public boolean isInstanceOrCastable(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (arg instanceof ScriptObject scriptObject) {
            throw new IllegalArgumentException("Argument cannot be an InterpObject");
        }

        if (arg instanceof Byte) {
            return this == Primitive.INT8 || this == Primitive.UINT8 || this == Primitive.INT16 || this == Primitive.UINT16 || this == Primitive.INT32 || this == Primitive.UINT32 || this == Primitive.INT64 || this == Primitive.UINT64;
        }

        if (arg instanceof Short) {
            return this == Primitive.INT16 || this == Primitive.UINT16 || this == Primitive.INT32 || this == Primitive.UINT32 || this == Primitive.INT64 || this == Primitive.UINT64;
        }

        if (arg instanceof Integer) {
            return this == Primitive.INT32 || this == Primitive.UINT32 || this == Primitive.INT64 || this == Primitive.UINT64;
        }

        if (arg instanceof Long) {
            return this == Primitive.INT64 || this == Primitive.UINT64;
        }

        if (arg instanceof Float) {
            return this == Primitive.FLOAT32 || this == Primitive.FLOAT64;
        }

        if (arg instanceof Double) {
            return this == Primitive.FLOAT64;
        }

        if (arg instanceof Character) {
            return this == Primitive.CHAR;
        }

        if (arg instanceof String) {
            return this == Primitive.STRING || this == Primitive.WSTRING;
        }

        if (arg instanceof Boolean) {
            return this == Primitive.BOOL;
        }

        if (arg instanceof byte[]) {
            return this == Primitive.INT8_ARRAY;
        }

        if (arg instanceof short[]) {
            return this == Primitive.INT16_ARRAY;
        }

        if (arg instanceof int[]) {
            return this == Primitive.INT32_ARRAY;
        }

        if (arg instanceof long[]) {
            return this == Primitive.INT64_ARRAY;
        }

        if (arg instanceof float[]) {
            return this == Primitive.FLOAT32_ARRAY;
        }

        if (arg instanceof double[]) {
            return this == Primitive.FLOAT64_ARRAY;
        }
        return false;
    }

    public Object defaultValue() {
        return switch (this) {
            case BOOL -> false;
            case INT8, UINT8 -> (byte) 0;
            case INT16, UINT16 -> (short) 0;
            case INT32, UINT32 -> 0;
            case INT64, UINT64 -> (long) 0;
            case INT128, UINT128 -> new Int128();
            case FLOAT32 -> (float) 0;
            case FLOAT64 -> (double) 0;
            case FLOAT128 -> new Float128();
            case CHAR, WCHAR -> (char) 0;
            case STRING, WSTRING -> "";
            case BOOL_ARRAY -> new boolean[0];
            case INT8_ARRAY, UINT8_ARRAY -> new byte[0];
            case INT16_ARRAY, UINT16_ARRAY -> new short[0];
            case INT32_ARRAY, UINT32_ARRAY -> new int[0];
            case INT64_ARRAY, UINT64_ARRAY -> new long[0];
            case FLOAT32_ARRAY -> new float[0];
            case FLOAT64_ARRAY -> new double[0];
            case FLOAT128_ARRAY -> new Float128[0];
            default -> throw new UnsupportedOperationException("Not implemented yet!");
        };
    }
}
