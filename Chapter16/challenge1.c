// The following code is for assignment submission only.
// It is not standalone or execution ready.

// TOKEN TYPES
// Added tokens for interpolation
TOKEN_STRING_START,   // beginning of string
TOKEN_STRING_END,     // end of string
TOKEN_STRING_TEXT,    // raw string segment
TOKEN_INTERP_START,   // ${
TOKEN_INTERP_END,     // }

// TOKEN SEQUENCE
// "${drink} will be ready in ${steep + cool} minutes."

STRING_START
INTERP_START
IDENTIFIER(drink)
INTERP_END
STRING_TEXT(" will be ready in ")
INTERP_START
IDENTIFIER(steep)
PLUS
IDENTIFIER(cool)
INTERP_END
STRING_TEXT(" minutes.")
STRING_END

// NESTED INTERPOLATION
// "Nested ${"interpolation?! Are you ${"mad?!"}"}"

STRING_START
STRING_TEXT("Nested ")
INTERP_START

  STRING_START
  STRING_TEXT("interpolation?! Are you ")
  INTERP_START

    STRING_START
    STRING_TEXT("mad?!")
    STRING_END

  INTERP_END
  STRING_END

INTERP_END
STRING_END
