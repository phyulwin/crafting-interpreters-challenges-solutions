// This is for Chapter 6 Challenge 2 and 3.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

@Override
public Object visitBinaryExpr(Expr.Binary expr) {
  // Evaluate operands left-to-right, preserving any side effects in order.
  Object left = evaluate(expr.left);
  Object right = evaluate(expr.right);

  // Dispatch based on the operator token type.
  switch (expr.operator.type) {

    // ----------------------------
    // Ordering comparisons
    // Policy:
    //   - number-number: numeric ordering
    //   - string-string: lexicographic ordering
    //   - anything else: runtime error (no mixed-type ordering)
    // ----------------------------

    case GREATER:
      // Numeric ordering.
      if (left instanceof Double && right instanceof Double) {
        return (double) left > (double) right;
      }
      // Lexicographic string ordering using Java's built-in compareTo().
      if (left instanceof String && right instanceof String) {
        return ((String) left).compareTo((String) right) > 0;
      }
      // Mixed or unsupported types are rejected to avoid surprising behavior.
      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or two strings.");

    case GREATER_EQUAL:
      if (left instanceof Double && right instanceof Double) {
        return (double) left >= (double) right;
      }
      if (left instanceof String && right instanceof String) {
        return ((String) left).compareTo((String) right) >= 0;
      }
      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or two strings.");

    case LESS:
      if (left instanceof Double && right instanceof Double) {
        return (double) left < (double) right;
      }
      if (left instanceof String && right instanceof String) {
        return ((String) left).compareTo((String) right) < 0;
      }
      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or two strings.");

    case LESS_EQUAL:
      if (left instanceof Double && right instanceof Double) {
        return (double) left <= (double) right;
      }
      if (left instanceof String && right instanceof String) {
        return ((String) left).compareTo((String) right) <= 0;
      }
      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or two strings.");

    // ----------------------------
    // Equality comparisons
    // Policy:
    //   - Equality is defined across all types via isEqual().
    //   - No type restrictions here (keeps Lox flexible and consistent).
    // ----------------------------

    case BANG_EQUAL:
      return !isEqual(left, right);

    case EQUAL_EQUAL:
      return isEqual(left, right);

    // ----------------------------
    // Arithmetic operators (numbers only)
    // Policy:
    //   - -, *, / are strictly numeric
    //   - / additionally checks divide-by-zero and raises RuntimeError
    // ----------------------------

    case MINUS:
      // Ensures both operands are numbers, otherwise throws RuntimeError.
      checkNumberOperands(expr.operator, left, right);
      return (double) left - (double) right;

    case STAR:
      checkNumberOperands(expr.operator, left, right);
      return (double) left * (double) right;

    case SLASH:
      checkNumberOperands(expr.operator, left, right);

      // Fail fast on divide-by-zero instead of producing Infinity/NaN.
      // Using == 0.0 also catches -0.0.
      double divisor = (double) right;
      if (divisor == 0.0) {
        throw new RuntimeError(expr.operator, "Division by zero.");
      }

      return (double) left / divisor;

    // ----------------------------
    // Addition / concatenation
    // Policy:
    //   - If either operand is a string, stringify both and concatenate.
    //   - Otherwise, both must be numbers and we do numeric addition.
    // ----------------------------

    case PLUS:
      // String concatenation path:
      // If either side is a string, we convert using stringify() so that
      // numbers, nil, booleans, etc. get the same textual form Lox prints.
      if (left instanceof String || right instanceof String) {
        return stringify(left) + stringify(right);
      }

      // Numeric addition path:
      // Only valid if both operands are numbers.
      if (left instanceof Double && right instanceof Double) {
        return (double) left + (double) right;
      }

      // Anything else is an error (e.g., true + 2, nil + "x" is handled above).
      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or at least one string.");
  }

  // Unreachable: the parser only produces known binary operators.
  return null;
}
