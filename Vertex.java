import java.util.ArrayList;
import java.util.List;

public class Vertex {
        private String name;
        private int x, y;
        private boolean beingDragged;
        private int offsetX, offsetY;
        private List<Edge> edges; 
        public Vertex(String name) {
            this.name = name;
            this.x = (int) (Math.random() * 600);
            this.y = (int) (Math.random() * 400);
             this.edges = new ArrayList<>();
        }
        public void addEdge(Edge edge) {
            edges.add(edge);
        }
        
        public void setName(String name) {
            this.name = name;
        }
        public int getDegree() {
            return edges.size(); 
        }
        public void setX(int x) {
            this.x = x;
        }
        public void removeEdge(Edge edge) {
            edges.remove(edge);
        }
        public boolean isBeingDragged() {
            return beingDragged;
        }

        public void setBeingDragged(boolean beingDragged) {
            this.beingDragged = beingDragged;
        }

        public void setOffset(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public void updatePosition(int x, int y) {
            this.x = x - offsetX;
            this.y = y - offsetY;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void move(int dx, int dy) {
            x += dx;
            y += dy;
        }

        public boolean contains(int x, int y) {
            return x >= this.x && x <= this.x + 20 && y >= this.y && y <= this.y + 20;
        }
        public List<Edge> getEdges() {
            return edges;
        }
        public void setEdges(List<Edge> edges) {
            this.edges = edges;
        }
    }