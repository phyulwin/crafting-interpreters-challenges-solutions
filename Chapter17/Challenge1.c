// The parser basically walks through the expression step by step, 
// calling prefix and infix functions while following operator precedence rules.

// The following code is for assignment submission only.
// It is not standalone or execution ready.

// Function Call Trace
// Expression: (-1 + 2) * 3 - -4

expression()
-> parsePrecedence(PREC_ASSIGNMENT)

  advance() -> '('
  prefix: grouping()
    -> expression()
      -> parsePrecedence(PREC_ASSIGNMENT)

        advance() -> '-'
        prefix: unary()
          -> parsePrecedence(PREC_UNARY)

            advance() -> '1'
            prefix: number()

        loop sees '+'
        advance() -> '+'
        infix: binary()
          -> parsePrecedence(PREC_TERM+1)

            advance() -> '2'
            prefix: number()

    consume ')'

  loop sees '*'
  advance() -> '*'
  infix: binary()
    -> parsePrecedence(PREC_FACTOR+1)

      advance() -> '3'
      prefix: number()

  loop sees '-'
  advance() -> '-'
  infix: binary()
    -> parsePrecedence(PREC_TERM+1)

      advance() -> '-'
      prefix: unary()
        -> parsePrecedence(PREC_UNARY)

          advance() -> '4'
          prefix: number()
        