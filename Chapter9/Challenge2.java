// This is for Chapter 9 Challenge 2.
// This code isn’t meant to be executed. It’s just written to answer the challenge question.

package Chapter9;

public class Challenge2 {
    // Looping can be implemented via recursion:
    fun loop(conditionFn, bodyFn) {
    if (conditionFn()) {
        bodyFn();
        loop(conditionFn, bodyFn);
    }
    }
    // Usage:
    var i = 0;
    loop(
    fun () { return i < 5; },
    fun () {
        print i;
        i = i + 1;
    }
    );
}

/* 
Required Optimization
    Tail Call Optimization (TCO)
Why Necessary
Without TCO:
    Each recursive call consumes stack space
    Infinite or large loops cause stack overflow
With TCO:
    Recursive calls reuse stack frame
    Behaves like iterative loop
Language That Uses This
    Scheme (iteration via tail-recursive functions) 
*/
