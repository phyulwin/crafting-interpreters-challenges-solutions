// The following changes are made in the actual forked repository files
// and copied here for assignment submission. 

// Semantics
// Method lookup begins at the root superclass.
// inner() calls the next subclass implementation.

// LoxClass.java
LoxFunction findMethod(String name) {

  if (superclass != null) {
    LoxFunction method = superclass.findMethod(name);
    if (method != null) return method;
  }

  if (methods.containsKey(name)) {
    return methods.get(name);
  }

  return null;
}

// Expr.java
static class Inner extends Expr {
  final Token keyword;

  Inner(Token keyword) {
    this.keyword = keyword;
  }
}

// Parser.java
if (match(INNER)) {
  Token keyword = previous();
  consume(LEFT_PAREN, "Expect '(' after inner.");
  consume(RIGHT_PAREN, "Expect ')' after inner.");
  return new Expr.Inner(keyword);
}

// Resolver.java
@Override
public Void visitInnerExpr(Expr.Inner expr) {
  resolveLocal(expr, expr.keyword);
  return null;
}

// Interpreter.java
@Override
public Object visitInnerExpr(Expr.Inner expr) {

  LoxFunction current =
      (LoxFunction)environment.get(new Token(IDENTIFIER,"this",null,0));

  LoxClass klass = current.getDeclaringClass();
  LoxClass subclass = klass.getSubclass();

  if (subclass == null) return null;

  LoxFunction method =
      subclass.findMethod(current.getName());

  if (method != null) {
    return method.bind(
      (LoxInstance)environment.get(
      new Token(IDENTIFIER,"this",null,0)))
      .call(this, new ArrayList<>());
  }

  return null;
}
