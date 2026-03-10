Challenge3lowing changes are made in the actual forked repository files
// and copied here for assignment submission. 

// Additional Feature Implemented: List Type

// LoxList.java
class LoxList {

  private final List<Object> values;

  LoxList(List<Object> values) {
    this.values = values;
  }

  Object get(int index) {
    return values.get(index);
  }

  void set(int index, Object value) {
    values.set(index, value);
  }

  void add(Object value) {
    values.add(value);
  }

  @Override
  public String toString() {
    return values.toString();
  }
}

// Interpreter.java
@Override
public Object visitListExpr(Expr.List expr) {

  List<Object> values = new ArrayList<>();

  for (Expr element : expr.elements) {
    values.add(evaluate(element));
  }

  return new LoxList(values);
}
