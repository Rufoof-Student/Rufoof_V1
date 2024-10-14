package dev.Program.UI;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;

import com.google.errorprone.annotations.ForOverride;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowUI extends JXFrame {

    private JPanel titleBar;
    private int mouseX, mouseY; // Variables to hold mouse position
    private Color backColor = new Color(47, 56, 143);
    private JXPanel backgroundPanel;

    public WindowUI(String title) {
        super(title);
        frameInit();
    }

    public void initialization() {
        setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        // setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        // Create a custom panel with a background image
        backgroundPanel = new JXPanel() {
            private Image backgroundImage;

            {
                try {
                    // Use absolute path for testing
                    File imageFile = new File("public/background.png");

                    // For relative path (once confirmed it works)
                    // File imageFile = new File("public/12313.jpg");

                    System.out.println(imageFile.getAbsolutePath() + " " + imageFile.exists());

                    backgroundImage = ImageIO.read(imageFile);

                    if (backgroundImage == null) {
                        System.err.println("Image file not found or could not be read.");
                    } else {
                        System.out.println("Image loaded successfully.");
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Handle error
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the background image to fill the entire panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        System.out.println("'hi");
        // Set layout for backgroundPanel
        backgroundPanel.setLayout(new BorderLayout());
        // Create custom title bar
        createTitleBar(this.getTitle());
        add(titleBar, BorderLayout.NORTH);
        // Add components over the background (if needed)
        add(backgroundPanel);
        // setVisible(true);
    }

    private void createTitleBar(String title) {
        titleBar = new JPanel();
        titleBar.setBackground(backColor); // Set your desired color here
        titleBar.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE); // Set title text color
        titleBar.add(titleLabel, BorderLayout.CENTER);

        // Create minimize button
        JButton minimizeButton = new JButton(" - ");
        minimizeButton.setForeground(Color.WHITE);
         minimizeButton.setBackground(backColor);
        minimizeButton.setBorder(BorderFactory.createEmptyBorder());
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED)); // Minimize action

        // Create close button
        JButton closeButton = new JButton(" X ");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(backColor);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.addActionListener(e -> System.exit(0)); // Close action

        // Add buttons to the title bar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.add(minimizeButton);
        buttonPanel.add(closeButton);
        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Mouse listener for dragging the window
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Record the mouse position when the mouse is pressed
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Move the window when dragging
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
    }

    
    public void addComp(Component comp, Object constraints) {
        backgroundPanel.add(comp, constraints);
    }

    public static void main(String[] args) {
        WindowUI a = new WindowUI("test");
        a.initialization();
    }
}
