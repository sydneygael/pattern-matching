package jep;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Jep361Test {

    @Test public void nomJour_avant_lundi() { assertThat(Jep361_SwitchExpressions.getDayNameAvant(1)).isEqualTo("Lundi"); }
    @Test public void nomJour_avant_vendredi() { assertThat(Jep361_SwitchExpressions.getDayNameAvant(5)).isEqualTo("Vendredi"); }
    @Test public void nomJour_avant_inconnu() { assertThat(Jep361_SwitchExpressions.getDayNameAvant(99)).isEqualTo("Inconnu"); }

    @Test public void nomJour_apres_lundi() { assertThat(Jep361_SwitchExpressions.getDayNameApres(1)).isEqualTo("Lundi"); }
    @Test public void nomJour_apres_vendredi() { assertThat(Jep361_SwitchExpressions.getDayNameApres(5)).isEqualTo("Vendredi"); }
    @Test public void nomJour_apres_inconnu() { assertThat(Jep361_SwitchExpressions.getDayNameApres(99)).isEqualTo("Inconnu"); }

    @Test public void avant_egale_apres() {
        for (int d = 1; d <= 8; d++)
            assertThat(Jep361_SwitchExpressions.getDayNameApres(d))
                .as("Divergence jour " + d)
                .isEqualTo(Jep361_SwitchExpressions.getDayNameAvant(d));
    }

    @Test public void classifyWeekdays() {
        for (int d = 1; d <= 5; d++)
            assertThat(Jep361_SwitchExpressions.classifyDay(d)).isEqualTo("Jour de semaine");
    }
    @Test public void classifySamedi() { assertThat(Jep361_SwitchExpressions.classifyDay(6)).isEqualTo("Samedi (week-end)"); }
    @Test public void classifyDimanche() { assertThat(Jep361_SwitchExpressions.classifyDay(7)).isEqualTo("Dimanche (week-end)"); }

    @Test public void fallThroughBug_case6() {
        // Bug classique : case 6 sans break tombe dans case 7 → accumule les deux
        assertThat(Jep361_SwitchExpressions.fallThroughBugAvant(6)).isEqualTo("Samedi Dimanche");
    }
    @Test public void fallThroughBug_case7() {
        assertThat(Jep361_SwitchExpressions.fallThroughBugAvant(7)).isEqualTo("Dimanche");
    }
    @Test public void noFallThrough_samedi() { assertThat(Jep361_SwitchExpressions.noFallThroughApres(6)).isEqualTo("Samedi"); }
    @Test public void noFallThrough_dimanche() { assertThat(Jep361_SwitchExpressions.noFallThroughApres(7)).isEqualTo("Dimanche"); }
    @Test public void noFallThrough_semaine() { assertThat(Jep361_SwitchExpressions.noFallThroughApres(3)).isEqualTo("Semaine"); }
}
