// This is for Chapter 9 Challenge 1.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

package Chapter9;

public class Challenge1 {
    // Implement if Using First-Class Functions
    // Assume functions are first-class and only the chosen branch is executed.
    fun ifElse(condition, thenFn, elseFn) {
    if (condition) {
        return thenFn();
    } else {
        return elseFn();
    }
    }

    // Usage:
    ifElse(a > 10,
    fun () { print "big"; },
    fun () { print "small"; }
    );

    // Conditional execution done via function dispatch
    // Only selected branch executes
    // Language That Uses This
    // Smalltalk (control flow implemented via message dispatch on booleans)
}
