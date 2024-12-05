package dev.ultreon.interpreter.api.primitives;

public class Int128 {
    private final long high;
    private final long low;

    public Int128(long high, long low) {
        this.high = high;
        this.low = low;
    }

    public Int128() {
        this(0, 0);
    }
}
