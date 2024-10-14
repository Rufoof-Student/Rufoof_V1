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
import dev.Program.DTOs.Window;

public class NewShelfHandlerDialog extends JDialog {
    private final Color BG_COLOR = new Color(25, 36, 125);
    private final Color BTN_COLOR = new Color(75, 100, 209);
    private JXTextField nameInput;
    private String newShelfName;
    private Colors chosenColor;
    private boolean submited;
    private JList<Colors> colorList;

    private FreeWindowsToSend freeWindowsToSend;
    private Map<Group, List<JCheckBox>> chrommeCheckBoxs;
    private Map<Group, List<JCheckBox>> edgeCheckBoxs;
    private Map<MicroApp, JCheckBox> microAppCheckBoxs;

    private void setProbs() {
        setLocationRelativeTo(null);
        setUndecorated(true);
        setSize(300, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public NewShelfHandlerDialog(Frame owner, FreeWindowsToSend freeWindowsToSend) {
        super(owner, "", true);
        // synchronized(InputLock){
        this.freeWindowsToSend = freeWindowsToSend;
        setProbs();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        setInputBtns();
        List<JCheckBox> checkBoxs = new ArrayList<>();
        setCheckBoxs(panel, checkBoxs);
        JScrollPane scrolCheckBoxs = new JScrollPane(panel);
        scrolCheckBoxs.setPreferredSize(new Dimension(150,150));
        add(scrolCheckBoxs, BorderLayout.CENTER);
        JXButton submitButton = getSubmitButton(checkBoxs);
        JXButton closeButton = getCloseButton();
        JXPanel lowerPanel = new JXPanel();
        lowerPanel.setPreferredSize(new Dimension(getWidth() / 3, 25));
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
        lowerPanel.setBackground(BG_COLOR);
        lowerPanel.add(submitButton);
        lowerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        lowerPanel.add(closeButton);
        lowerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // lowerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lowerPanel, BorderLayout.SOUTH);
        setVisible(true);
        // InputLock.notifyAll();
        // }
    }

    private void setInputBtns() {
        JXPanel btnsPanel = new JXPanel();
        btnsPanel.setLayout(new BoxLayout(btnsPanel, BoxLayout.Y_AXIS));
        btnsPanel.setBackground(BG_COLOR);
        JXLabel inputNameTitle = new JXLabel("The name of the new shelf");
        inputNameTitle.setForeground(new Color(255, 255, 255));
        nameInput = new JXTextField();
        nameInput.setPreferredSize(new Dimension(30, 25));
        DefaultListModel<Colors> listModel = new DefaultListModel<>();
        for (Colors color : Colors.getAllColors()) {
            listModel.addElement(color);
        }
        colorList = new JList<>(listModel);
        colorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        colorList.setVisibleRowCount(5);
        JScrollPane scrollPane = new JScrollPane(colorList);
        scrollPane.setPreferredSize(new Dimension(150, 70));
        btnsPanel.add(scrollPane);

        btnsPanel.add(nameInput, BoxLayout.X_AXIS);
        btnsPanel.add(inputNameTitle, BoxLayout.X_AXIS);
        btnsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(btnsPanel, BorderLayout.NORTH);

    }

    private JXButton getSubmitButton(List<JCheckBox> checkBoxs) {
        JXButton submitButton = new JXButton("Submit");
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(BTN_COLOR);
        // submitButton.setBorder(null);
        submitButton.addActionListener((ActionEvent e) -> {
            // synchronized (InputLock) {
            System.out.println("joining the sync on submit btn " + Thread.currentThread().getName());
            // Update FreeWindowsToSend based on selections
            List<Group> newEdgeGroups = getSelectedEdgeTabs();
            List<Group> newChromeGroups = getSelectedChromeTabs();
            List<MicroApp> newMicrosoftApps = getSelectedMicroApps();

            newShelfName = nameInput.getText();
            if (newShelfName == null) {
                UI.warning("You have to write a name for the shelf!");
                return;
            }

            Colors selectedColor = colorList.getSelectedValue();
            if (selectedColor != null) {
                chosenColor = selectedColor;
            } else {
                UI.warning("You have to choose color!");
                return;
            }

            if (newEdgeGroups.size() + newChromeGroups.size() + newMicrosoftApps.size() <= 0) {
                UI.warning("You have to choose apps to keep in shelf!!");
                return;
            }

            freeWindowsToSend.edgeGroups = newEdgeGroups;
            freeWindowsToSend.chromeGroups = newChromeGroups;
            freeWindowsToSend.microsoftApps = newMicrosoftApps;

            submited = true;
            dispose();
        });
        System.out.println(Thread.currentThread().getName());
        return submitButton;
    }

    private List<Group> getSelectedEdgeTabs() {
        return getSelectedGroupsFrom(edgeCheckBoxs);
    }

    private List<Group> getSelectedGroupsFrom(Map<Group, List<JCheckBox>> checkBoxsMap) {
        List<Group> toRet = new ArrayList<>();
        for (Group group : checkBoxsMap.keySet()) {
            List<JCheckBox> checkBoxs = checkBoxsMap.get(group);
            List<Tab> tabsForGroup = group.getTabs();
            List<Tab> tabsToKeep = new ArrayList<>();
            for (int i = 0; i < checkBoxs.size(); i++) {
                if (checkBoxs.get(i).isSelected())
                    tabsToKeep.add(tabsForGroup.get(i));
            }
            if(tabsToKeep.size()>0){
                group.setTabs(tabsToKeep);
                toRet.add(group);
            }
        }
        return toRet;
    }

    private List<Group> getSelectedChromeTabs() {
        return getSelectedGroupsFrom(chrommeCheckBoxs);
    }

    private List<MicroApp> getSelectedMicroApps() {
        List<MicroApp> toRet = new ArrayList<>();
        for(MicroApp microApp:microAppCheckBoxs.keySet()){
            if(microAppCheckBoxs.get(microApp).isSelected())toRet.add(microApp);
        }
        return toRet;
    }

    private JXButton getCloseButton() {
        JXButton CloseButton = new JXButton("Cancel");
        CloseButton.setForeground(Color.WHITE);
        CloseButton.setBackground(new Color(255, 0, 0));
        CloseButton.addActionListener((ActionEvent e) -> {

            dispose();

        });
        return CloseButton;
    }

    private void setCheckBoxs(JPanel panel, List<JCheckBox> checkBoxs) {
        chrommeCheckBoxs = new HashMap<>();
        edgeCheckBoxs = new HashMap<>();
        listTabsWindows(freeWindowsToSend.chromeGroups, panel, chrommeCheckBoxs);
        listTabsWindows(freeWindowsToSend.edgeGroups, panel, edgeCheckBoxs);
        listWindows(freeWindowsToSend.microsoftApps, panel, checkBoxs);
    }

    private void listTabsWindows(List<Group> edgeGroups, JPanel panel, Map<Group, List<JCheckBox>> toAddIn) {
        for (Group groupOfTabs : edgeGroups) {
            List<JCheckBox> checkBoxs = new ArrayList<>();
            JXLabel label = new JXLabel(groupOfTabs.getName());
            label.setForeground(new Color(0, 0, 0));
            panel.add(label);
            for (Tab tab : groupOfTabs.getTabs()) {
                JCheckBox checkBox = new JCheckBox(tab.getTitle());
                checkBox.setForeground(Color.WHITE);
                checkBox.setBackground(BG_COLOR);
                checkBoxs.add(checkBox);
                panel.add(checkBox);
            }
            toAddIn.put(groupOfTabs, checkBoxs);

        }
    }

    public void listWindows(List<MicroApp> windows, JPanel panel, List<JCheckBox> checkBoxs) {
        microAppCheckBoxs = new HashMap<>();
        for (MicroApp window : windows) {
            JCheckBox checkBox = new JCheckBox(window.getName() + ": " + window.getWindowTitle());
            checkBox.setForeground(Color.WHITE);
            checkBox.setBackground(BG_COLOR);
            checkBoxs.add(checkBox);
            panel.add(checkBox);
            microAppCheckBoxs.put(window, checkBox);
        }
    }

    public String getShelfName() {
        return newShelfName;
    }

    public Colors getShelfColor() {
        return chosenColor;
    }

    public boolean isSubmited() {
        return submited;
    }

}
