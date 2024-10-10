package dev.Program.UI;

import java.util.List;
import javax.swing.*;

import dev.Program.Backend.BusinessLayer.MainController;
import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.*;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BasicUI extends JFrame {

    private MainController m;
    private JList<String> shelfList;
    private DefaultListModel<String> shelfListModel;

    public BasicUI() {
        m = new MainController();
        setTitle("Shelf Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        // Layout
        setLayout(new BorderLayout());

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton createShelfButton = new JButton("Create Shelf");
        JButton getShelvesButton = new JButton("Get All Shelves");
        JButton openShelfButton = new JButton("Open Shelf");
        JButton closeShelfButton = new JButton("Close Shelf");

        buttonPanel.add(createShelfButton);
        // buttonPanel.add(getShelvesButton);
        buttonPanel.add(openShelfButton);
        buttonPanel.add(closeShelfButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Shelf list to display shelves
        shelfListModel = new DefaultListModel<>();
        shelfList = new JList<>(shelfListModel);
        // Set larger font for the JList
        Font biggerFont = new Font("Arial", Font.PLAIN, 18); // Set the font size to 18
        shelfList.setFont(biggerFont);

        // Optionally, set a larger row height for the items in the list
        shelfList.setFixedCellHeight(30); // Set the row height to 30 pixels

        add(new JScrollPane(shelfList), BorderLayout.CENTER);

        // Button Actions
        createShelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createShelf();
                } catch (UserException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                getShelves();
            }
        });

        openShelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openShelf();
            }
        });

        closeShelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    closeShelf();
                } catch (UserException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    private void createShelf() throws UserException {
        FreeWindowsToSend f = m.getFreeWindows();
        // List<Group> groups =f.chromeGroups;
        // List<Group> groups2 = f.edgeGroups;
        // List<MicroApp> microApps = f.microsoftApps;
        // if (groups == null) {
        //     JOptionPane.showMessageDialog(this, "Refresh the extension!!");
        //     return;
        // }
        // System.out.println(microApps.size());

        processTabSelection(f);

        // Get shelf name
        String name = JOptionPane.showInputDialog(this, "Enter shelf name");

        // Choose a color
        Colors[] colors = Colors.values();
        String[] colorNames = { "BLUE", "CYAN", "ORANGE", "RED" };
        String chosenColor = (String) JOptionPane.showInputDialog(this, "Choose a color",
                "Color", JOptionPane.QUESTION_MESSAGE, null, colorNames, colorNames[0]);

        // Find selected color
        Colors selectedColor = Colors.valueOf(chosenColor.toUpperCase());

        // Create the shelf
        try {
            m.createNewShelf(name, selectedColor, f);
        } catch (DeveloperException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Shelf created successfully!");
    }

   private void processTabSelection(FreeWindowsToSend f) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // Create a bigger font
    Font biggerFont = new Font("Arial", Font.PLAIN, 18);

    // Map for storing the selected checkboxes for Chrome and Edge
    Map<Group, List<JCheckBox>> chromeGroupTabCheckBoxMap = new HashMap<>();
    Map<Group, List<JCheckBox>> edgeGroupTabCheckBoxMap = new HashMap<>();
    int chromeCounter = 0;
    int edgeCounter = 0;

    // Create checkboxes for each Chrome group and tab
    for (Group group : f.chromeGroups) {
        JLabel groupLabel = new JLabel("Chrome Window - " + chromeCounter);
        groupLabel.setFont(biggerFont); 
        panel.add(groupLabel);

        List<JCheckBox> tabCheckBoxes = new ArrayList<>();
        for (Tab tab : group.getTabs()) {
            JCheckBox tabCheckBox = new JCheckBox(tab.getTitle(), true); 
            tabCheckBox.setFont(biggerFont);
            tabCheckBoxes.add(tabCheckBox);
            panel.add(tabCheckBox);
        }

        chromeGroupTabCheckBoxMap.put(group, tabCheckBoxes);
        chromeCounter++;
    }

    // Create checkboxes for each Edge group and tab
    for (Group group : f.edgeGroups) {
        JLabel groupLabel = new JLabel("Edge Window - " + edgeCounter);
        groupLabel.setFont(biggerFont);
        panel.add(groupLabel);

        List<JCheckBox> tabCheckBoxes = new ArrayList<>();
        for (Tab tab : group.getTabs()) {
            JCheckBox tabCheckBox = new JCheckBox(tab.getTitle(), true); 
            tabCheckBox.setFont(biggerFont);
            tabCheckBoxes.add(tabCheckBox);
            panel.add(tabCheckBox);
        }

        edgeGroupTabCheckBoxMap.put(group, tabCheckBoxes);
        edgeCounter++;
    }

    // MicroApps section
    List<JCheckBox> checkBoxesMicroApps = new ArrayList<>();
    for (MicroApp microApp : f.microsoftApps) {
        JCheckBox cbox = new JCheckBox(microApp.getBlankAppName() + ": " + microApp.getFileName(), true);
        cbox.setFont(biggerFont);
        checkBoxesMicroApps.add(cbox);
        panel.add(cbox);
    }

    // Display the dialog with checkboxes
    int result = JOptionPane.showConfirmDialog(this, new JScrollPane(panel),
            "Select Tabs to Keep", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Process the selected Chrome tabs
        List<Group> chromeGroupsToRemove = new ArrayList<>();
        for (Group group : f.chromeGroups) {
            List<Tab> tabsToKeep = new ArrayList<>();
            List<JCheckBox> checkBoxes = chromeGroupTabCheckBoxMap.get(group);

            for (int i = 0; i < checkBoxes.size(); i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                if (checkBox.isSelected()) {
                    tabsToKeep.add(group.getTabs().get(i));
                }
            }

            group.setTabs(tabsToKeep);

            // If the group has no tabs left, remove it
            if (tabsToKeep.isEmpty()) {
                chromeGroupsToRemove.add(group);
            }
        }

        f.chromeGroups.removeAll(chromeGroupsToRemove);

        // Process the selected Edge tabs
        List<Group> edgeGroupsToRemove = new ArrayList<>();
        for (Group group : f.edgeGroups) {
            List<Tab> tabsToKeep = new ArrayList<>();
            List<JCheckBox> checkBoxes = edgeGroupTabCheckBoxMap.get(group);

            for (int i = 0; i < checkBoxes.size(); i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                if (checkBox.isSelected()) {
                    tabsToKeep.add(group.getTabs().get(i));
                }
            }

            group.setTabs(tabsToKeep);

            // If the group has no tabs left, remove it
            if (tabsToKeep.isEmpty()) {
                edgeGroupsToRemove.add(group);
            }
        }

        f.edgeGroups.removeAll(edgeGroupsToRemove);

        // Process MicroApps
        List<MicroApp> appsToRemove = new ArrayList<>();
        for (int i = 0; i < checkBoxesMicroApps.size(); i++) {
            JCheckBox jcb = checkBoxesMicroApps.get(i);
            MicroApp microAppToKeep = f.microsoftApps.get(i);
            if (!jcb.isSelected()) {
                appsToRemove.add(microAppToKeep);
            }
        }

        f.microsoftApps.removeAll(appsToRemove);
    }
}


    private void getShelves() {
        shelfListModel.clear();
        List<Shelf> shelves = m.getAllShelfs();
        for (int i = 0; i < shelves.size(); i++) {
            shelfListModel.addElement(i + ": " + shelves.get(i).toString());
        }
    }

    private void openShelf() {
        int selectedShelfIndex = shelfList.getSelectedIndex();
        if (selectedShelfIndex != -1) {
            try {
                m.openShelf(m.getAllShelfs().get(selectedShelfIndex));
            } catch (UserException | DeveloperException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Shelf opened successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a shelf to open.");
        }
    }

    private void closeShelf() throws UserException {
        int selectedShelfIndex = shelfList.getSelectedIndex();
        if (selectedShelfIndex != -1) {
            Shelf shelf = m.getAllShelfs().get(selectedShelfIndex);
            // // Get the open tabs and allow the user to select/deselect them
            // List<Group> groups = m.getFreeOpenedTabsAsGroups("msedge.exe");
            // List<MicroApp> microApp = m.getFreeOpenedMicroApps();
            // System.out.println("----------------------------------");
            // if (groups == null) {
            //     JOptionPane.showMessageDialog(this, "Refresh the extension!!");
            //     return;
            // }
            // System.out.println(microApp.size());
            FreeWindowsToSend f = m.getFreeWindows();
            processTabSelection(f);
            // System.out.println("the apps to add is null ? "+microApp==null);
            // Close the shelf with the updated groups
            try {
                m.closeShelf(shelf, f);
            } catch (UserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (DeveloperException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Shelf closed successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a shelf to close.");
        }
    }

    private void processTabSelection(List<Group> groups, List<MicroApp> microApps) {
        // int tabsNumber = 0;
        // for (Group group : groups) {
        // tabsNumber += group.getTabs().size();
        // }
        // if (tabsNumber == 0)
        // return;
        // Panel to hold the checkboxes for all groups
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a bigger font
        Font biggerFont = new Font("Arial", Font.PLAIN, 18);

        // Map for storing the selected checkboxes
        Map<Group, List<JCheckBox>> groupTabCheckBoxMap = new HashMap<>();
        int counter = 0;

        // Create checkboxes for each group and tab
        for (Group group : groups) {
            // Label for group
            JLabel groupLabel = new JLabel("Chrome Window - " + counter);
            groupLabel.setFont(biggerFont); // Set bigger font for the label
            panel.add(groupLabel);

            // List of checkboxes for each tab in the group
            List<JCheckBox> tabCheckBoxes = new ArrayList<>();
            for (Tab tab : group.getTabs()) {
                JCheckBox tabCheckBox = new JCheckBox(tab.getTitle(), true); // Default to checked
                tabCheckBox.setFont(biggerFont); // Set bigger font for the checkboxes
                tabCheckBoxes.add(tabCheckBox);
                panel.add(tabCheckBox);
            }

            // Store the checkboxes for this group
            groupTabCheckBoxMap.put(group, tabCheckBoxes);
            counter++;
        }

        List<JCheckBox> checkBoxesMicroApps = new ArrayList<>();

        for (MicroApp microApp : microApps) {
            JCheckBox cbox = new JCheckBox(microApp.getBlankAppName() + ":" + microApp.getFileName(), true);
            cbox.setFont(biggerFont);
            checkBoxesMicroApps.add(cbox);
            panel.add(cbox);

        }

        // Display the dialog with checkboxes
        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(panel),
                "Select Tabs to Keep", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Process the selected tabs after user clicks OK
            List<Group> blankGroupsToRemove =new ArrayList<>();
            for (Group group : groups) {
                List<Tab> tabsToKeep = new ArrayList<>();
                List<JCheckBox> checkBoxes = groupTabCheckBoxMap.get(group);

                // Loop through the checkboxes and gather selected tabs
                for (int i = 0; i < checkBoxes.size(); i++) {
                    JCheckBox checkBox = checkBoxes.get(i);
                    if (checkBox.isSelected()) {
                        tabsToKeep.add(group.getTabs().get(i)); // Keep the selected tab
                    }
                }
                System.out.println("hii");

                // Update the group with only the selected tabs
                group.setTabs(tabsToKeep);

                // If the group has no tabs left, remove it from the groups list
                if (tabsToKeep.isEmpty()) {
                    blankGroupsToRemove.add(group);
                    
                }
            }
            
            for (Group group : blankGroupsToRemove) {
                groups.remove(group);
            }

            List<MicroApp> toRemove = new ArrayList<>();
            for (int i = 0; i < checkBoxesMicroApps.size(); i++) {
                JCheckBox jcb = checkBoxesMicroApps.get(i);
                MicroApp microAppToKeep = microApps.get(i);
                if (!jcb.isSelected()) {
                    toRemove.add(microAppToKeep);
                }
            }
            for (MicroApp microAppToRemove : toRemove) {
                if (microApps.contains(microAppToRemove))
                    microApps.remove(microAppToRemove);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BasicUI mainUI = new BasicUI();
            mainUI.setVisible(true);
        });
    }
}
