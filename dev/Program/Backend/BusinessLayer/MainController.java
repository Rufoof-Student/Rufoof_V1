package dev.Program.Backend.BusinessLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.m;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.BusinessLayer.Shelf.ShelfController;
import dev.Program.Backend.BusinessLayer.Window.WindowController;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Tab;
import dev.Program.DTOs.Window;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

public class MainController {
    private ProcessController processController;
    private ShelfController shelfcController;
    private WindowController windowController;

    public MainController() {
        shelfcController = new ShelfController();
        windowController = new WindowController();
        processController = new ProcessController();
        ProcessController.initProcesses();
    }

    public List<Group> getFreeOpenedTabsAsGroups() throws UserException {
        return windowController.getFreeTabs();
    }

    public Shelf createNewShelf(String name, Colors color, List<Window> windowsToInclude, List<Group> tabsToInclude) throws DeveloperException {
        Shelf toCreate = shelfcController.getNewShelf(name, color);
        toCreate = windowController.addGroupsToShelf(toCreate, tabsToInclude);
        // =====================
        // TODO set the windows to the new shelf.
        shelfcController.saveShelf(toCreate);
        return toCreate;
    }

    public List<Shelf> getAllShelfs() {
        return shelfcController.getAllShelfsAsList();
    }

  

    public Shelf closeShelf(Shelf s, List<Group> groups) throws UserException {
        return windowController.closeShelf(s, groups);
    }

    private static String[][] getTabsIds(List<Group> groups) {
        String[][] IdsForGroupTabs = new String[groups.size()][];
        for (int i = 0; i < groups.size(); i++) {
            Group currGroup = groups.get(i);
            IdsForGroupTabs[i] = new String[currGroup.getTabs().size()];
            System.out.println("group " + i + ":");
            int tabNumber = 0;
            for (Tab tab : currGroup.getTabs()) {
                System.out.println("   " + tabNumber + " tab info :" + tab.getTitle() + "-" + tab.getUrl() + "-"
                        + tab.getNativeTabId());
                IdsForGroupTabs[i][tabNumber] = tab.getNativeTabId();
                tabNumber++;
            }
        }
        return IdsForGroupTabs;
    }

    private static void removeFromGroup(List<Group> groups, String[][] IdsForGroupTabs, Scanner sc) {
        boolean done = false;
        while (!done) {
            System.out.println("enter group num");
            String input = sc.nextLine();
            if (input.equals("done")) {
                done = true;
            } else {
                int groupNum;
                int tabNum;
                try {
                    groupNum = Integer.parseInt(input);
                    System.out.println("enter tab num");
                    tabNum = Integer.parseInt(sc.nextLine());
                    groups.get(groupNum).removeTab(IdsForGroupTabs[groupNum][tabNum]);
                } catch (NumberFormatException ex) {
                    System.out.println("enter a number");
                }
            }
        }
    }

    public void openShelf(Shelf s) throws UserException {
        Shelf toUpdate = windowController.openShelf(s);
    }

}
