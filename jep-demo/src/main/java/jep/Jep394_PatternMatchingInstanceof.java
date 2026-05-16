package jep;

import java.util.Objects;

/**
 * JEP 305 (Preview Java 14) & JEP 394 (Final Java 16)
 * Pattern Matching for instanceof
 */
public class Jep394_PatternMatchingInstanceof {

    // ❌ AVANT : cast manuel obligatoire
    public static String describeAvant(Object obj) {
        if (obj instanceof String) {
            String s = (String) obj; // cast explicite
            return "String de longueur " + s.length() + " : " + s.toUpperCase();
        } else if (obj instanceof Integer) {
            Integer i = (Integer) obj; // cast explicite
            return "Integer, valeur double = " + (i * 2);
        } else if (obj instanceof Double) {
            Double d = (Double) obj; // cast explicite
            return "Double arrondi = " + Math.round(d);
        }
        return "Type inconnu : " + obj;
    }

    // ✅ APRÈS JEP 394 : pattern variable inline
    public static String describeApres(Object obj) {
        if (obj instanceof String s) {
            return "String de longueur " + s.length() + " : " + s.toUpperCase();
        } else if (obj instanceof Integer i) {
            return "Integer, valeur double = " + (i * 2);
        } else if (obj instanceof Double d) {
            return "Double arrondi = " + Math.round(d);
        }
        return "Type inconnu : " + obj;
    }

    // ✅ APRÈS : portée dans la condition (&&)
    public static boolean isLongString(Object obj) {
        return obj instanceof String s && s.length() > 5;
    }
}
