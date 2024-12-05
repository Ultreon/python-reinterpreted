package dev.ultreon.interpreter.api.obj;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.api.FuncCall;

public interface FCInvokable {
    Value invoke(Context context, FuncCall visit);
}
