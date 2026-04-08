// The following code is for assignment submission only.
// It is not standalone or execution ready.
/**
 * @brief Add an ownership flag so each ObjString knows whether its characters 
 * should be freed or are borrowed from a constant source buffer. String literals 
 * can safely point into immutable source memory with ownsChars = false, 
 * while concatenated/runtime strings keep ownsChars = true. During object cleanup, 
 * free the character buffer only when the flag says the string owns that memory.
 * 
 */
// Track ownership with a flag.
typedef struct {
  Obj obj;
  int length;
  bool ownsChars;
  char* chars;
} ObjString;

ObjString* makeConstantString(const char* chars, int length) {
  ObjString* string = ALLOCATE_OBJ(ObjString, OBJ_STRING);
  string->length = length;
  string->chars = (char*)chars;
  string->ownsChars = false;
  return string;
}

case OBJ_STRING: {
  ObjString* string = (ObjString*)object;
  if (string->ownsChars) {
    FREE_ARRAY(char, string->chars, string->length + 1);
  }
  FREE(ObjString, object);
  break;
}