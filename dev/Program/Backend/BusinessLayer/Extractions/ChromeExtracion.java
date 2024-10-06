package dev.Program.Backend.BusinessLayer.Extractions;

import java.util.ArrayList;
import java.util.List;

import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Server.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.ChromeWindow;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.GroupPack;
import dev.Program.DTOs.Tab;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;

import java.util.Scanner;

import org.openqa.selenium.support.Color;

public class ChromeExtracion {

    private ExtensionSocketServer server;
    private List<ChromeWindow> runningWindows;
    private int idCounter;

    public ChromeExtracion(ExtensionSocketServer server) {
        this.server = server;
        // runningWindows = server.getCurrentFreeTabs();
        // for (ChromeWindow chromeWindow : runningWindows) {
        // idCounter =
        // idCounter<chromeWindow.getChromeId()?chromeWindow.getChromeId():idCounter;
        // }
    }

    /**
     * Creates groups and moves specified tabs to these groups. Sets the tabs to the
     * shelf and returns the shelf with the updated group data.
     * 
     * @param shelf contains data for the new groups, such as name, color, etc.
     * @param tabs  the tabs to move to the groups.
     * @return the shelf with updated group information.
     */
    public Shelf createNewGroups(Shelf shelf, List<Group> groups) {
        for (Group group : groups) {
            group.setShelfProperties(shelf);
            if (group.getGeneratedId() == -1) {
                group.setGeneratedId(idCounter);
                idCounter++;
            }
        }
        GroupPack openedGroup = server.createGroups(groups);
        shelf.initGroupPack(openedGroup);

        return shelf;
    }

    /**
     * Opens the groups in the specified shelf.
     * 
     * @param shelf the shelf whose groups will be opened.
     * @return the shelf with updated data, such as group IDs.
     */
    public Shelf runShelf(Shelf shelf) {
        if(!insureConnection()){
            ProcessController.runChrome();
        }
        GroupPack groupsToOpen = shelf.getGroups();
        groupsToOpen.setGroups(server.runAllGroups(groupsToOpen.getList(), shelf));
        // TODO - update shelf data
        // shelf.setGroups(groupsToOpen);
        server.filterEmptyTabs();
        return shelf;
    }

    /**
     * Adds tabs to their respective groups and closes all tabs in the new groups.
     * If a tab is not open, it will remain in the shelf.
     * 
     * @param shelf          the shelf containing the groups and tabs.
     * @param groupsToUpdate the tabs to be added and/or closed.
     * @return the shelf with updated group and tab data.
     */
    public Shelf closeShelf(Shelf shelf, List<Group> groupsToUpdate) {
        updateShelfTabsURLs(shelf);
        dealWithGroups(shelf, groupsToUpdate);
        if (insureConnection()) {
            server.closeAllGroups(shelf.getGroups().getList());
        }
        // shelf.markAsClosed();
        server.filterEmptyTabs();
        return shelf;

    }

    private void updateShelfTabsURLs(Shelf shelf){
        for (Group group : shelf.getGroups().getList()) {
            for (Tab tab : group.getTabs()) {
                String id = tab.getNativeTabId();
                if(id!=null){ 
                    String newDataAsJson = server.getUpdatedDataForTabAsJSON(id);
                    tab.updateData(newDataAsJson);
                }
            }
        }
    }


    private void dealWithGroups(Shelf shelf, List<Group> groupsToUpdate) {
        List<Group> groupsToCreate = new ArrayList<>();
        for (Group groupToAdd : groupsToUpdate) {
            Group currGroup;
            if ((currGroup = shelf.hasGroup(groupToAdd)) != null) {
                currGroup.addTabs(groupToAdd.getTabs());
            } else {
                groupToAdd.setShelfProperties(shelf);
                groupsToCreate.add(groupToAdd);
            }
        }

        insertNewGroups(groupsToCreate, shelf);
    }

    private void insertNewGroups(List<Group> newGroups, Shelf shelf) {
        GroupPack diffGroups = server.createGroups(newGroups);
        shelf.addForiegnGroups(diffGroups);
    }

    /**
     * Returns all tabs that are not assigned to any group.
     * 
     * @return a list of ungrouped (free) tabs.
     */
    public List<Group> getFreeTabs() {
        if (!insureConnection()) {
            System.out.println("empty list must be returned");
            return new ArrayList<>();
        }

        List<Group> toRet =server.getCurrentFreeTabs();
        server.filterEmptyTabs();
        return toRet;

    }

    private boolean insureConnection() {
        boolean chromeIsOpened = ProcessController.isChromeOpened();
        boolean extensionConnected = server.connectionIsOpenedWithGoogle();
        if (chromeIsOpened && !extensionConnected) {
            ProcessController.runChrome();

            return true;
        } else if (!chromeIsOpened) {
            System.out.println("chrome is not open");
            return false;
        } else if (chromeIsOpened && extensionConnected) {
            return true;
        }
        return false;

    }

    public static void main(String[] args) {

        ExtensionSocketServer s = new ExtensionSocketServer(8887);
        s.start();
        Scanner sc = new Scanner(System.in);
        List<Shelf> shelfs = new ArrayList<>();
        int idCounter = 0;
        Colors[] colors = new Colors[] { Colors.BLUE, Colors.CYAN, Colors.ORANGE, Colors.RED };
        Shelf shelf = null;
        ChromeExtracion c = new ChromeExtracion(s);
        while (true) {
            System.out.println(
                    "Enter a number to execute a function: \n1. Get Free Tabs\n2. Create New Groups\n3. Close Shelf\n4. open shelf\n5.create new Shelf\n6.move to shelf \n7.Exit");
            String input = sc.nextLine();

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        // Get Free Tabs
                        System.out.println("Getting free tabs...");
                        List<Group> freeTabs = c.getFreeTabs();
                        System.out.println("Free tabs retrieved: " + freeTabs.size());
                        break;

                    case 2:
                        // Create New Groups
                        System.out.println("Creating new groups...");
                        List<Group> free = c.getFreeTabs(); // Assuming free tabs are used here
                        c.createNewGroups(shelf, free);
                        System.out.println("New groups created.");
                        break;

                    case 3:
                        // Close Shelf
                        System.out.println("Closing shelf...");
                        c.closeShelf(shelf, new ArrayList<>());
                        System.out.println("Shelf closed.");
                        for (Group group : shelf.getGroups().getList()) {
                            System.out.println("group collected:");
                            for (Tab tab : group.getTabs()) {
                                System.out.println(tab);
                            }
                        }
                        break;

                    case 4:
                        c.runShelf(shelf);
                        break;
                    case 5:
                        System.out.println("set the name and the color of the shelf:");
                        String name = sc.nextLine();
                        String colorNumber = sc.nextLine();
                        int colorIndex = Integer.parseInt(colorNumber);
                        Shelf sh = new Shelf(name, idCounter, colors[colorIndex]);
                        idCounter++;
                        shelfs.add(sh);
                        break;
                    case 6:
                        System.out.println("enter shelf number:");
                        int index = Integer.parseInt(sc.nextLine());
                        shelf = shelfs.get(index);
                        break;
                    case 7:
                        // Exit loop
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a valid number.");
                        break;
                }

                // Add sleep after each operation to simulate delay
                Thread.sleep(1000);

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
