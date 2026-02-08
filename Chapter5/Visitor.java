// This visitor walks the syntax tree left-to-right and 
// naturally produces RPN because the AST already encodes 
// operator precedence and evaluation order.

package Chapter5;

// RPN printer visitor
class RpnPrinter implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return expr.left.accept(this) + " "
             + expr.right.accept(this) + " "
             + expr.operator.lexeme;
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return expr.right.accept(this) + " "
             + expr.operator.lexeme;
    }
}
// Written Feb 8 -- Kelly Lwin