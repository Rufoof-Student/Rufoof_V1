package dev.Program.Backend.CLI;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import dev.Program.Backend.ServiceLayer.ShelfService;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Tab;
import dev.Program.Backend.BusinessLayer.MainController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.BusinessLayer.Window.WindowController;
import dev.Program.DTOs.Window;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;
import dev.Program.Backend.BusinessLayer.Shelf.ShelfController;

public class CLI_Excuter {
    private MainController MainController;
    private WindowController windowController;

    public CLI_Excuter() {
        MainController = new MainController();
        windowController = new WindowController();
    }

    private static void print(String toPrint) {
        System.out.println("\u001B[36m" + toPrint + "\u001B[0m");
    }

    public static void main(String[] args) throws UserException {
        Scanner sc = new Scanner(System.in);
        boolean toExit = false;
        MainController main = new MainController();
        ShelfService service = new ShelfService();
        List<Group> avalibaleGroups = main.getFreeOpenedTabsAsGroups();
        String[][] IdsForGroupTabs = getTabsIds(avalibaleGroups);
        while (!toExit) {
            print("Welcome to Rufoof CLI .\n1- Exit \n2 - to get all shelves in data. \n3 to create a new shelf. \n4 openShelf \n5 closeShelf \n7 removeShelf");
            String input = sc.nextLine();

            switch (input) {
                case "1":
                    toExit = true;
                    break;
                case "2":
                    String output = service.getAllShelves();
                    print(output);
                    break;
                case "3":
                    getShelfData(service, sc);
                    break;
                case "4":
                    createNewShelf(service, sc, main);
                case "5":
                    openShelf(main, sc);
                    break;
                case "6":
                    closeShelf(sc, main);

                    break;
                case "7":
                    print("enter the Shelf number you want to remove ");
                    break;
                default:
                    break;

            }

        }

    }

    private static void createNewShelf(ShelfService service, Scanner sc, MainController main) throws UserException {
        print("here are all your open tabs, please choose the tabs you want to include in your new shelf .\n after finishing all the tabs write Done");
        List<Group> avalibaleGroups = main.getFreeOpenedTabsAsGroups();
        String[][] IdsForGroupTabs = getTabsIds(avalibaleGroups);
        removeFromGroup(avalibaleGroups, IdsForGroupTabs, sc);
        System.out.print("\u001B[36m" + IdsForGroupTabs + "\u001B[0m");
        String idToAdd = "";
        int[] tabsToAdd = new int[avalibaleGroups.size()];
        while (!idToAdd.equalsIgnoreCase("Done")) {
            idToAdd = sc.nextLine();
            int IdAsInt = Integer.parseInt(idToAdd);
            tabsToAdd[IdAsInt] = IdAsInt;
        }

        print("please choode a name for the shelf");
        String name = sc.nextLine();
        print("please choose the color you want from\n{BLUE,Colors.CYAN,Colors.ORANGE,Colors.RED}");
        int number = Integer.parseInt(sc.nextLine());
        Colors[] colors = new Colors[] { Colors.BLUE, Colors.CYAN, Colors.ORANGE, Colors.RED };
        try {
            main.createNewShelf(name, colors[number], null, avalibaleGroups);
        } catch (DeveloperException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void openShelf(MainController main, Scanner sc) throws UserException {
        print("Enter the number of shelf you want to open");
        try {
            main.openShelf(main.getAllShelfs().get(Integer.parseInt(sc.nextLine())));
        } catch (NumberFormatException ex) {
            System.out.println("enter a number");
        }
    }

    private static void removeShelf(int id) {
    }

    private static void closeShelf(Scanner sc, MainController main) throws UserException {
        List<Group> avalibaleGroups = main.getFreeOpenedTabsAsGroups();
        String[][] IdsForGroupTabs = getTabsIds(avalibaleGroups);
        print("enter the shelf nunber you want to close");
        try {
            Shelf s = main.getAllShelfs().get(Integer.parseInt(sc.nextLine()));

            System.out.println("are you want to add any of this tabs?");
            avalibaleGroups = main.getFreeOpenedTabsAsGroups();
            IdsForGroupTabs = getTabsIds(avalibaleGroups);
            removeFromGroup(avalibaleGroups, IdsForGroupTabs, sc);

            main.closeShelf(s, avalibaleGroups);

        } catch (NumberFormatException ex) {
            System.out.println("enter a valid num");
        }
    }

    private static void getShelfData(ShelfService service, Scanner sc) {
        String allshelves = service.getAllShelves();
        print(allshelves);
        print("Choose one of the shelves and Enter an ID for it ");
        String IDasString = sc.nextLine();
        int IdAsInt = Integer.parseInt(IDasString);
        String output = service.getShelfData(IdAsInt);
        print(output);
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

    private Shelf closeShelf(Shelf s, List<Group> groups) throws UserException {
        return windowController.closeShelf(s, groups);
    }

}
