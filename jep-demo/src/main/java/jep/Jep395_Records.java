package jep;

import java.util.Objects;

/**
 * JEP 395 (Final Java 16)
 * Records
 */
public class Jep395_Records {

    // ❌ AVANT : POJO classique (~30 lignes de boilerplate)
    public static class PointAvant {
        private final int x;
        private final int y;

        public PointAvant(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PointAvant)) return false;
            PointAvant p = (PointAvant) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "PointAvant[x=" + x + ", y=" + y + "]";
        }
    }

    // ✅ APRÈS JEP 395 : Record en 1 ligne !
    public record Point(int x, int y) {

        // Constructeur compact avec validation
        public Point {
            if (x < 0 || y < 0) throw new IllegalArgumentException("Coordonnées négatives interdites");
        }

        // Méthode custom
        public double distanceOrigine() {
            return Math.sqrt(x * (double) x + y * (double) y);
        }
    }

    // Record imbriqué
    public record Ligne(Point debut, Point fin) {
        public double longueur() {
            int dx = fin.x() - debut.x();
            int dy = fin.y() - debut.y();
            return Math.sqrt(dx * (double) dx + dy * (double) dy);
        }
    }
}
