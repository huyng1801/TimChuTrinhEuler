import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

public class GraphPanel extends JPanel {

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    private Graph graph;
    private Vertex draggedVertex = null;
    private boolean addVertexMode = false;
    private boolean addEdgeMode = false;
private Set<Character> usedVertexNames = new HashSet<>();
    public boolean isAddEdgeMode() {
        return addEdgeMode;
    }
public void clearSetUsedVertexNames(){
    usedVertexNames.clear();
}
    public GraphPanel() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (addVertexMode) {
                    // Chế độ thêm đỉnh: Thêm đỉnh mới với tên ngẫu nhiên
                    String vertexName = generateRandomVertexName();
                    Vertex newVertex = new Vertex(vertexName);
                    newVertex.setX(x);
                    newVertex.setY(y);
                    graph.addVertex(newVertex);
                    repaint();
                } else if (addEdgeMode) {
                    // Chế độ thêm cạnh: Xác định đỉnh được click và thực hiện vẽ cạnh
                    Vertex selectedVertex = null;
                    for (Vertex vertex : graph.getVertices()) {
                        if (vertex.contains(x, y)) {
                            if (selectedVertex == null) {
                                selectedVertex = vertex;
                            } else {
                                // Đã có đỉnh được chọn trước đó, vẽ cạnh giữa hai đỉnh này
                                graph.addEdge(selectedVertex.getName(), vertex.getName());
                                repaint();
                                break;
                            }
                        }
                    }
                } else {
                    // Chế độ chọn và di chuyển đỉnh
                    for (Vertex vertex : graph.getVertices()) {
                        if (vertex.contains(x, y)) {
                            draggedVertex = vertex;
                            vertex.setBeingDragged(true);
                            vertex.setOffset(x - vertex.getX(), y - vertex.getY());
                            repaint();
                            break;
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (draggedVertex != null) {
                    draggedVertex.setBeingDragged(false);
                    draggedVertex = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (draggedVertex != null) {
                    draggedVertex.updatePosition(e.getX(), e.getY());
                    repaint();
                }
            }
        });
    }

    public void setAddVertexMode(boolean enabled) {
        addVertexMode = enabled;
    }

    public void setAddEdgeMode(boolean enabled) {
        addEdgeMode = enabled;
    }

    private String generateRandomVertexName() {
        // Tạo tên đỉnh ngẫu nhiên bằng cách xác định chữ cái tiếp theo chưa được sử dụng
        char name = 'A';
        while (usedVertexNames.contains(name)) {
            name++;
        }
        usedVertexNames.add(name);
        return name + "";
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph != null) {
            for (Edge edge : graph.getEdges()) {
                int x1 = edge.getSource().getX() + 10;
                int y1 = edge.getSource().getY() + 10;
                int x2 = edge.getDestination().getX() + 10;
                int y2 = edge.getDestination().getY() + 10;
                g.drawLine(x1, y1, x2, y2); // Vẽ cạnh
            }
            for (Vertex vertex : graph.getVertices()) {
                int x = vertex.getX();
                int y = vertex.getY();
                g.setColor(Color.BLUE);
                g.fillOval(x, y, 20, 20); // Vẽ đỉnh

                g.setColor(Color.BLACK);
                g.drawString(vertex.getName(), x + 5, y - 5); // Vẽ tên đỉnh
            }
        }
        if (draggedVertex != null) {
            g.setColor(Color.RED);
            g.fillOval(draggedVertex.getX(), draggedVertex.getY(), 20, 20);
            g.setColor(Color.BLACK);
            g.drawString(draggedVertex.getName(), draggedVertex.getX() + 5, draggedVertex.getY() - 5);
        }
    }
}