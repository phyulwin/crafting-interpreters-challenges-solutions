// This is for Chapter 8 Challenge 1.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

package Chapter8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Lox {
    // In runPrompt()
    private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
        System.out.print("> ");
        String line = reader.readLine();
        if (line == null) break;

        runRepl(line); // custom method
        hadError = false;
    }
    }

    // Add this method

    private static void runRepl(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);

    try {
        List<Stmt> statements = parser.parse();
        if (!hadError) interpreter.interpret(statements);
    } catch (Exception e) {
        // fallback: try single expression
        Parser exprParser = new Parser(tokens);
        Expr expr = exprParser.expression();
        Object value = interpreter.evaluate(expr);
        System.out.println(interpreter.stringify(value));
    }
    }
}
