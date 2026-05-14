// Challenge 2: Small String Optimization
// This implementation adds support for inline short strings in clox.
//
// Normally, all strings are heap allocated and stored through ObjString
// pointers. Very small strings waste memory because the pointer itself
// may use more space than the actual characters.
//
// To improve this, OBJ_SHORT_STRING stores short character data directly
// inside the value/object structure instead of allocating separate heap
// memory. This reduces memory usage, lowers allocation overhead, and
// improves cache efficiency for frequently used small strings.

typedef enum {
    OBJ_BOUND_METHOD,
    OBJ_CLASS,
    OBJ_CLOSURE,
    OBJ_FUNCTION,
    OBJ_INSTANCE,
    OBJ_NATIVE,
    OBJ_STRING,
    OBJ_SHORT_STRING,
    OBJ_UPVALUE
} ObjType;

struct ObjShortString {
    Obj obj;
    char chars[8];   // Stores small strings inline.
    uint32_t hash;
};

Value takeStringValue(char* chars, int length) {
    if (length <= SHORT_STRING_MAX) {
        Value value = SHORT_STRING_VAL(chars, length);

        // Free original heap allocation after inline copy.
        FREE_ARRAY(char, chars, length + 1);

        return value;
    }

    return OBJ_VAL(takeString(chars, length));
}