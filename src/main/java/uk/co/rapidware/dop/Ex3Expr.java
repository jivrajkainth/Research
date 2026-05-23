package uk.co.rapidware.dop;

/**
 * Exercise 3: Expression tree — data-oriented interpreter
 *
 * Goal: represent arithmetic expressions as an immutable AST using sealed types,
 * then write multiple interpreters (eval, pretty-print, simplify) as plain functions.
 *
 * This is the classic demonstration of why algebraic data types beat class hierarchies:
 * adding a new operation (a new function) is trivial; the data stays fixed.
 *
 * Key concepts:
 *  - nested sealed hierarchies
 *  - recursive pattern matching
 *  - deconstruction patterns in recursive calls
 *
 * Tasks:
 *  1. Run main() and verify the output matches the comments.
 *  2. Add a Div(Expr left, Expr right) node and handle it in eval() and prettyPrint().
 *  3. Implement simplify(): reduce Mul(_, Num(0)) → Num(0), Add(Num(0), e) → e, etc.
 *  4. Add a variables map: introduce a Var(String name) node and an eval(Expr, Map<String,Double>).
 *  5. Add a differentiate(Expr, String variable) method using the standard calculus rules.
 */
public class Ex3Expr {

    sealed interface Expr permits Ex3Expr.Num, Ex3Expr.Add, Ex3Expr.Mul, Ex3Expr.Neg {}

    record Num(double value)        implements Expr {}
    record Add(Expr left, Expr right) implements Expr {}
    record Mul(Expr left, Expr right) implements Expr {}
    record Neg(Expr expr)           implements Expr {}

    static double eval(Expr e) {
        return switch (e) {
            case Num(var v)          -> v;
            case Add(var l, var r)   -> eval(l) + eval(r);
            case Mul(var l, var r)   -> eval(l) * eval(r);
            case Neg(var inner)      -> -eval(inner);
        };
    }

    static String prettyPrint(Expr e) {
        return switch (e) {
            case Num(var v)          -> v % 1 == 0 ? String.valueOf((int) v) : String.valueOf(v);
            case Add(var l, var r)   -> "(" + prettyPrint(l) + " + " + prettyPrint(r) + ")";
            case Mul(var l, var r)   -> "(" + prettyPrint(l) + " * " + prettyPrint(r) + ")";
            case Neg(var inner)      -> "-" + prettyPrint(inner);
        };
    }

    public static void main(String[] args) {
        // (2 + 3) * -(4)  =  -20
        Expr e1 = new Mul(
            new Add(new Num(2), new Num(3)),
            new Neg(new Num(4))
        );
        System.out.println(prettyPrint(e1) + " = " + eval(e1));  // ((2 + 3) * -4) = -20.0

        // 1 + 2 * 3  =  7
        Expr e2 = new Add(
            new Num(1),
            new Mul(new Num(2), new Num(3))
        );
        System.out.println(prettyPrint(e2) + " = " + eval(e2));  // (1 + (2 * 3)) = 7.0
    }
}
