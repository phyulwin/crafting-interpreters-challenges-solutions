// The following changes are made in the actual forked repository files
// and copied here for assignment submission. 

package Chapter12;

// Parser.java
if (check(IDENTIFIER) && checkNext(LEFT_BRACE)) {
  Token name = consume(IDENTIFIER, "Expect getter name.");
  consume(LEFT_BRACE, "Expect '{' before getter body.");
  List<Stmt> body = block();
  return new Stmt.Function(name, new ArrayList<>(), body, false, true);
}

// Modying the LoxFunction
class LoxFunction implements LoxCallable {
  private final boolean isGetter;

  LoxFunction(Stmt.Function declaration, Environment closure,
              boolean isInitializer, boolean isGetter) {
    this.declaration = declaration;
    this.closure = closure;
    this.isInitializer = isInitializer;
    this.isGetter = isGetter;
  }

  boolean isGetter() {
    return isGetter;
  }
}

// Modify instance property lookup:
// LoxInstance.java
Object get(Token name) {
  if (fields.containsKey(name.lexeme)) {
    return fields.get(name.lexeme);
  }

  LoxFunction method = klass.findMethod(name.lexeme);
  if (method != null) {
    if (method.isGetter()) {
      return method.bind(this).call(null, new ArrayList<>());
    }
    return method.bind(this);
  }

  throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
}
