package dev.ultreon.interpreter.api.symbols;

import dev.ultreon.interpreter.api.context.Context;
import dev.ultreon.interpreter.api.obj.InterpClassRef;
import dev.ultreon.interpreter.api.obj.ScriptObject;
import dev.ultreon.interpreter.api.primitives.Primitive;
import dev.ultreon.interpreter.api.InterpInitializerList;
import dev.ultreon.interpreter.api.ClassLike;
import dev.ultreon.interpreter.api.HostClass;
import dev.ultreon.interpreter.api.ObjectLike;
import org.jetbrains.annotations.NotNull;

public class Type implements Symbol {
    private final Primitive type;
    private final Object clazz;
    private @NotNull Context context;

    public Type(Primitive type, Object clazz, @NotNull Context context) {
        this.type = type;
        this.clazz = clazz;
        this.context = context;
    }

    public static Type primitive(Primitive type, @NotNull Context context) {
        if (type == null) {
            throw new RuntimeException("Type is null");
        }

        return new Type(type, null, context);
    }

    public static Type class_(ClassLike clazz) {
        if (clazz == null) {
            throw new RuntimeException("Class is null");
        }
        return new Type(null, clazz, clazz.context());
    }

    public static Object class_(Context context, String visit) {
        if (visit == null) {
            throw new RuntimeException("Class is null");
        }
        return new Type(null, new InterpClassRef(context, visit), context);
    }

    public static Type of(@NotNull Context context, Class<?> parameterType) {
        if (parameterType == null) {
            throw new RuntimeException("Class is null");
        }

        return new Type(null, new HostClass(context, parameterType), context);
    }

    public Value cast(Value value) {
        if (type != null) {
            return type.cast(context, value);
        }
        if (clazz instanceof InterpClassRef ref) {
            ClassLike theClass = ref.getTheClass();
            if (theClass == null) {
                throw new RuntimeException("Class isn't defined");
            }
            return theClass.cast(value);
        } else if (clazz instanceof ClassLike) {
            return ((ClassLike) clazz).cast(value);
        }
        throw new RuntimeException("Class is of unknown type");
    }

    public String name() {
        if (type != null) {
            return type.name();
        }
        if (clazz instanceof InterpClassRef ref) {
            ClassLike theClass = ref.getTheClass();
            if (theClass == null) {
                throw new RuntimeException("Class isn't defined");
            }
            return theClass.name();
        } else if (clazz instanceof ClassLike) {
            return ((ClassLike) clazz).name();
        }
        throw new RuntimeException("Class is of unknown type");
    }

    public Object create(InterpInitializerList visit) {
        switch (clazz) {
            case null -> throw new RuntimeException("This is not a class");
            case InterpClassRef ref -> {
                ClassLike theClass = ref.getTheClass();
                if (theClass == null) {
                    throw new RuntimeException("Class isn't defined");
                }
                ObjectLike construct = theClass.construct();
                construct.initialize(visit, members -> {});
                return construct;
            }
            case ClassLike classType -> {
                ObjectLike construct = classType.construct();
                construct.initialize(visit, members -> {});
                return construct;
            }
            default -> {
            }
        }
        throw new RuntimeException("Class is of unknown type");
    }

    public boolean isInstance(ScriptObject scriptObject) {
        switch (clazz) {
            case null -> throw new RuntimeException("This is not a class");
            case InterpClassRef ref -> {
                ClassLike theClass = ref.getTheClass();
                if (theClass == null) {
                    throw new RuntimeException("Class isn't defined");
                }
                return theClass.isInstance(scriptObject);
            }
            case ClassLike classType -> {
                return classType.isInstance(scriptObject);
            }
            default -> {
            }
        }
        throw new RuntimeException("Class is of unknown type");
    }

    public boolean isInstance(Value arg) {
        if (clazz == null) {
            if (type != null) {
                return type.isInstance(arg);
            }
            return false;
        }

        Object got = arg.get();
        if (!(got instanceof ScriptObject)) {
            return false;
        }

        if (clazz instanceof InterpClassRef ref) {
            ClassLike theClass = ref.getTheClass();
            if (theClass == null) {
                throw new RuntimeException("Class isn't defined");
            }
            return theClass.isInstance((ScriptObject) got);
        } else if (clazz instanceof ClassLike classType) {
            return classType.isInstance((ScriptObject) got);
        }
        throw new RuntimeException("Class is of unknown type");
    }

    public boolean isPrimitive() {
        return type != null;
    }

    public Object defaultValue() {
        if (type != null) {
            return type.defaultValue();
        }
        return null;
    }

    public ClassLike theClass() {
        if (clazz instanceof InterpClassRef ref) {
            ClassLike theClass = ref.getTheClass();
            if (theClass == null) {
                throw new RuntimeException("Class isn't defined");
            }
            return theClass;
        } else if (clazz instanceof ClassLike) {
            return (ClassLike) clazz;
        }
        throw new RuntimeException("Class is of unknown type");
    }

    @Override
    public String toString() {
        if (type != null) {
            return type.toString();
        }
        if (clazz instanceof InterpClassRef ref) {
            ClassLike theClass = ref.getTheClass();
            if (theClass == null) {
                throw new RuntimeException("Class isn't defined");
            }
            return theClass.toString();
        } else if (clazz instanceof ClassLike) {
            return clazz.toString();
        }

        return "<unknown>";
    }
}
