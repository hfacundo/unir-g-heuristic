package com.ia.unir;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * El objetivo es mover los inventarios M1, M2 y M3 mediante búsqueda heurística desde su posición inicial
 * hasta una posición final, comenzando desde R.
 *
 * Referencia gráfica:
 *
 *      Matriz inicial
 *        {"M1", "#", "-", "M3"},
 *        {"-", "#", "-", "-"},
 *        {"M2", "-", "R", "-"},
 *        {"-", "-", "-", "-"}
 *
 *      Matriz final
 *        {"-", "#", "-", "-"},
 *        {"-", "#", "-", "-"},
 *        {"-", "-", "-", "-"},
 *        {"-", "M3", "M2", "M1"}
 *
 * Coordenadas
 *      Inventario | Ubicación inicial | Ubicación final
 *           M1    |     (0, 0)        | (3, 3)
 *           M2    |     (2, 0)        | (3, 2)
 *           M3    |     (0, 3)        | (3, 1)
 *
 * El robot inicia en la posicion (2, 2)
 */
public class Main {

    /**
     * Método principal
     * @param args
     */
    public static void main(String[] args) {
        // Matriz inicial 4x4
        String[][] matrix = new String[][] {
                {"M1", "#", "-", "M3"},
                {"-", "#", "-", "-"},
                {"M2", "-", "R", "-"},
                {"-", "-", "-", "-"}
        };

        Main main = new Main();

        // inicia búsqueda
        main.startRobot(matrix);
    }

    public void startRobot(String[][] matrix) {
        Queue<Inventory> inventoryQueue = new PriorityQueue<>(Comparator.comparingInt(Inventory::getDistance));
        // posición inicial del robot
        int[] robotXY = new int[] {2, 2};

        initializeInventory(inventoryQueue, robotXY);
        int sum = 0;

        while (!inventoryQueue.isEmpty()) {
            Inventory currentInventory = inventoryQueue.poll();
            int x = currentInventory.getTargetXY()[0];
            int y = currentInventory.getTargetXY()[1];

            Node robotToInventory = findInventory(matrix, currentInventory.getRobotXY(), currentInventory.getStartXY());
            print(robotToInventory, true, currentInventory.getName(), currentInventory.getRobotXY());

            Node inventoryToTarget = findInventory(matrix, currentInventory.getStartXY(), currentInventory.getTargetXY());
            print(inventoryToTarget, false, currentInventory.getName(), currentInventory.getStartXY());

            int totalMoves = (robotToInventory.getCost() + inventoryToTarget.getCost()) / 2;
            sum += totalMoves;
            System.out.println(String.format("Movimientos totales: %d\n", totalMoves));

            // Actualizo matriz añadiendo # en la posición final del inventario que se acaba de mover
            matrix[x][y] = "#";

            // Ahora necesito recalcular la distancia de Manhattan de los inventarios restantes ya que el robot ahora se
            // encuentra en una nueva posición y esta última también contará como una nueda pared
            inventoryQueue  = recalculateManhattanDistance(inventoryQueue, currentInventory.getTargetXY());
        }

        System.out.println(String.format("El robot realizó un total de %d movimientos", sum));

    }

    private static Queue<Inventory> recalculateManhattanDistance(Queue<Inventory> inventoryQueue, int[] newRobotXY) {

        if (inventoryQueue.isEmpty())
            return inventoryQueue;

        Queue<Inventory> updatedQueue = new PriorityQueue<>(Comparator.comparingInt(Inventory::getDistance));

        System.out.println("Recalculando Heurística... \n");
        while (!inventoryQueue.isEmpty()) {
            Inventory currentInventory = inventoryQueue.poll();
            int[] startXY = currentInventory.getStartXY();
            int[] targetXY = currentInventory.getTargetXY();
            int newDistance = manhattanDistance(newRobotXY, startXY) + manhattanDistance(startXY, targetXY);
            Inventory newInventory = new Inventory(currentInventory.getName(), newRobotXY, startXY, targetXY, newDistance);
            updatedQueue.add(newInventory);
        }

        return updatedQueue;
    }

    /**
     *
     * @param inventoryQueue - Queue donde se almacenan M1, M2 y M3
     * @param robotXY - Posición inicial del robot
     */
    private void initializeInventory(Queue<Inventory> inventoryQueue, int[] robotXY) {
        int[] m1StartXY = new int[] {0, 0};
        int[] m1TargetXY = new int[] {3, 3};
        int distanceM1 = manhattanDistance(robotXY, m1StartXY) + manhattanDistance(m1StartXY, m1TargetXY);
        Inventory m1 = new Inventory("M1", robotXY, new int[] {0, 0}, new int[] {3, 3}, distanceM1);
        inventoryQueue.add(m1);

        int[] m2StartXY = new int[] {2, 0};
        int[] m2TargetXY = new int[] {3, 2};
        int distanceM2 = manhattanDistance(robotXY, m2StartXY) + manhattanDistance(m2StartXY, m2TargetXY);
        Inventory m2 = new Inventory("M2", robotXY, new int[] {2, 0}, new int[] {3, 2}, distanceM2);
        inventoryQueue.add(m2);

        int[] m3StartXY = new int[] {0, 3};
        int[] m3TargetXY = new int[] {3, 1};
        int distanceM3 = manhattanDistance(robotXY, m3StartXY) + manhattanDistance(m3StartXY, m3TargetXY);
        Inventory m3 = new Inventory("M3", robotXY, new int[] {0, 3}, new int[] {3, 1}, distanceM3);
        inventoryQueue.add(m3);
    }

    /**
     *
     * @param matrix - Matriz inicial
     * @param startXY - Coordenadas de la posición inicial
     * @param targetXY - Coordenadas de la posición objetivo
     * @return Inventory o null
     */
    private Node findInventory(String[][] matrix, int[] startXY, int[] targetXY) {
        // Queue para ir almacenando los caminos encontrados, siempre priorizando el de menor distancia
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();

        // El costo inicial es 0 ya que aún no ha ocurrido nada
        int cost = 0;
        int heuristic = manhattanDistance(startXY, targetXY);

        // Parametros: int x, int y, int heuristic, int cost, int totalCost, Node parent
        Node node = new Node(startXY, cost, heuristic, cost + heuristic, null);

        queue.add(node);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            int[] currentXY = currentNode.getXy();

            if (Arrays.equals(currentXY, targetXY)) {
                // encontramos el objetivo
                return currentNode;
            }

            // ya que este no es el objetivo lo marcamos como visitado
            visited.add(currentNode);

            // Ahora debemos buscar en todas direcciones (arriba, abajo, izquierda y derecha) para calcular la distancia de Manhattan
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // arriba, abajo, izquierda, derecha

            for (int[] direction : directions) {
                int[] newXY = new int[] {currentXY[0] + direction[0], currentXY[1] + direction[1]};

                if (isValid(newXY, matrix) && !isPresent(visited, newXY)) {
                    int newCost = currentNode.getCost() + 1;
                    int newHeuristic = manhattanDistance(newXY, targetXY);
                    int newTotalCost = newCost + newHeuristic;
                    Node newNode = new Node(newXY, newHeuristic, newCost, newTotalCost, currentNode);
                    queue.add(newNode);
                }
            }

        }

        // No se encontró camino
        return null;
    }

    private boolean isValid(int[] xy, String[][] matrix) {
        int x = xy[0];
        int y = xy[1];
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length && !matrix[x][y].equals("#");
    }

    private static boolean isPresent(Set<Node> visited, int[] xy) {
        for (Node node : visited) {
            if (Arrays.equals(node.getXy(), xy))
                return true;
        }
        return false;
    }

    /**
     * Calcula la distancia de Manhattan
     *
     * @param x1y1
     * @param x2y2
     * @return
     */
    public static int manhattanDistance(int[] x1y1, int[] x2y2) {
        int x1 = x1y1[0];
        int y1 = x1y1[1];
        int x2 = x2y2[0];
        int y2 = x2y2[1];
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static void print(Node result, boolean lift, String name, int[] originalRobotXY) {
        if (result == null) {
            System.out.println("Camino no encontrado");
        } else {
            // para imprimir los resultados en el orden inverso
            Stack<String> stack = new Stack<>();
            stack.add(lift ? String.format("Objetivo encontrado, levantando inventario %s", name)
                    : String.format("Poniendo inventario %s en su posición final", name));

            while (result != null) {
                if (Arrays.equals(originalRobotXY, result.getXy()))
                    stack.add(String.format("Posición inicial del robot (%d, %d)", result.getXy()[0], result.getXy()[1]));
                else
                    stack.add(String.format("Moviendose a (%d, %d)", result.getXy()[0], result.getXy()[1]));
                result = result.getParent();
            }

            while (!stack.isEmpty()) {
                String record = stack.pop();
                System.out.println(record);
            }
        }

    }

}