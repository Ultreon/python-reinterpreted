package dev.ultreon.interpreter.api.symbols;

public interface Symbol {
    Symbol THIS = new Symbol() { };
    Symbol SUPER = new Symbol() { };

    default void print() {
        System.out.println(this);
    }
}
