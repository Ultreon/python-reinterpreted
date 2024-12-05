package dev.ultreon.interpreter.api.symbols;

import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.primitives.Primitive;
import org.jetbrains.annotations.NotNull;

public class PrimitiveValue extends Value {
    private final Primitive type;
    private @NotNull Context context;

    public PrimitiveValue(@NotNull Context context, Object value) {
        super(value);
        this.context = context;
        switch (value) {
            case Boolean b -> this.type = Primitive.BOOL;
            case Byte b -> this.type = Primitive.INT8;
            case Short i -> this.type = Primitive.INT16;
            case Integer i -> this.type = Primitive.INT32;
            case Long l -> this.type = Primitive.INT64;
            case Float v -> this.type = Primitive.FLOAT32;
            case Double v -> this.type = Primitive.FLOAT64;
            case String s -> this.type = Primitive.STRING;
            case Character c -> this.type = Primitive.CHAR;
            case null -> this.type = Primitive.VOID;
            default -> throw new InterpreterException("Unknown primitive type: " + value.getClass().getName());
        }
        this.ptr = value;
    }

    @Override
    public boolean isNull() {
        return super.isNull() || type == Primitive.VOID;
    }

    @Override
    public Value xor(Value visit) {
        return PrimitiveValue.of(context, switch (type) {
            case BOOL -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.BOOL ? (boolean) visit.get() ^ (boolean) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT8, UINT8 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT8 ? (byte) visit.get() ^ (byte) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT16, UINT16 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT16 ? (short) visit.get() ^ (short) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT32, UINT32 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT32 ? (int) visit.get() ^ (int) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT64, UINT64 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT64 ? (long) visit.get() ^ (long) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            default -> throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
        });
    }

    @Override
    public Value plus(Value visit) {
        return PrimitiveValue.of(context, switch (type) {
            case INT8, UINT8 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT8 ? (byte) visit.get() + (byte) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT16, UINT16 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT16 ? (short) visit.get() + (short) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT32, UINT32 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT32 ? (int) visit.get() + (int) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            case INT64, UINT64 -> {
                if (visit instanceof PrimitiveValue) {
                    yield ((PrimitiveValue) visit).type == Primitive.INT64 ? (long) visit.get() + (long) this.get() : this;
                }
                throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
            }
            default -> throw new InterpreterException("Unknown primitive type: " + visit.getClass().getName());
        });
    }

    public Primitive getType() {
        return type;
    }
}
