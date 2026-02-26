// This is for Chapter 8 Challenge 2.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

package Chapter8;

public class Challenge2 {
    // Do NOT auto-initialize to nil.
    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
    Object value = null;

    if (stmt.initializer != null) {
        value = evaluate(stmt.initializer);
    }

    // Store marker for uninitialized
    environment.define(stmt.name.lexeme, value);

    return null;
    }

    //Throw error if value is null and never assigned:
    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            Object value = values.get(name.lexeme);

            if (value == null) {
            throw new RuntimeError(name,
                "Variable '" + name.lexeme + "' not initialized.");
            }

            return value;
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name,
            "Undefined variable '" + name.lexeme + "'.");
    }
}

// var b; print b; --> runtime error
// a = "value"; print a; --> works