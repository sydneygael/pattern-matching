package jep;

/**
 * JEP 361 (Final Java 14)
 * Switch Expressions
 */
public class Jep361_SwitchExpressions {

    // ❌ AVANT : switch statement verbeux avec break et variable externe
    public static String getDayNameAvant(int day) {
        String name;
        switch (day) {
            case 1: name = "Lundi"; break;
            case 2: name = "Mardi"; break;
            case 3: name = "Mercredi"; break;
            case 4: name = "Jeudi"; break;
            case 5: name = "Vendredi"; break;
            case 6: name = "Samedi"; break;
            case 7: name = "Dimanche"; break;
            default: name = "Inconnu"; break;
        }
        return name;
    }

    // ✅ APRÈS JEP 361 : switch expression avec ->
    public static String getDayNameApres(int day) {
        return switch (day) {
            case 1 -> "Lundi";
            case 2 -> "Mardi";
            case 3 -> "Mercredi";
            case 4 -> "Jeudi";
            case 5 -> "Vendredi";
            case 6 -> "Samedi";
            case 7 -> "Dimanche";
            default -> "Inconnu";
        };
    }

    // ✅ APRÈS : multi-case et yield dans un bloc
    public static String classifyDay(int day) {
        return switch (day) {
            case 1, 2, 3, 4, 5 -> "Jour de semaine";
            case 6, 7 -> {
                String type = day == 6 ? "Samedi" : "Dimanche";
                yield type + " (week-end)";
            }
            default -> "Jour invalide";
        };
    }

    // ❌ AVANT : fall-through dangereux (bug classique)
    public static String fallThroughBugAvant(int day) {
        String result = "";
        switch (day) {
            case 6:
                result += "Samedi ";
                // oubli de break → fall-through !
            case 7:
                result += "Dimanche";
                break;
            default:
                result = "Semaine";
        }
        return result.trim();
    }

    // ✅ APRÈS : impossible avec -> (pas de fall-through)
    public static String noFallThroughApres(int day) {
        return switch (day) {
            case 6 -> "Samedi";
            case 7 -> "Dimanche";
            default -> "Semaine";
        };
    }
}
