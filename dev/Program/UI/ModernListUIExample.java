package dev.Program.UI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.ComponentAdapter;

import com.formdev.flatlaf.FlatDarkLaf;

import dev.Program.Backend.BusinessLayer.MainController;
import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.FreeWindowsToSend;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ModernListUIExample {

    public static void main(String[] args){
        ModernListUIExample a = new ModernListUIExample();
        a.start();
    }

    private  final List<String> elements = new ArrayList<>(); // List of elements
    private  Color color = new Color(26, 35, 125);
    private MainController mainC = new MainController();
    public void start() {
        // Set the FlatLaf Dark theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the frame
        JFrame frame = new JFrame("Modern List UI Example") {
            @Override
            public void setSize(Dimension d) {
                super.setSize(d);
                // Maintain aspect ratio when size is set
                int width = d.width;
                int height = (int) (width * 4.0 / 4.0); // 4:3 aspect ratio
                super.setSize(width, height);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        // Add a component listener to maintain the aspect ratio
       

        frame.setBackground(color);

        ImageIcon icon = new ImageIcon("C:\\Users\\bhaah\\OneDrive\\תמונות\\12313.jpg");
        // Create a panel for the list
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(color);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Shelf> shelfs = mainC.getAllShelfs();

        // Create buttons for each element
        for (int i=0;i<10;i++) {
            listPanel.add(createElementPanel(null,frame));
            listPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        }

        // Add a scroll pane for the list panel
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null); // Remove border for modern look
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a circular "+" button at the bottom
        JButton addButton = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                // Make the button circular
                g.setColor(getBackground());
                g.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setOpaque(false);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(177, 180, 189));
        addButton.setBorder(BorderFactory.createEmptyBorder());
        addButton.setPreferredSize(new Dimension(50, 50)); // Set the size for circular button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FreeWindowsToSend free;
                try {
                    free = mainC.getFreeWindows();
                    
                    createAndShowFreeWindowsUI(free,frame);
                    Shelf s = mainC.createNewShelf("test", Colors.BLUE, free);
                    System.out.println(free.microsoftApps.size());
                    // String newElement = "Element " + (elements.size() + 1);
                    // elements.add(newElement);
                    listPanel.add(createElementPanel(s,frame));
                    listPanel.revalidate(); // Refresh the panel
                    listPanel.repaint();
                } catch (UserException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (DeveloperException e1) {
                    e1.printStackTrace();
                }


            }
        });

        // Add the "+" button to the bottom of the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(color);
        buttonPanel.add(addButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }




    private  JPanel createElementPanel(Shelf element,Frame frame) {
        JPanel elementPanel = new JPanel();
        elementPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better alignment
        elementPanel.setBackground(new Color(50, 50, 50));
        elementPanel.setBorder(new RoundedBorder(6)); // Rounded corners with radius 6

        JLabel label = new JLabel("aaaa");
        // label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(100, 40)); // Set a modern size for the element

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Align buttons to the right
        buttonPanel.setOpaque(false); // Make button panel transparent

        JButton openButton = new JButton("Open");
        JButton closeButton = new JButton("Close");

        // Set button properties
        styleButton(openButton);
        styleButton(closeButton);

        // Set font to medium size
        openButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size as needed
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size as needed

        // Initially, make the buttons invisible
        openButton.setVisible(false);
        closeButton.setVisible(false);

        // Add action listeners for buttons
        openButton.addActionListener(e -> {
            try {
                mainC.openShelf(element);
            } catch (UserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (DeveloperException e1) {
                
                e1.printStackTrace();
            }
        });

        closeButton.addActionListener(e -> {
            try {
                FreeWindowsToSend free = mainC.getFreeWindows();
                createAndShowFreeWindowsUI(free,frame);
                System.out.println(free.microsoftApps.size());
            } catch (UserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        // Add components to the button panel
        buttonPanel.add(openButton);
        buttonPanel.add(closeButton);

        // GridBagConstraints for positioning the label
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow the label to take available space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        gbc.insets = new Insets(0, 10, 0, 0); // Add 10px padding on the left
        elementPanel.add(label, gbc);

        // GridBagConstraints for positioning the button panel
        gbc = new GridBagConstraints(); // Create a new instance for button panel
        gbc.gridx = 1; // Next column for button panel
        gbc.gridy = 0; // Same row as the label
        gbc.weightx = 0; // No extra space for button panel
        gbc.fill = GridBagConstraints.NONE; // Do not fill
        gbc.anchor = GridBagConstraints.EAST; // Align buttons to the right
        elementPanel.add(buttonPanel, gbc);

        // Ensure the panel fits the width of the parent
        elementPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Fit the parent width

        // Add a MouseListener to show/hide buttons when hovering
        elementPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openButton.setVisible(true);
                closeButton.setVisible(true);
                elementPanel.revalidate();
                elementPanel.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openButton.setVisible(false);
                closeButton.setVisible(false);
                elementPanel.revalidate();
                elementPanel.repaint();
            }
        });
        // elementPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 0));
        return elementPanel;
    }


 private  final Color BG_COLOR = new Color(41, 51, 97);
    private  final Color BTN_COLOR = new Color(75, 100, 209);
    
    public  FreeWindowsToSend createAndShowFreeWindowsUI(FreeWindowsToSend freeWindowsToSend,Frame owner) {
        // Create the dialog
        JDialog dialog = new JDialog( owner, "Select Free Windows", true); // Modal dialog
        dialog.setSize(300, 300);
        dialog.setResizable(false);
        // dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JXPanel panel = new JXPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);

        List<JCheckBox> checkboxes = new ArrayList<>();
        
        // Add Edge groups checkboxes
        for (Group group : freeWindowsToSend.edgeGroups) {
            JCheckBox checkBox = new JCheckBox("Edge: " + group.getName());
            checkBox.setForeground(Color.WHITE);
            checkBox.setBackground(BG_COLOR);
            checkboxes.add(checkBox);
            panel.add(checkBox);
        }
        
        // Add Chrome groups checkboxes
        for (Group group : freeWindowsToSend.chromeGroups) {
            JCheckBox checkBox = new JCheckBox("Chrome: " + group.getName());
            checkBox.setForeground(Color.WHITE);
            checkBox.setBackground(BG_COLOR);
            checkboxes.add(checkBox);
            panel.add(checkBox);
        }
        
        // Add Microsoft apps checkboxes
        for (MicroApp app : freeWindowsToSend.microsoftApps) {
            JCheckBox checkBox = new JCheckBox("Microsoft: " + app.getName());
            checkBox.setForeground(Color.WHITE);
            checkBox.setBackground(BG_COLOR);
            checkboxes.add(checkBox);
            panel.add(checkBox);
        }
        
        // Add the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(BTN_COLOR);
        submitButton.addActionListener((ActionEvent e) -> {
            // Update FreeWindowsToSend based on selections
            List<Group> newEdgeGroups = new ArrayList<>();
            List<Group> newChromeGroups = new ArrayList<>();
            List<MicroApp> newMicrosoftApps = new ArrayList<>();
            
            int index = 0;
            // Iterate through the checkboxes and remove unselected items
            for (JCheckBox checkBox : checkboxes) {
                if (checkBox.isSelected()) {
                    if (index < freeWindowsToSend.edgeGroups.size()) {
                        newEdgeGroups.add(freeWindowsToSend.edgeGroups.get(index));
                    } else if (index < freeWindowsToSend.edgeGroups.size() + freeWindowsToSend.chromeGroups.size()) {
                        newChromeGroups.add(freeWindowsToSend.chromeGroups.get(index - freeWindowsToSend.edgeGroups.size()));
                    } else {
                        newMicrosoftApps.add(freeWindowsToSend.microsoftApps.get(index - freeWindowsToSend.edgeGroups.size() - freeWindowsToSend.chromeGroups.size()));
                    }
                }
                index++;
            }
            
            freeWindowsToSend.edgeGroups = newEdgeGroups;
            freeWindowsToSend.chromeGroups = newChromeGroups;
            freeWindowsToSend.microsoftApps = newMicrosoftApps;

            // Close the dialog
            dialog.dispose();
        });
        
        panel.add(Box.createVerticalStrut(10)); // Add spacing
        panel.add(submitButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true); // Show the dialog and block the calling thread

        // Return the updated FreeWindowsToSend after the dialog is closed
        return freeWindowsToSend;
    }

    // Method to style buttons
    private static void styleButton(JButton button) {
        button.setFocusPainted(true);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(null);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        button.setPreferredSize(new Dimension(80, 30)); // Set button size for modern look
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 0));
    }

    // Custom border with rounded corners
    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Rounded border
        }
    }
}
