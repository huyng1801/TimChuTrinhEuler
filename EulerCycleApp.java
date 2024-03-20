import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class EulerCycleApp extends JFrame {
    private GraphPanel graphPanel;
    private Graph graph;
    private JMenuItem addVertexRadioButton;
    private JMenuItem addEdgeRadioButton;
    private JMenuItem selectRadioButton;
    private ButtonGroup modeButtonGroup;
    private Vertex tempEdgeStartVertex; // Đỉnh tạm thời cho đường nối tạm thời
    private int tempEdgeEndX; // Tọa độ X tạm thời của đỉnh đích tạm thời
    private int tempEdgeEndY; // Tọa độ Y tạm thời của đỉnh đích tạm thời

    public EulerCycleApp() {

        setTitle("Euler Cycle Finder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        graphPanel = new GraphPanel();
        graph = new Graph();
        graphPanel.setGraph(graph);

        add(graphPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        // ... (Phần code trước đó)

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu updateMenu = new JMenu("Cập nhật");
        menuBar.add(updateMenu);

        addVertexRadioButton = new JRadioButtonMenuItem("Thêm đỉnh");

        updateMenu.add(addVertexRadioButton);

        addEdgeRadioButton = new JRadioButtonMenuItem("Thêm cạnh");

        selectRadioButton = new JRadioButtonMenuItem("Chọn");

        updateMenu.add(selectRadioButton);
        updateMenu.add(addEdgeRadioButton);
        modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(addEdgeRadioButton);
        modeButtonGroup.add(addVertexRadioButton);
        modeButtonGroup.add(selectRadioButton);
        JMenuItem deleteVertexButton = new JMenuItem("Delete Vertex");
        JMenuItem deleteEdgeButton = new JMenuItem("Delete Edge");
        updateMenu.add(deleteVertexButton);
        updateMenu.add(deleteEdgeButton);

        JMenu calculateMenu = new JMenu("Tính toán");
        menuBar.add(calculateMenu);

        JMenuItem checkConnectivityButton = new JMenuItem("Kiểm tra liên thông");

        calculateMenu.add(checkConnectivityButton);

        JMenuItem findEulerCycleButton = new JMenuItem("Tìm chu trình Euler");

        calculateMenu.add(findEulerCycleButton);

        JMenu saveMenu = new JMenu("Lưu lại");
        menuBar.add(saveMenu);

        JMenuItem saveGraphButton = new JMenuItem("Lưu đồ thị");

        saveMenu.add(saveGraphButton);
        JMenuItem openGraphButton = new JMenuItem("Mở đồ thị");

        saveMenu.add(openGraphButton);
        JMenuItem clearButton = new JMenuItem("Làm mới");

        saveMenu.add(clearButton);

        pack();
        setVisible(true);

        add(controlPanel, BorderLayout.NORTH);

        pack();
        setVisible(true);

        addVertexRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.setAddVertexMode(true);
                graphPanel.setAddEdgeMode(false);
            }
        });

        addEdgeRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.setAddVertexMode(false);
                graphPanel.setAddEdgeMode(true);
            }
        });

        selectRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.setAddVertexMode(false);
                graphPanel.setAddEdgeMode(false);
            }
        });

        deleteVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vertexToDelete = JOptionPane.showInputDialog("Enter vertex to delete:");
                if (vertexToDelete != null && !vertexToDelete.isEmpty()) {
                    graph.deleteVertex(vertexToDelete);
                    graphPanel.repaint();
                } else {
                    showError("Invalid vertex name.");
                }
            }
        });

        deleteEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sourceVertex = JOptionPane.showInputDialog("Enter source vertex:");
                String destinationVertex = JOptionPane.showInputDialog("Enter destination vertex:");
                if (sourceVertex != null && destinationVertex != null &&
                        !sourceVertex.isEmpty() && !destinationVertex.isEmpty()) {
                    graph.deleteEdge(sourceVertex, destinationVertex);
                    graphPanel.repaint();
                } else {
                    showError("Invalid source or destination vertex.");
                }
            }
        });

        checkConnectivityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<List<Vertex>> connectedComponents = graph.findConnectedComponents();
                showMessage("Number of Connected Components: " + connectedComponents.size());
            }
        });

        findEulerCycleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> eulerCycle = graph.findEulerCycle();
                if (eulerCycle == null) {
                    showMessage("The graph is not Eulerian.");
                } else if (eulerCycle.isEmpty()) {
                    showMessage("No Euler Cycle found.");
                } else {
                    showMessage("Euler Cycle: " + eulerCycle);
                }
            }
        });

        saveGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(EulerCycleApp.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        graph.saveToFile(fileName);
                    } catch (IOException ex) {
                        showError("Error saving graph to file.");
                    }
                }
            }
        });

        openGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(EulerCycleApp.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        graph.loadFromFile(fileName);
                        graphPanel.setGraph(graph);
                        graphPanel.repaint();
                    } catch (IOException ex) {
                        showError("Error loading graph from file.");
                    }
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                graph.clearGraph();
                graphPanel.repaint();
                graphPanel.clearSetUsedVertexNames();
            }
        });

        // Thêm sự kiện mouse listener cho graphPanel để xử lý thêm cạnh
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (graphPanel.isAddEdgeMode()) {
                    int x = e.getX();
                    int y = e.getY();
                    Vertex clickedVertex = null;

                    for (Vertex vertex : graph.getVertices()) {
                        if (vertex.contains(x, y)) {
                            clickedVertex = vertex;
                            break;
                        }
                    }

                    if (clickedVertex != null) {
                        // Nếu tọa độ chuột nằm trên một đỉnh, lưu đỉnh này làm đỉnh đầu của đường nối
                        // tạm thời
                        tempEdgeStartVertex = clickedVertex;
                        tempEdgeEndX = x;
                        tempEdgeEndY = y;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (graphPanel.isAddEdgeMode() && tempEdgeStartVertex != null) {
                    int x = e.getX();
                    int y = e.getY();
                    Vertex endVertex = null;

                    for (Vertex vertex : graph.getVertices()) {
                        if (vertex.contains(x, y)) {
                            endVertex = vertex;
                            break;
                        }
                    }

                    if (endVertex != null) {
                        // Nếu tọa độ chuột nằm trên một đỉnh hợp lệ, thêm cạnh và vẽ lại
                        graph.addEdge(tempEdgeStartVertex.getName(), endVertex.getName());
                        graphPanel.repaint();
                    }

                    // Xóa đường nối tạm thời
                    tempEdgeStartVertex = null;
                    tempEdgeEndX = -1;
                    tempEdgeEndY = -1;
                    repaint();
                }
            }
        });
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
