// Interpreter.java (relevant changes only)
// resolve() now accepts depth + index.
// lookUpVariable() and visitAssignExpr() use getAt(depth, index) / assignAt(depth, index, value).

  // Replace the existing resolve() method:
  void resolve(Expr expr, int depth, int index) {
    locals.put(expr, new int[]{depth, index});
  }

  // Replace the existing lookUpVariable():
  private Object lookUpVariable(Token name, Expr expr) {
    int[] location = locals.get(expr);
    if (location != null) {
      return environment.getAt(location[0], location[1]);
    } else {
      return globals.get(name);
    }
  }

  // Replace the assignment block inside visitAssignExpr():
  @Override
  public Object visitAssignExpr(Expr.Assign expr) {
    Object value = evaluate(expr.value);
    int[] location = locals.get(expr);
    if (location != null) {
      environment.assignAt(location[0], location[1], value);
    } else {
      globals.assign(expr.name, value);
    }
    return value;
  }

  // Change the locals field type:
  private final Map<Expr, int[]> locals = new HashMap<>();
