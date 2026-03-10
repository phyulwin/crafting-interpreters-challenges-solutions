// The following changes are made in the actual forked repository files
// and copied here for assignment submission. 

// Mixins allow code reuse without complex multiple inheritance. 
// A class can include behavior from multiple reusable modules 
// while keeping the inheritance model simple.

package Chapter13;

// Stmt.java
static class Class extends Stmt {
  final Token name;
  final Expr.Variable superclass;
  final List<Expr.Variable> mixins;
  final List<Stmt.Function> methods;

  Class(Token name, Expr.Variable superclass,
        List<Expr.Variable> mixins,
        List<Stmt.Function> methods) {
    this.name = name;
    this.superclass = superclass;
    this.mixins = mixins;
    this.methods = methods;
  }
}

// Parser.java
private Stmt classDeclaration() {
  Token name = consume(IDENTIFIER, "Expect class name.");

  Expr.Variable superclass = null;
  if (match(LESS)) {
    consume(IDENTIFIER, "Expect superclass name.");
    superclass = new Expr.Variable(previous());
  }

  List<Expr.Variable> mixins = new ArrayList<>();
  if (match(WITH)) {
    do {
      consume(IDENTIFIER, "Expect mixin name.");
      mixins.add(new Expr.Variable(previous()));
    } while (match(COMMA));
  }

  consume(LEFT_BRACE, "Expect '{' before class body.");

  List<Stmt.Function> methods = new ArrayList<>();
  while (!check(RIGHT_BRACE) && !isAtEnd()) {
    methods.add(function("method"));
  }

  consume(RIGHT_BRACE, "Expect '}' after class body.");

  return new Stmt.Class(name, superclass, mixins, methods);
}

// Interpreter.java
@Override
public Void visitClassStmt(Stmt.Class stmt) {

  Object superclass = null;
  if (stmt.superclass != null) {
    superclass = evaluate(stmt.superclass);
  }

  environment.define(stmt.name.lexeme, null);

  Map<String, LoxFunction> methods = new HashMap<>();

  for (Stmt.Function method : stmt.methods) {
    LoxFunction function =
        new LoxFunction(method, environment,
        method.name.lexeme.equals("init"));

    methods.put(method.name.lexeme, function);
  }

  if (stmt.mixins != null) {
    for (Expr.Variable mixin : stmt.mixins) {
      Object mixinValue = evaluate(mixin);

      if (mixinValue instanceof LoxClass) {
        LoxClass mixinClass = (LoxClass)mixinValue;

        for (Map.Entry<String, LoxFunction> entry :
             mixinClass.methods.entrySet()) {
          methods.putIfAbsent(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  LoxClass klass =
      new LoxClass(stmt.name.lexeme,
      (LoxClass)superclass,
      methods);

  environment.assign(stmt.name, klass);
  return null;
}
