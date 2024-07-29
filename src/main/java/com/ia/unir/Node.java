package com.ia.unir;

import java.util.Arrays;
import java.util.Objects;

/**
 * Esta clase representa el estado de las coordenadas x,y.
 *
 * Se incluyen valores para conocer el costo de los movimientos, la heurística y el nodo padre con el cual
 * podemos reconstruir el camino seguido.
 */
public class Node implements Comparable<Node>{
    // Coordenadas x, y
    private int[] xy;
    // Distancia de Manhattan
    private int heuristic;
    // Costo de movimientos
    private int cost;
    // Valor para ordenar por prioridad
    private int totalCost; // heuristic + cost
    // Mantiene un registro de los nodos recorridos para llegar al objetivo
    private Node parent;

    public Node(int[] xy, int heuristic, int cost, int totalCost, Node parent) {
        this.xy = xy;
        this.heuristic = heuristic;
        this.cost = cost;
        this.totalCost = totalCost;
        this.parent = parent;
    }

    public int[] getXy() {
        return xy;
    }

    public void setXy(int[] xy) {
        this.xy = xy;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.totalCost, o.totalCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return heuristic == node.heuristic && cost == node.cost && totalCost == node.totalCost && Objects.deepEquals(xy, node.xy) && Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(xy), heuristic, cost, totalCost, parent);
    }
}
