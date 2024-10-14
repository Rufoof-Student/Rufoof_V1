package dev.Program.UI;

import org.jdesktop.swingx.JXPanel;
// import org.openqa.selenium.support.Color;
import javax.swing.*;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ShelfsListUI extends JPanel {

    private final Color elementColor = new Color(255, 255, 255, 100);


    public ShelfsListUI(List<Shelf> shelfs) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false); 
    }

    public void x() {
        // setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    }

    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
        
    //     // Custom painting for transparency
    //     Graphics2D g2d = (Graphics2D) g.create();
    //     g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f)); // 50% transparency
    //     g2d.setColor(elementColor);  // Set your transparent color
    //     g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill panel with transparent color
    //     g2d.dispose(); // Dispose of Graphics2D to avoid memory leaks
    // }
}
