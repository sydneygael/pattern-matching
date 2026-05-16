package jep;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Jep394Test {

    @Test public void avant_string() {
        assertThat(Jep394_PatternMatchingInstanceof.describeAvant("hello")).isEqualTo("String de longueur 5 : HELLO");
    }
    @Test public void avant_integer() {
        assertThat(Jep394_PatternMatchingInstanceof.describeAvant(42)).isEqualTo("Integer, valeur double = 84");
    }
    @Test public void avant_double() {
        assertThat(Jep394_PatternMatchingInstanceof.describeAvant(3.14)).isEqualTo("Double arrondi = 3");
    }
    @Test public void avant_inconnu() {
        assertThat(Jep394_PatternMatchingInstanceof.describeAvant(true)).startsWith("Type inconnu");
    }
    @Test public void apres_string() {
        assertThat(Jep394_PatternMatchingInstanceof.describeApres("hello")).isEqualTo("String de longueur 5 : HELLO");
    }
    @Test public void apres_integer() {
        assertThat(Jep394_PatternMatchingInstanceof.describeApres(42)).isEqualTo("Integer, valeur double = 84");
    }
    @Test public void apres_double() {
        assertThat(Jep394_PatternMatchingInstanceof.describeApres(3.14)).isEqualTo("Double arrondi = 3");
    }
    @Test public void avant_egale_apres() {
        Object[] values = {"hello", 42, 3.14, true, null};
        for (Object v : values) {
            assertThat(Jep394_PatternMatchingInstanceof.describeApres(v))
                .as("Divergence pour : " + v)
                .isEqualTo(Jep394_PatternMatchingInstanceof.describeAvant(v));
        }
    }
    @Test public void apres_guarded_condition() {
        assertThat(Jep394_PatternMatchingInstanceof.isLongString("bonjour")).isTrue();
        assertThat(Jep394_PatternMatchingInstanceof.isLongString("hi")).isFalse();
        assertThat(Jep394_PatternMatchingInstanceof.isLongString(42)).isFalse();
        assertThat(Jep394_PatternMatchingInstanceof.isLongString(null)).isFalse();
    }
}
