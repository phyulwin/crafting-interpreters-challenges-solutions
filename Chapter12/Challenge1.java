// The following changes are made in the actual forked repository files
// and copied here for assignment submission. 

package Chapter12;

// Parser.java
private Stmt.Function function(String kind) {
  boolean isStatic = false;
  if (match(CLASS)) isStatic = true;

  Token name = consume(IDENTIFIER, "Expect " + kind + " name.");
  consume(LEFT_PAREN, "Expect '(' after " + kind + " name.");
  List<Token> parameters = new ArrayList<>();

  if (!check(RIGHT_PAREN)) {
    do {
      parameters.add(consume(IDENTIFIER, "Expect parameter name."));
    } while (match(COMMA));
  }

  consume(RIGHT_PAREN, "Expect ')' after parameters.");
  consume(LEFT_BRACE, "Expect '{' before " + kind + " body.");
  List<Stmt> body = block();

  return new Stmt.Function(name, parameters, body, isStatic);
}

// LoxClass.java
class LoxClass extends LoxInstance implements LoxCallable {
  final String name;
  private final Map<String, LoxFunction> methods;
  private final Map<String, LoxFunction> staticMethods;

  LoxClass(String name,
           Map<String, LoxFunction> methods,
           Map<String, LoxFunction> staticMethods) {
    super(null);
    this.name = name;
    this.methods = methods;
    this.staticMethods = staticMethods;
  }

  LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) return methods.get(name);
    return null;
  }

  @Override
  Object get(Token name) {
    if (staticMethods.containsKey(name.lexeme)) {
      return staticMethods.get(name.lexeme);
    }
    return super.get(name);
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }
    return instance;
  }

  @Override
  public int arity() {
    LoxFunction initializer = findMethod("init");
    if (initializer == null) return 0;
    return initializer.arity();
  }
}