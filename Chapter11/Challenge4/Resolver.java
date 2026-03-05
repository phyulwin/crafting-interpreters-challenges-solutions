// Resolver.java 
// Changes for the Challenge 4:
//   - Each scope maps name -> index (Integer) instead of name -> Boolean.
//   - A separate "defined" set tracks which variables are ready.
//   - resolveLocal() passes both depth and index to interpreter.resolve().

package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
  private final Interpreter interpreter;

  // Maps name -> slot index within the scope.
  private final Stack<Map<String, Integer>> scopes  = new Stack<>();
  // Tracks which names are fully defined (initializer done).
  private final Stack<Set<String>>          defined = new Stack<>();

  private FunctionType currentFunction = FunctionType.NONE;

  private enum FunctionType { NONE, FUNCTION }

  Resolver(Interpreter interpreter) {
    this.interpreter = interpreter;
  }

  // ── Statements ───────────────────────────────────────────────────

  @Override
  public Void visitBlockStmt(Stmt.Block stmt) {
    beginScope();
    resolve(stmt.statements);
    endScope();
    return null;
  }

  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    declare(stmt.name);
    if (stmt.initializer != null) resolve(stmt.initializer);
    define(stmt.name);
    return null;
  }

  @Override
  public Void visitFunctionStmt(Stmt.Function stmt) {
    declare(stmt.name);
    define(stmt.name);
    resolveFunction(stmt, FunctionType.FUNCTION);
    return null;
  }

  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override
  public Void visitIfStmt(Stmt.If stmt) {
    resolve(stmt.condition);
    resolve(stmt.thenBranch);
    if (stmt.elseBranch != null) resolve(stmt.elseBranch);
    return null;
  }

  @Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override
  public Void visitReturnStmt(Stmt.Return stmt) {
    if (currentFunction == FunctionType.NONE) {
      Lox.error(stmt.keyword, "Can't return from top-level code.");
    }
    if (stmt.value != null) resolve(stmt.value);
    return null;
  }

  @Override
  public Void visitWhileStmt(Stmt.While stmt) {
    resolve(stmt.condition);
    resolve(stmt.body);
    return null;
  }

  // ── Expressions ──────────────────────────────────────────────────

  @Override
  public Void visitVariableExpr(Expr.Variable expr) {
    if (!scopes.isEmpty()) {
      Map<String, Integer> scope = scopes.peek();
      if (scope.containsKey(expr.name.lexeme) &&
          !defined.peek().contains(expr.name.lexeme)) {
        Lox.error(expr.name,
            "Can't read local variable in its own initializer.");
      }
    }
    resolveLocal(expr, expr.name);
    return null;
  }

  @Override
  public Void visitAssignExpr(Expr.Assign expr) {
    resolve(expr.value);
    resolveLocal(expr, expr.name);
    return null;
  }

  @Override
  public Void visitBinaryExpr(Expr.Binary expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitCallExpr(Expr.Call expr) {
    resolve(expr.callee);
    for (Expr arg : expr.arguments) resolve(arg);
    return null;
  }

  @Override
  public Void visitGroupingExpr(Expr.Grouping expr) {
    resolve(expr.expression);
    return null;
  }

  @Override
  public Void visitLiteralExpr(Expr.Literal expr) {
    return null;
  }

  @Override
  public Void visitLogicalExpr(Expr.Logical expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitUnaryExpr(Expr.Unary expr) {
    resolve(expr.right);
    return null;
  }

  // ── Helpers ──────────────────────────────────────────────────────

  void resolve(List<Stmt> statements) {
    for (Stmt s : statements) resolve(s);
  }

  private void resolve(Stmt stmt)  { stmt.accept(this); }
  private void resolve(Expr expr)  { expr.accept(this); }

  private void beginScope() {
    scopes.push(new HashMap<>());
    defined.push(new HashSet<>());
  }

  private void endScope() {
    scopes.pop();
    defined.pop();
  }

  private void declare(Token name) {
    if (scopes.isEmpty()) return;
    Map<String, Integer> scope = scopes.peek();
    if (scope.containsKey(name.lexeme)) {
      Lox.error(name, "Already a variable with this name in this scope.");
    }
    // Assign the next available slot index.
    scope.put(name.lexeme, scope.size());
  }

  private void define(Token name) {
    if (scopes.isEmpty()) return;
    defined.peek().add(name.lexeme);
  }

  private void resolveLocal(Expr expr, Token name) {
    for (int i = scopes.size() - 1; i >= 0; i--) {
      Map<String, Integer> scope = scopes.get(i);
      if (scope.containsKey(name.lexeme)) {
        int depth = scopes.size() - 1 - i;
        int index = scope.get(name.lexeme);
        interpreter.resolve(expr, depth, index);
        return;
      }
    }
  }

  private void resolveFunction(Stmt.Function function, FunctionType type) {
    FunctionType enclosing = currentFunction;
    currentFunction = type;
    beginScope();
    for (Token param : function.params) {
      declare(param);
      define(param);
    }
    resolve(function.body);
    endScope();
    currentFunction = enclosing;
  }
}
