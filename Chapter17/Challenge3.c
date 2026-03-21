// The following code is for assignment submission only.
// It is not standalone or execution ready.

// FILE: compiler.c

// Add new parse function for '? :'
static void ternary() {
  // Left operand already compiled

  // Parse true branch
  parsePrecedence(PREC_ASSIGNMENT);

  consume(TOKEN_COLON, "Expect ':' in ternary expression.");

  // Parse false branch
  parsePrecedence(PREC_ASSIGNMENT);
}

// ===== PARSE RULE TABLE UPDATE =====
[TOKEN_QUESTION] = {NULL, ternary, PREC_ASSIGNMENT},
