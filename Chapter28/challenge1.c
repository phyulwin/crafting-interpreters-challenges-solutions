// Instead of searching the initializer through the hash table every time,
// I cache the initializer directly inside ObjClass for faster lookup.

typedef struct ObjClass
{
    Obj obj;
    ObjString *name;

    // Cached initializer method.
    Value initializer;

    Table methods;
} ObjClass;

// When a new class is created, the initializer is set to nil by default.

ObjClass *newClass(ObjString *name)
{
    ObjClass *klass = ALLOCATE_OBJ(ObjClass, OBJ_CLASS);

    klass->name = name;

    // No initializer exists yet.
    klass->initializer = NIL_VAL;

    initTable(&klass->methods);

    return klass;
}

// When defining methods, check if the method name is "init".
// If true, cache the initializer directly into the class.

static void defineMethod(ObjString *name)
{
    Value method = peek(0);
    ObjClass *klass = AS_CLASS(peek(1));

    tableSet(&klass->methods, name, method);

    // Cache initializer method for direct access later.
    if (name == vm.initString)
    {
        klass->initializer = method;
    }

    pop();
}

// During class calls, directly invoke the cached initializer.
// This avoids another hash table lookup.

case OBJ_CLASS:
{
    ObjClass *klass = AS_CLASS(callee);

    vm.stackTop[-argCount - 1] =
        OBJ_VAL(newInstance(klass));

    // If initializer exists, call it.
    if (!IS_NIL(klass->initializer))
    {
        return call(
            AS_CLOSURE(klass->initializer),
            argCount);
    }

    // If no initializer exists, constructor arguments are invalid.
    else if (argCount != 0)
    {
        runtimeError(
            "Expected 0 arguments but got %d.",
            argCount);

        return false;
    }

    break;
}