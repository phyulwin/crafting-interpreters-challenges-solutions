-- Typeclass = bundle of operations for an expression type.
-- This plays the same role as a class with methods in OOP.
-- Any type that wants to behave like an expression must implement these.
class ExprOps e where
  eval   :: e -> Double      -- Evaluate the expression to a value
  pretty :: e -> String      -- Convert the expression to a printable form

-- A concrete expression type representing a literal number.
-- This is equivalent to adding a new row (type) to the expression table.
data Lit = Lit Double

-- Instance declaration ties *all operations* for Lit together.
-- This is the key difference from the Visitor pattern.
instance ExprOps Lit where
  eval (Lit n) = n           -- Evaluating a literal returns its value
  pretty (Lit n) = show n    -- Printing a literal just shows the number

-- A second concrete expression type representing addition.
-- Adding a new type requires defining its structure and implementing the class.
data Add = Add Lit Lit

-- All behavior for Add is defined here, in one place.
-- No existing code needs to be modified to add this new expression type.
instance ExprOps Add where
  eval (Add a b) = eval a + eval b
  pretty (Add a b) =
    "(" ++ pretty a ++ " + " ++ pretty b ++ ")"