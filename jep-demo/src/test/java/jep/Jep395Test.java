package jep;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class Jep395Test {

    @Test public void avant_equals() {
        var p1 = new Jep395_Records.PointAvant(3, 4);
        var p2 = new Jep395_Records.PointAvant(3, 4);
        assertThat(p1).isEqualTo(p2);
    }
    @Test public void avant_hashCode() {
        assertThat(new Jep395_Records.PointAvant(3,4).hashCode()).isEqualTo(new Jep395_Records.PointAvant(3,4).hashCode());
    }
    @Test public void avant_toString() {
        assertThat(new Jep395_Records.PointAvant(3,4)).hasToString("PointAvant[x=3, y=4]");
    }
    @Test public void apres_equals() {
        assertThat(new Jep395_Records.Point(3,4)).isEqualTo(new Jep395_Records.Point(3,4));
    }
    @Test public void apres_hashCode() {
        assertThat(new Jep395_Records.Point(3,4).hashCode()).isEqualTo(new Jep395_Records.Point(3,4).hashCode());
    }
    @Test public void apres_toString() {
        assertThat(new Jep395_Records.Point(3,4)).hasToString("Point[x=3, y=4]");
    }
    @Test public void apres_accessors() {
        var p = new Jep395_Records.Point(3,4);
        assertThat(p.x()).isEqualTo(3);
        assertThat(p.y()).isEqualTo(4);
    }
    @Test
    public void apres_validation_x_negatif() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Jep395_Records.Point(-1, 4));
    }
    @Test
    public void apres_validation_y_negatif() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Jep395_Records.Point(3, -1));
    }
    @Test public void apres_distance() {
        assertThat(new Jep395_Records.Point(3,4).distanceOrigine()).isEqualTo(5.0);
    }
    @Test public void apres_ligne() {
        var ligne = new Jep395_Records.Ligne(new Jep395_Records.Point(0,0), new Jep395_Records.Point(3,4));
        assertThat(ligne.longueur()).isEqualTo(5.0);
    }
    @Test public void apres_immutable() {
        var p = new Jep395_Records.Point(3,4);
        assertThat(p.x()).isEqualTo(3);
        assertThat(p.y()).isEqualTo(4);
    }
}
