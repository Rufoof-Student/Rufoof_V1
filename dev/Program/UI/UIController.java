package dev.Program.UI;
// import org.jdesktop.swingx.JXScrollPane;

import dev.Program.Backend.BusinessLayer.MainController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.FreeWindowsToSend;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.Dimension;

import javax.swing.*;

// import org.openqa.selenium.Dimension;
public class UIController {
    public MainController main;
    private final String Title = "Rufoof";
    private WindowUI window;
    private ShelfsListUI shelfsPanel;

    public UIController() {
        main = new MainController();

    }

    public void run() {
        window = new WindowUI(Title);
        window.initialization();
        shelfsPanel = new ShelfsListUI(main.getAllShelfs());
        // JScrollPane list = new JScrollPane(shelfsPanel);
        // Make the panel transparent
        // shelfsPanel.setBackground(new Color(0, 0, 0, 0)); // Se?mi-transparent red
        // for testing

        for (Shelf shelf : main.getAllShelfs()) {
            shelfsPanel.add(new ShelfElement(shelf, main, window));
            shelfsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        }
        JScrollPane list = new JScrollPane(shelfsPanel);
        list.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        list.setBorder(null);
        list.setOpaque(false);
        list.getViewport().setOpaque(false);

        window.addComp(list, BorderLayout.CENTER);
        window.addComp(getAddButton(), BorderLayout.SOUTH);
        window.setVisible(true);
    }

    private JPanel getAddButton() {
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
                    free = main.getFreeWindows();

                    NewShelfHandlerDialog newShelfHandler = new NewShelfHandlerDialog(window, free);

                    // newShelfHandler.waitUnttillDispose();

                    System.out.println("button submited!!!!");
                    if (!newShelfHandler.isSubmited())
                        return;
                    Shelf s = main.createNewShelf(newShelfHandler.getShelfName(), newShelfHandler.getShelfColor(),
                            free);
                    System.out.println(free.microsoftApps.size());
                    shelfsPanel.add(new ShelfElement(s, main, window));
                    shelfsPanel.add(Box.createRigidArea(new Dimension(5, 10)));
                    shelfsPanel.revalidate(); // Refresh the panel
                    shelfsPanel.repaint();
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
        buttonPanel.setBackground(new Color(30));
        buttonPanel.add(addButton);
        // frame.add(buttonPanel, BorderLayout.SOUTH);
        return buttonPanel;

    }

    public static void main(String[] args) {
        UIController a = new UIController();
        a.run();
    }

}
