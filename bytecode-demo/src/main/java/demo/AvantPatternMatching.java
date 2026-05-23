package demo;

public class AvantPatternMatching {

    public static double aire(Forme f) {
        if (f instanceof Cercle) {
            Cercle c = (Cercle) f;
            return Math.PI * c.r() * c.r();
        } else if (f instanceof Rectangle) {
            Rectangle r = (Rectangle) f;
            return r.l() * r.h();
        }
        throw new IllegalArgumentException("type inconnu");
    }
}
