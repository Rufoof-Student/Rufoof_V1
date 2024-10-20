package dev.Program.UI;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.DTOs.Colors;

// import com.sun.jna.platform.unix.X11.Window;

import dev.Program.DTOs.FreeWindowsToSend;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Tab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import dev.Program.DTOs.FreeWindowsToSend;

public class UpdateShelfHandler extends NewShelfHandlerDialog {

    private static String shelfName;
    private static Colors color;

    public UpdateShelfHandler(Frame owner, FreeWindowsToSend freeWindowsToSend) {
        super(owner, freeWindowsToSend);
        
        // TODO Auto-generated constructor stub
        System.out.println("the shelf name is "+shelfName);
    }

    public static UpdateShelfHandler initUpdateHandler(Frame owner, FreeWindowsToSend freeWindowsToSend, String shelfName1, Colors color1){
        shelfName = shelfName1;
        color = color1;
        return new UpdateShelfHandler(owner, freeWindowsToSend);

    }


    @Override
    public void setCheckBoxs(JPanel panel, List<JCheckBox> checkBoxs) {
        super.setCheckBoxs(panel, checkBoxs);
        for (List<JCheckBox> edgeCBs : edgeCheckBoxs.values()) {
            for (JCheckBox cb : edgeCBs) {
                cb.setSelected(true);
            }
        }
        for (List<JCheckBox> edgeCBs : chrommeCheckBoxs.values()) {
            for (JCheckBox cb : edgeCBs) {
                cb.setSelected(true);
            }
        }
        for (JCheckBox cb : microAppCheckBoxs.values()) {
            cb.setSelected(true);
        }

    }

    @Override
    protected JXPanel setInputBtns() {
        // JXPanel panel = super.setInputBtns();
        // nameInput.setText(shelfName);
        // panel.revalidate(); // Refresh the panel
        // panel.repaint();
        JXPanel btnsPanel = new JXPanel();
        btnsPanel.setLayout(new BoxLayout(btnsPanel, BoxLayout.Y_AXIS));
        btnsPanel.setBackground(BG_COLOR);
        JXLabel inputNameTitle = new JXLabel("To update shelf's name");
        inputNameTitle.setForeground(new Color(255, 255, 255));
        nameInput = new JXTextField();
        nameInput.setPreferredSize(new Dimension(30, 25));
        nameInput.setText(shelfName);
        

        btnsPanel.add(nameInput, BoxLayout.X_AXIS);
        btnsPanel.add(inputNameTitle, BoxLayout.X_AXIS);
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(btnsPanel, BorderLayout.NORTH);
        btnsPanel.revalidate(); // Refresh the panel
        btnsPanel.repaint();
        return btnsPanel;

    }


    @Override
    protected void setListColor(JXPanel btnsPanel){
        
    }

}
