package demo;

public class ApresPatternMatching {

    public static double aire(Forme f) {
        return switch (f) {
            case Cercle c -> Math.PI * c.r() * c.r();
            case Rectangle r -> r.l() * r.h();
        };
    }
}

