// This is for Chapter 6 Challenge 2 and 3.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

package Chapter7;

public class Interpreter {
  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
  // Evaluate operands left-to-right to preserve user-visible side effects.
  Object left = evaluate(expr.left);
  Object right = evaluate(expr.right);

  switch (expr.operator.type) {

    // ------------------------------------------------------------
    // Equality stays as-is: any types are comparable for == and !=.
    // ------------------------------------------------------------
    case BANG_EQUAL:
      return !isEqual(left, right);

    case EQUAL_EQUAL:
      return isEqual(left, right);

    // ------------------------------------------------------------
    // Ordering comparisons:
    //   - number vs number => numeric ordering
    //   - string vs string => lexicographic ordering
    //   - everything else  => runtime error
    // ------------------------------------------------------------
    case GREATER:
      if (left instanceof Double && right instanceof Double) {
        return (double) left > (double) right;
      }
      if (left instanceof String && right instanceof String) {
        return ((String) left).compareTo((String) right) > 0;
      }
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

    // ------------------------------------------------------------
    // Arithmetic:
    //   - -, *, / require numbers (same baseline rule as the book)
    //   - / additionally throws a runtime error on division by zero
    // ------------------------------------------------------------
    case MINUS:
      checkNumberOperands(expr.operator, left, right);
      return (double) left - (double) right;

    case STAR:
      checkNumberOperands(expr.operator, left, right);
      return (double) left * (double) right;

    case SLASH:
      checkNumberOperands(expr.operator, left, right);

      // Fail fast instead of returning Infinity/NaN.
      // (Using == 0.0 also catches -0.0.)
      double divisor = (double) right;
      if (divisor == 0.0) {
        throw new RuntimeError(expr.operator, "Division by zero.");
      }

      return (double) left / divisor;

    // ------------------------------------------------------------
    // Plus:
    //   - if either operand is a string, stringify both and concatenate
    //   - otherwise require two numbers and add
    // ------------------------------------------------------------
    case PLUS:
      // If either side is a string, we treat '+' as concatenation.
      // stringify() keeps formatting consistent with the interpreter’s printing.
      if (left instanceof String || right instanceof String) {
        return stringify(left) + stringify(right);
      }

      // Otherwise, this is numeric addition only.
      if (left instanceof Double && right instanceof Double) {
        return (double) left + (double) right;
      }

      throw new RuntimeError(expr.operator,
          "Operands must be two numbers or at least one string.");
  }

    // Unreachable: parser only produces known binary operators.
    return null;
  }
}