package C;

import C.graph.Graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class RoutesMap {
    private final Graph graph = new Graph();

    public int getNumOfCities() {
        return graph.getNumOfVertices();
    }

    public boolean isEmpty() {
        return graph.isEmpty();
    }

    public boolean isFullyCovered() {
        return graph.isFull();
    }

    public String getCity(int i) {
        return graph.getVertex(i);
    }

    public void addCity(String name) {
        graph.addVertex(name);
    }

    public boolean hasBus(int from, int to) {
        return graph.getWeight(from, to) != Graph.NO_EDGE;
    }

    public void addBus(int from, int to, int price) {
        graph.addEdge(from, to, price);
    }

    public int getPrice(int from, int to) {
        return graph.getWeight(from, to);
    }

    public void setPrice(int from, int to, int price) {
        graph.setWeight(from, to, price);
    }

    public List<Integer> getAdjacentCities(int city) {
        return graph.getAdjacent(city);
    }

    public void removeCity(int i) {
        graph.removeVertex(i);
    }

    public void removeBus(int from, int to) {
        graph.removeEdge(from, to);
    }

    public AbstractMap.SimpleEntry<List<String>, Integer> findRoute(int from, int to) {
        List<String> path = new ArrayList<>();
        int totalPrice = 0;

        if (hasBus(from, to)) {
            path.add(getCity(from));
            path.add(getCity(to));
            totalPrice = getPrice(from, to);
            return new AbstractMap.SimpleEntry<>(path, totalPrice);
        }

        return graph.findRoute(from, to);
    }

    @Override
    public String toString() {
        return graph.toString();
    }
}
