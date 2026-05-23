package jep;

/**
 * JEP 409 (Final Java 17) - Sealed Classes
 * JEP 441 (Final Java 21) - Pattern Matching for Switch
 * JEP 440 (Final Java 21) - Record Patterns
 */
public class Jep409_441_SealedAndSwitch {

    // ========== JEP 409 : Sealed Classes ==========

    // ❌ AVANT : hiérarchie ouverte, n'importe qui peut hériter
    public static abstract class ShapeAvant {
        public abstract double area();
    }
    public static class CercleAvant extends ShapeAvant {
        private final double radius;
        public CercleAvant(double radius) { this.radius = radius; }
        @Override public double area() { return Math.PI * radius * radius; }
        public double getRadius() { return radius; }
    }

    public static class RectangleAvant extends ShapeAvant {
        private final double w, h;
        public RectangleAvant(double w, double h) { this.w = w; this.h = h; }
        @Override public double area() { return w * h; }
        public double getW() { return w; }
        public double getH() { return h; }
    }

    // ✅ APRÈS JEP 409 : hiérarchie scellée
    public sealed interface Shape permits Cercle, Rectangle, Triangle {}

    public record Cercle(double radius)      implements Shape {}
    public record Rectangle(double w, double h) implements Shape {}
    public non-sealed interface Triangle     extends Shape {}
    public record TriangleRect(double base, double hauteur) implements Triangle {}

    // ========== JEP 441 : Pattern Matching for Switch ==========

    // ❌ AVANT : cascade de if/instanceof avec cast
    public static String describeAvant(Object obj) {
        if (obj instanceof CercleAvant c) {
            return "Cercle de rayon " + c.getRadius();
        } else if (obj instanceof RectangleAvant r) {
            return "Rectangle " + r.getW() + "x" + r.getH();
        } else if (obj == null) {
            return "Nul";
        } else {
            return "Inconnu";
        }
    }

    // ✅ APRÈS JEP 441 : switch avec patterns et null
    public static String describeApres(Object obj) {
        return switch (obj) {
            case Cercle c    -> "Cercle de rayon " + c.radius();
            case Rectangle r -> "Rectangle " + r.w() + "x" + r.h();
            case null        -> "Nul";
            default          -> "Inconnu : " + obj;
        };
    }

    // ✅ APRÈS : guarded patterns avec when
    public static String classifyShape(Shape shape) {
        return switch (shape) {
            case Cercle c    when c.radius() > 10 -> "Grand cercle";
            case Cercle c                         -> "Petit cercle";
            case Rectangle r when r.w() == r.h()  -> "Carré de côté " + r.w();
            case Rectangle r                      -> "Rectangle " + r.w() + "x" + r.h();
            case Triangle t                       -> "Triangle";
        };
    }

    // ✅ JEP 440 + 441 : Record Patterns dans switch — exhaustif grâce aux sealed
    public static double computeArea(Shape shape) {
        return switch (shape) {
            case Cercle(var r)       -> Math.PI * r * r;
            case Rectangle(var w, var h) -> w * h;
            case TriangleRect(var b, var h) -> 0.5 * b * h;
            case Triangle t          -> 0.0; // non-sealed : default nécessaire
        };
    }
}
