package C.graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    public static final int NO_EDGE = Integer.MIN_VALUE;
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<List<Edge>> edges = new ArrayList<>();

    public int getNumOfVertices() {
        return vertices.size();
    }

    public int getNumOfEdges() {
        int n = 0;
        for (List<Edge> adjacent : edges) {
            n += adjacent.size();
        }
        return n / 2;
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public boolean isFull() {
        return getNumOfEdges() == (getNumOfVertices() * (getNumOfVertices() - 1) / 2);
    }

    public String getVertex(int i) {
        return vertices.get(i).getName();
    }

    public int getWeight(int from, int to) {
        List<Edge> adjacent = edges.get(from);
        for (Edge edge : adjacent) {
            if (edge.getTo().getNumber() == to) {
                return edge.getWeight();
            }
        }
        return NO_EDGE;
    }

    public void setWeight(int from, int to, int weight) {
        List<Edge> adjacent = edges.get(from);
        for (Edge edge : adjacent) {
            if (edge.getTo().getNumber() == to) {
                edge.setWeight(weight);
                break;
            }
        }

        adjacent = edges.get(to);
        for (Edge edge : adjacent) {
            if (edge.getTo().getNumber() == from) {
                edge.setWeight(weight);
                return;
            }
        }
    }

    public void addVertex(String name) {
        vertices.add(new Vertex(vertices.size(), name));
        edges.add(new ArrayList<>());
    }

    public void addEdge(int from, int to, int weight) {
        if (from == to) return;
        edges.get(from).add(new Edge(vertices.get(from), vertices.get(to), weight));
        edges.get(to).add(new Edge(vertices.get(to), vertices.get(from), weight));
    }

    public List<Integer> getAdjacent(int vertex) {
        List<Integer> result = new ArrayList<>();
        for (Edge edge : edges.get(vertex)) {
            result.add(edge.getTo().getNumber());
        }
        return result;
    }

    public void removeVertex(int i) {
        edges.remove(i);
        for (List<Edge> adjacent : edges) {
            for (int j = 0; j < adjacent.size(); j++) {
                if (adjacent.get(j).getTo().getNumber() == i) {
                    adjacent.remove(j);
                    j--;
                }
            }
        }

        vertices.remove(i);
        for (int j = i; j < vertices.size(); j++) {
           vertices.get(j).setNumber(j);
        }
    }

    public void removeEdge(int from, int to) {
        List<Edge> adjacent = edges.get(from);
        for (int i = 0; i < adjacent.size(); i++) {
            if (adjacent.get(i).getTo().getNumber() == to) {
                adjacent.remove(i);
                break;
            }
        }

        adjacent = edges.get(to);
        for (int i = 0; i < adjacent.size(); i++) {
            if (adjacent.get(i).getTo().getNumber() == from) {
                adjacent.remove(i);
                return;
            }
        }
    }

    private boolean dfsRecursive(int current, int aim, boolean[] visited, List<Edge> path) {
        visited[current] = true;
        List<Edge> adjacent = edges.get(current);

        for (Edge edge : adjacent) {
            int to = edge.getTo().getNumber();
            if (to == aim) {
                path.add(edge);
                return true;
            }
            if (!visited[to]) {
                if (dfsRecursive(to, aim, visited, path)) {
                    path.add(edge);
                    return true;
                }
            }
        }

        return false;
    }

    public AbstractMap.SimpleEntry<List<String>, Integer> findRoute(int from, int to) {
        boolean[] visited = new boolean[vertices.size()];
        List<Edge> path = new ArrayList<>();

        dfsRecursive(from, to, visited, path);

        List<String> strPath = new ArrayList<>(path.size());
        int sumWeight = 0;

        for (int i = path.size() - 1; i >= 0; i--) {
            strPath.add(path.get(i).getFrom().getName());
            sumWeight += path.get(i).getWeight();
        }
        if (!path.isEmpty()) {
            strPath.add(vertices.get(to).getName());
        }

        return new AbstractMap.SimpleEntry<>(strPath, sumWeight);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < vertices.size(); i++) {
            builder
                    .append(vertices.get(i).getName())
                    .append(": ");

            List<Edge> adjacent = edges.get(i);
            for (int j = 0; j < adjacent.size() - 1; j++) {
                builder
                        .append(adjacent.get(j).getTo().getName())
                        .append("(")
                        .append(adjacent.get(j).getWeight())
                        .append("), ");
            }
            if (!adjacent.isEmpty()) {
                builder
                        .append(adjacent.get(adjacent.size() - 1).getTo().getName())
                        .append("(")
                        .append(adjacent.get(adjacent.size() - 1).getWeight())
                        .append(")");
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
