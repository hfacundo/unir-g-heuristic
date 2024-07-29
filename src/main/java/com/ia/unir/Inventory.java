package com.ia.unir;

import java.util.Arrays;
import java.util.Objects;

/**
 * Representa el inventario que el robot debe mover.
 *
 * Incluye el nombre del inventario, las coordenadas iniciales y finales, la posición inicial del robot y la distancia de Manhattan
 * la cual permitirá ordenar los inventarios de acuerdo a una prioridad.
 *
 * Esta prioridad es el camino mas corto, a menor distancia mayor prioridad para explorar el inventario
 */
public class Inventory implements Comparable<Inventory>{

    private String name;
    private int[] robotXY;
    private int[] startXY;
    private int[] targetXY;
    private int distance;

    public Inventory(String name, int[] robotXY, int[] startXY, int[] targetXY, int distance) {
        this.name = name;
        this.robotXY = robotXY;
        this.startXY = startXY;
        this.targetXY = targetXY;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public int[] getRobotXY() {
        return robotXY;
    }

    public int[] getStartXY() {
        return startXY;
    }

    public int[] getTargetXY() {
        return targetXY;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                ", robotXY=" + Arrays.toString(robotXY) +
                ", startXY=" + Arrays.toString(startXY) +
                ", targetXY=" + Arrays.toString(targetXY) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory that = (Inventory) o;
        return distance == that.distance && Objects.equals(name, that.name) && Objects.deepEquals(robotXY, that.robotXY) && Objects.deepEquals(startXY, that.startXY) && Objects.deepEquals(targetXY, that.targetXY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, distance, Arrays.hashCode(robotXY), Arrays.hashCode(startXY), Arrays.hashCode(targetXY));
    }

    @Override
    public int compareTo(Inventory o) {
        return Integer.compare(this.distance, o.distance);
    }
}
