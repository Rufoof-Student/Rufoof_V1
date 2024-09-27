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

    public List<Group> getFreeOpenedTabsAsGroups() {
        return windowController.getFreeTabs();
    }

    public Shelf createNewShelf(String name, Colors color, List<Window> windowsToInclude, List<Group> tabsToInclude) {
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

    // public static void main(String[] args) {
    //     MainController m = new MainController();
    //     boolean toExit = false;
    //     Scanner sc = new Scanner(System.in);
    //     while (!toExit) {
    //         System.out.println("1-create shelf \n2-get all shelfs\n3-open shelf\n4-close shelf");
    //         try {
    //             String input = sc.nextLine();
    //             switch (input) {
    //                 case "1":
    //                     System.out.println(
    //                             "will show the opened tabs 4 u , please choose the tabs that you want to include in the new shelf");
    //                     List<Group> groups = m.getFreeOpenedTabsAsGroups();
    //                     String[][] IdsForGroupTabs = getTabsIds(groups);
    //                     removeFromGroup(groups, IdsForGroupTabs, sc);
    //                     System.out.println("choose a name for the group");
    //                     String name = sc.nextLine();
    //                     System.out.println("choose a color from Colors.BLUE,Colors.CYAN,Colors.ORANGE,Colors.RED}");
    //                     int number = Integer.parseInt(sc.nextLine());
    //                     Colors[] colors = new Colors[] { Colors.BLUE, Colors.CYAN, Colors.ORANGE, Colors.RED };
    //                     m.createNewShelf(name, colors[number], null, groups);
    //                     break;
    //                 case "2":
    //                     System.out.println("choose shelf to close");
    //                     List<Shelf> shelfs = m.getAllShelfs();
    //                     for (int i = 0; i < shelfs.size(); i++) {
    //                         System.out.println(i + " " + shelfs.get(i).toString());
    //                     }
    //                     break;
    //                 case "3":
    //                     System.out.println("enter the number of the shelf to open");
    //                     try {
    //                         m.openShelf(m.getAllShelfs().get(Integer.parseInt(sc.nextLine())));
    //                     } catch (NumberFormatException ex) {
    //                         System.out.println("enter a number");
    //                     }
    //                     break;
    //                 case "4":
    //                     System.out.println("choose shelf");
    //                     try {
    //                         Shelf s = m.getAllShelfs().get(Integer.parseInt(sc.nextLine()));

    //                         System.out.println("are you want to add any of this tabs?");
    //                         groups = m.getFreeOpenedTabsAsGroups();
    //                         IdsForGroupTabs = getTabsIds(groups);
    //                         removeFromGroup(groups, IdsForGroupTabs, sc);

    //                         m.closeShelf(s, groups);

    //                     } catch (NumberFormatException ex) {
    //                         System.out.println("enter a valid num");
    //                     }

    //                     break;
    //                 default:
    //                     break;
    //             }
    //         } catch (Exception ex) {
    //             ex.printStackTrace();
    //         }
    //     }
    // }

    public Shelf closeShelf(Shelf s, List<Group> groups) {
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

    public void openShelf(Shelf s) {
        Shelf toUpdate = windowController.openShelf(s);
    }

}
