package demo;

public sealed interface Forme permits Cercle, Rectangle {}

record Cercle(double r) implements Forme {}

record Rectangle(double l, double h) implements Forme {}

