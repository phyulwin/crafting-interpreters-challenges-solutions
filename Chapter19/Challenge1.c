// The following code is for assignment submission only.
// It is not standalone or execution ready.
/**
* @brief A flexible array member moves the character bytes directly inside ObjString, 
* so the object header and string contents are stored in one contiguous heap block. 
* This removes the second allocation for char* and reduces pointer chasing from 
* two memory lookups to one. It improves cache locality and makes string access faster 
* while keeping the same external VM behavior. 
*/
// Use a flexible array member so ObjString and its bytes live in one contiguous block
// This removes the second allocation and avoids the extra pointer indirection.
// object.h
typedef struct {
  Obj obj;
  int length;
  char chars[];
} ObjString;

// object.c
ObjString* copyString(const char* chars, int length) {
  ObjString* string =
      (ObjString*)allocateObject(sizeof(ObjString) + length + 1, OBJ_STRING);
  string->length = length;
  memcpy(string->chars, chars, length);
  string->chars[length] = '\0';
  return string;
}
