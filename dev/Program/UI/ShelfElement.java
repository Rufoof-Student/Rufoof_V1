package dev.Program.UI;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import javax.swing.border.AbstractBorder;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import dev.Program.Backend.BusinessLayer.MainController;
import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.FreeWindowsToSend;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
// import java.awt.Dimension;

import javax.swing.*;

public class ShelfElement extends JXPanel {

    private MainController mainC ;

    private  final Color BG_COLOR = new Color(41, 51, 97);
    private  final Color BTN_COLOR = new Color(75, 100, 209);
    private final Color elementColor = new Color(25, 36, 125);
    private JXLabel txt;


    public ShelfElement(Shelf shelf,MainController mc,Frame frame) {
        super();
        mainC=mc;
        // Setting layout to BoxLayout for vertical alignment
        setLayout(new GridBagLayout());

        setBackground(elementColor);
        setBorder(new RoundedBorder(6));

        // Creating a JXLabel to add to the panel
        txt = new JXLabel(shelf.getName());
        txt.setAlignmentX(Component.CENTER_ALIGNMENT); // Centering the text horizontally
        txt.setForeground(new Color(255, 255,255));
        // Adding components to the panel
        add(txt);
        add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components

        // Setting the size of the panel
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setMinimumSize(new Dimension(100, 40));
        setPreferredSize(new Dimension(100, 50));

        JXPanel buttonPanel = new JXPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Align buttons to the right
        buttonPanel.setOpaque(false); // Make button panel transparent

        JButton openButton = new JButton("Open");
        JButton closeButton = new JButton("Close");
        JButton updateButton = new JButton("update");

        openButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size as needed
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size as needed
        updateButton.setFont(new Font("Arial", Font.PLAIN, 14)); 
        openButton.setVisible(false);
        closeButton.setVisible(false);
        updateButton.setVisible(false);

        // Add components to the button panel
        buttonPanel.add(openButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(updateButton);
        styleButton(updateButton);
        styleButton(openButton);
        styleButton(closeButton);

        // GridBagConstraints for positioning the label
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow the label to take available space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        gbc.insets = new Insets(0, 10, 0, 0); // Add 10px padding on the left
        add(txt, gbc);

        // GridBagConstraints for positioning the button panel
        gbc = new GridBagConstraints(); // Create a new instance for button panel
        gbc.gridx = 1; // Next column for button panel
        gbc.gridy = 0; // Same row as the label
        gbc.weightx = 0; // No extra space for button panel
        gbc.fill = GridBagConstraints.NONE; // Do not fill
        gbc.anchor = GridBagConstraints.EAST; // Align buttons to the right
        add(buttonPanel, gbc);

        addMouseListener(new java.awt.event.MouseAdapter() {

            
            // private final Color whiteColor = new Color(255, 255,255);
            // @Override
            // public void mouseEntered(java.awt.event.MouseEvent evt) {
            //     openButton.setForeground(whiteColor);
            //     closeButton.setForeground(whiteColor);
            //     revalidate();
            //     repaint();
            // }

            // @Override
            // public void mouseExited(java.awt.event.MouseEvent evt) {
            //     openButton.setForeground(elementColor);
            //     closeButton.setForeground(elementColor);
            //     revalidate();
            //     repaint();
            // }
        });

        // Add action listeners for buttons
        openButton.addActionListener(e -> {
            try {
                System.out.println("ShelfElementUI : open the shelf ");
                mainC.openShelf(shelf);
            } catch (UserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (DeveloperException e1) {

                e1.printStackTrace();
            }
        });

        closeButton.addActionListener(e -> {
            try {
                System.out.println("ShelfElementUI : closing the shelf ");
                FreeWindowsToSend free = mainC.getFreeWindows();
                CloseshelfHandlerDialog closeHandler=new CloseshelfHandlerDialog(frame, free);
                if(!closeHandler.isSubmited()) return;
                try {
                    mainC.closeShelf(shelf, free);
                } catch (DeveloperException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.out.println(free.microsoftApps.size());
            } catch (UserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        updateButton.addActionListener(e -> {
            try {
                // System.out.println("ShelfElementUI : closing the shelf ");
                // FreeWindowsToSend free = mainC.getFreeWindows();
                // CloseshelfHandlerDialog closeHandler=new CloseshelfHandlerDialog(frame, free);
                // if(!closeHandler.isSubmited()) return;
                // try {
                //     mainC.closeShelf(shelf, free);
                // } catch (DeveloperException e1) {
                //     // TODO Auto-generated catch block
                //     e1.printStackTrace();
                // }
                // System.out.println(free.microsoftApps.size());
                FreeWindowsToSend shelfWindows  =mainC.getWindowInShelf(shelf.getId());
                UpdateShelfHandler handler =  UpdateShelfHandler.initUpdateHandler(frame,shelfWindows,shelf.getName(),shelf.getColorAsEnum());
                if(!handler.isSubmited()) return ;
                try{
                    mainC.updateShelf(shelf.getId(),shelfWindows,handler.getShelfName());
                    txt.setText(shelf.getName());
                }catch(UserException ex){
                    UI.warning(ex.getMessage());
                }catch(DeveloperException ex){
                    ex.printStackTrace();
                }

            } catch (UserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

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

    private  void styleButton(JButton button) {
        button.setFocusPainted(true);
        button.setBackground(elementColor);
        button.setForeground(Color.WHITE);
        button.setBorder(null);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        button.setPreferredSize(new Dimension(80, 30)); // Set button size for modern look
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 0));
        button.setVisible(true);
                
    }


    
  

}
