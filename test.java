import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 class MDIExample extends JFrame {

    private JDesktopPane desktopPane = new JDesktopPane();

    public MDIExample() {
        setTitle("MDI Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMenuBar();
		setLayout(new BorderLayout());
        // Set up the main desktop pane
        add(desktopPane, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createInternalFrame();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void createInternalFrame() {
        JInternalFrame internalFrame = new JInternalFrame("Internal Frame", true, true, true, true);
        internalFrame.setSize(300, 200);
        internalFrame.setLocation(desktopPane.getAllFrames().length * 20, desktopPane.getAllFrames().length * 20);
        internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("This is an internal frame");
        internalFrame.add(label);

        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MDIExample().setVisible(true);
            }
        });
    }
}
