package uk.co.rapidware.dop;

/**
 * Exercise 2: Result<T> — modelling success/failure as data instead of exceptions
 *
 * Goal: build a generic Result type (like Rust's Result or Haskell's Either) using
 * sealed interfaces and records, then chain operations on it.
 *
 * Key concepts:
 *  - generic sealed interfaces
 *  - record deconstruction in switch
 *  - replacing exception-driven control flow with explicit typed errors
 *
 * Tasks:
 *  1. Run main() and trace the two pipelines through the code.
 *  2. Implement map() so that Success applies a function and Failure passes through unchanged.
 *  3. Implement flatMap() for chaining operations that themselves return a Result.
 *  4. Add a recover(Function<String, T>) method that converts a Failure into a Success.
 *  5. Implement a method sequence(List<Result<T>>) that returns Result<List<T>> —
 *     succeeds only if all elements succeed, fails on the first failure.
 */
public class Ex2Result {

    sealed interface Result<T> permits Result.Success, Result.Failure {

        record Success<T>(T value) implements Result<T> {}
        record Failure<T>(String error) implements Result<T> {}

        static <T> Result<T> of(java.util.function.Supplier<T> supplier) {
            try {
                return new Success<>(supplier.get());
            } catch (Exception e) {
                return new Failure<>(e.getMessage());
            }
        }

        // TODO Exercise task 2: implement map()
        default <U> Result<U> map(java.util.function.Function<T, U> fn) {
            throw new UnsupportedOperationException("implement me");
        }

        // TODO Exercise task 3: implement flatMap()
        default <U> Result<U> flatMap(java.util.function.Function<T, Result<U>> fn) {
            throw new UnsupportedOperationException("implement me");
        }

        default String display() {
            return switch (this) {
                case Success<T>(var v) -> "OK: " + v;
                case Failure<T>(var e) -> "ERR: " + e;
            };
        }
    }

    static Result<Integer> parseInt(String s) {
        return Result.of(() -> Integer.parseInt(s));
    }

    static Result<Integer> safeDivide(int a, int b) {
        if (b == 0) return new Result.Failure<>("division by zero");
        return new Result.Success<>(a / b);
    }

    public static void main(String[] args) {
        // Happy path
        Result<Integer> r1 = parseInt("100")
            .flatMap(n -> safeDivide(n, 4));
        System.out.println(r1.display());  // OK: 25

        // Failure path — parseInt fails, flatMap should short-circuit
        Result<Integer> r2 = parseInt("abc")
            .flatMap(n -> safeDivide(n, 4));
        System.out.println(r2.display());  // ERR: For input string: "abc"

        // Division by zero
        Result<Integer> r3 = parseInt("42")
            .flatMap(n -> safeDivide(n, 0));
        System.out.println(r3.display());  // ERR: division by zero
    }
}
