package jep;

import org.junit.jupiter.api.Test;

import static jep.Jep409_441_SealedAndSwitch.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Jep409_441Test {

    @Test public void cercleIsShape() {
        Shape s = new Cercle(5.0);
        assertThat(s).isInstanceOf(Shape.class);
        assertThat(s).isInstanceOf(Cercle.class);
    }
    @Test public void rectangleIsShape() {
        assertThat(new Rectangle(3.0, 4.0)).isInstanceOf(Shape.class);
    }
    @Test public void triangleIsShape() {
        assertThat(new TriangleRect(6.0, 8.0)).isInstanceOf(Shape.class);
    }

    @Test public void avant_describe_cercle() {
        assertThat(describeAvant(new CercleAvant(5.0))).isEqualTo("Cercle de rayon 5.0");
    }
    @Test public void avant_describe_rectangle() {
        assertThat(describeAvant(new RectangleAvant(3.0, 4.0))).isEqualTo("Rectangle 3.0x4.0");
    }
    @Test public void avant_describe_null() {
        assertThat(describeAvant(null)).isEqualTo("Nul");
    }
    @Test public void apres_describe_cercle() {
        assertThat(describeApres(new Cercle(5.0))).isEqualTo("Cercle de rayon 5.0");
    }
    @Test public void apres_describe_rectangle() {
        assertThat(describeApres(new Rectangle(3.0, 4.0))).isEqualTo("Rectangle 3.0x4.0");
    }
    @Test public void apres_describe_null() {
        assertThat(describeApres(null)).isEqualTo("Nul");
    }

    @Test public void guarded_grand_cercle() { assertThat(classifyShape(new Cercle(15.0))).isEqualTo("Grand cercle"); }
    @Test public void guarded_petit_cercle() { assertThat(classifyShape(new Cercle(5.0))).isEqualTo("Petit cercle"); }
    @Test public void guarded_carre() { assertThat(classifyShape(new Rectangle(4.0, 4.0))).isEqualTo("Carré de côté 4.0"); }
    @Test public void guarded_rectangle() { assertThat(classifyShape(new Rectangle(3.0, 5.0))).isEqualTo("Rectangle 3.0x5.0"); }

    @Test public void recordPattern_cercle() {
        assertThat(computeArea(new Cercle(1.0))).isEqualTo(Math.PI);
    }
    @Test public void recordPattern_rectangle() {
        assertThat(computeArea(new Rectangle(3.0, 4.0))).isEqualTo(12.0);
    }
    @Test public void recordPattern_triangle() {
        assertThat(computeArea(new TriangleRect(6.0, 4.0))).isEqualTo(12.0);
    }
    @Test public void nullInSwitch() {
        assertThat(describeApres(null)).isEqualTo("Nul");
    }
    @Test public void exhaustivite() {
        Shape[] shapes = { new Cercle(1), new Rectangle(2,3), new TriangleRect(4,5) };
        for (Shape s : shapes) computeArea(s);
    }
}
