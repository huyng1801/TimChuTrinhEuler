import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Graph {
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public boolean edgeExists(String source, String destination) {
        for (Edge edge : edges) {
            if ((edge.getSource().getName().equals(source) && edge.getDestination().getName().equals(destination))
                    || (edge.getSource().getName().equals(destination)
                            && edge.getDestination().getName().equals(source))) {
                return true;
            }
        }
        return false;
    }

    public void clearGraph() {
        vertices.clear();
        edges.clear();
    }

    public List<List<Vertex>> findConnectedComponents() {
        List<List<Vertex>> connectedComponents = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();

        for (Vertex vertex : vertices) {
            if (!visited.contains(vertex)) {
                List<Vertex> component = new ArrayList<>();
                dfsForConnectedComponent(vertex, visited, component);
                connectedComponents.add(component);
            }
        }

        return connectedComponents;
    }

    private void dfsForConnectedComponent(Vertex vertex, Set<Vertex> visited, List<Vertex> component) {
        visited.add(vertex);
        component.add(vertex);

        for (Edge edge : edges) {
            if (edge.getSource() == vertex && !visited.contains(edge.getDestination())) {
                dfsForConnectedComponent(edge.getDestination(), visited, component);
            }
            if (edge.getDestination() == vertex && !visited.contains(edge.getSource())) {
                dfsForConnectedComponent(edge.getSource(), visited, component);
            }
        }
    }

    public void addEdge(String source, String destination) {
        Vertex sourceVertex = getVertex(source);
        Vertex destVertex = getVertex(destination);

        if (sourceVertex != null && destVertex != null) {
            Edge newEdge = new Edge(sourceVertex, destVertex);
            edges.add(newEdge);
            sourceVertex.addEdge(newEdge);
            destVertex.addEdge(newEdge);
        }
    }
    public void deleteVertex(String vertexName) {
        Vertex vertex = getVertex(vertexName);
        if (vertex != null) {
            vertices.remove(vertex);
    
            // Xóa các cạnh liên quan đến đỉnh mà bạn đang xóa
            List<Edge> edgesToRemove = new ArrayList<>(vertex.getEdges());
            for (int i = 0; i < edgesToRemove.size(); i++) {
                Edge edge = edgesToRemove.get(i);
                edges.remove(edge);
                edge.getSource().removeEdge(edge);
                edge.getDestination().removeEdge(edge);
            }
        } else {
     
        JOptionPane.showMessageDialog(null, "Không tìm thấy đỉnh với tên: " + vertexName, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
     
        }
    }
    
    public void deleteEdge(String source, String destination) {
        boolean edgeRemoved = edges.removeIf(edge -> edge.getSource().getName().equals(source) &&
                edge.getDestination().getName().equals(destination));
    
        if (!edgeRemoved) {
          
            JOptionPane.showMessageDialog(null, "Không tìm thấy cạnh giữa " + source + " và " + destination, "Thông báo", JOptionPane.INFORMATION_MESSAGE);

           
        } else {
         
            Vertex sourceVertex = getVertex(source);
            Vertex destinationVertex = getVertex(destination);
    
            if (sourceVertex != null && destinationVertex != null) {
                Edge edgeToRemove = null;
                for (Edge edge : sourceVertex.getEdges()) {
                    if (edge.getDestination() == destinationVertex) {
                        edgeToRemove = edge;
                        break;
                    }
                }
    
                if (edgeToRemove != null) {
                    sourceVertex.removeEdge(edgeToRemove);
                    destinationVertex.removeEdge(edgeToRemove);
                }
            }
        }
    }
    

    public boolean isConnected() {
        if (vertices.isEmpty()) {
            return true;
        }

        Set<Vertex> visited = new HashSet<>();
        dfs(vertices.get(0), visited);

        return visited.size() == vertices.size();
    }

    private void dfs(Vertex vertex, Set<Vertex> visited) {
        visited.add(vertex);
        for (Edge edge : edges) {
            if (edge.getSource() == vertex && !visited.contains(edge.getDestination())) {
                dfs(edge.getDestination(), visited);
            }
            if (edge.getDestination() == vertex && !visited.contains(edge.getSource())) {
                dfs(edge.getSource(), visited);
            }
        }
    }

    public ArrayList<String> findEulerCycle() {
        ArrayList<String> cycle = new ArrayList<>();

        if (!isEulerianCyclePossible()) {
            return cycle;
        }

        Map<Vertex, List<Vertex>> adjacencyMap = new HashMap<>();
        for (Edge edge : edges) {
            adjacencyMap.computeIfAbsent(edge.getSource(), k -> new ArrayList<>()).add(edge.getDestination());
            adjacencyMap.computeIfAbsent(edge.getDestination(), k -> new ArrayList<>()).add(edge.getSource());
        }

        Stack<Vertex> stack = new Stack<>();
        Vertex currentVertex = vertices.get(0);
        stack.push(currentVertex);

        while (!stack.isEmpty()) {
            if (adjacencyMap.containsKey(currentVertex) && !adjacencyMap.get(currentVertex).isEmpty()) {
                Vertex nextVertex = adjacencyMap.get(currentVertex).remove(0);
                stack.push(nextVertex);
                adjacencyMap.get(nextVertex).remove(currentVertex);
                currentVertex = nextVertex;
            } else {
                cycle.add(currentVertex.getName());
                stack.pop();
                if (!stack.isEmpty()) {
                    currentVertex = stack.peek();
                }
            }
        }

        return cycle;
    }

    private boolean isEulerianCyclePossible() {
        if (vertices.isEmpty()) {
            return false;
        }

        for (Vertex vertex : vertices) {
            if (vertex.getDegree() % 2 != 0) {
                return false;
            }
        }

        return true;
    }

    public void saveToFile(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Vertex vertex : vertices) {
                writer.println("V " + vertex.getName() + " " + vertex.getX() + " " + vertex.getY());
            }
            for (Edge edge : edges) {
                writer.println("E " + edge.getSource().getName() + " " + edge.getDestination().getName());
            }
        }
    }

    public void loadFromFile(String fileName) throws IOException {
        vertices.clear();
        edges.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals("V")) {
                    addVertex(new Vertex(parts[1]));
                    Vertex vertex = getVertex(parts[1]);
                    if (vertex != null) {
                        vertex.setX(Integer.parseInt(parts[2]));
                        vertex.setY(Integer.parseInt(parts[3]));
                    }
                } else if (parts[0].equals("E")) {
                    addEdge(parts[1], parts[2]);
                }
            }
        }
    }

    public Vertex getVertex(String vertexName) {
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals(vertexName)) {
                return vertex;
            }
        }
        return null;
    }
}