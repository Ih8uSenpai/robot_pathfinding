package main.search;

import java.util.ArrayList;
import java.util.List;

// класс, описывающий вершину графа
public class Node implements Comparable<Node> {
    private static int idCounter = 0;
    public Rectangle rect;
    public Node parent = null;
    public List<Edge> neighbors;
    public double f = Double.MAX_VALUE;
    public double h = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public boolean opened = false;
    public boolean closed = false;

    Node(double h, Rectangle rect){
        this.h = h;
        this.rect = rect;
        this.neighbors = new ArrayList<>();
    }

    // для сравнения вершин
    @Override
    public int compareTo(Node n) {
        return Double.compare(this.f, n.f);
    }

    // класс, описывающий ребро графа
    public static class Edge {
        Edge(double weight, Node node){
            this.weight = weight;
            this.node = node;
        }

        public double weight;
        public Node node;
    }

    // функция добавления ветви
    public void addBranch(double weight, Node node){
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

}