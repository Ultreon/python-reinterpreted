package dev.ultreon.interpreter.python3;

import dev.ultreon.interpreter.api.FuncCall;
import dev.ultreon.interpreter.api.symbols.Value;

import java.util.Map;

public record PyFuncCall(
    Map<String, Value> kwargs,
    Value[] args
) implements FuncCall {

}
