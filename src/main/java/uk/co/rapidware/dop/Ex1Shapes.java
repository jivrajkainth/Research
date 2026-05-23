package uk.co.rapidware.dop;

/**
 * Exercise 1: Shapes — sealed interfaces + records + pattern matching switch
 *
 * Goal: model a closed set of shapes and compute area/perimeter without instanceof chains.
 *
 * Key concepts:
 *  - sealed interface restricts which classes can implement it
 *  - record provides a compact, immutable data carrier with auto equals/hashCode/toString
 *  - switch with pattern matching dispatches on type and binds the variable in one step
 *  - the compiler enforces exhaustiveness — forgetting a case is a compile error
 *
 * Tasks:
 *  1. Run main() and read the output.
 *  2. Add a Triangle record (base, height, sideA, sideB, sideC) to the sealed hierarchy.
 *  3. Add Triangle cases to area() and perimeter() — the compiler will tell you if you miss one.
 *  4. Add a guarded pattern: in describe(), print "large circle" when radius > 10.
 *  5. Add a method renderSVG(Shape s) that returns an SVG string for each shape.
 */
public class Ex1Shapes {

    sealed interface Shape permits Circle, Rectangle, Square {}

    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Square(double side) implements Shape {}

    static double area(Shape s) {
        return switch (s) {
            case Circle(var r)            -> Math.PI * r * r;
            case Rectangle(var w, var h)  -> w * h;
            case Square(var side)         -> side * side;
        };
    }

    static double perimeter(Shape s) {
        return switch (s) {
            case Circle(var r)            -> 2 * Math.PI * r;
            case Rectangle(var w, var h)  -> 2 * (w + h);
            case Square(var side)         -> 4 * side;
        };
    }

    static String describe(Shape s) {
        return switch (s) {
            case Circle c when c.radius() > 10 -> "large circle, radius=" + c.radius();
            case Circle(var r)                  -> "circle, radius=" + r;
            case Rectangle(var w, var h)        -> "rectangle " + w + "x" + h;
            case Square(var side)               -> "square, side=" + side;
        };
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5),
            new Circle(15),
            new Rectangle(4, 7),
            new Square(3)
        };

        for (Shape s : shapes) {
            System.out.printf("%s  area=%.2f  perimeter=%.2f%n",
                describe(s), area(s), perimeter(s));
        }
    }
}
