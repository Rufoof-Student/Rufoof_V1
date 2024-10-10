package dev.Program.Backend.BusinessLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.m;

import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroAppController;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.BusinessLayer.Shelf.ShelfController;
import dev.Program.Backend.BusinessLayer.Window.WindowController;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.Relax;
import dev.Program.DTOs.Tab;
import dev.Program.DTOs.Window;
import dev.Program.DTOs.Exceptions.DeveloperException;
import dev.Program.DTOs.Exceptions.UserException;

public class MainController {
    private ProcessController processController;
    private ShelfController shelfcController;
    private WindowController windowController;
    private MicroAppController microAppsController;

    public MainController() {
        shelfcController = new ShelfController();
        windowController = new WindowController();
        processController = new ProcessController();
        microAppsController = new MicroAppController(
                shelfcController.getAllShelfsAsList().stream().map((s) -> s.getId()).toList());
        ProcessController.initProcesses();
    }

    public List<Group> getFreeOpenedTabsAsGroups() throws UserException {
        return windowController.getFreeTabs();
    }

    public List<MicroApp> getFreeOpenedMicroApps() {
        return microAppsController.extractFreeRunningMicroApps();
    }

    public Shelf createNewShelf(String name, Colors color, List<MicroApp> windowsToInclude, List<Group> tabsToInclude)
            throws DeveloperException {
        Shelf toCreate = shelfcController.getNewShelf(name, color);
        toCreate = windowController.addGroupsToShelf(toCreate, tabsToInclude);
        // =====================
        microAppsController.createShelf(toCreate.getId());
        microAppsController.addMicroAppToUsedList(windowsToInclude, toCreate.getId());

        shelfcController.saveShelf(toCreate);
        return toCreate;
    }

    public List<Shelf> getAllShelfs() {
        return shelfcController.getAllShelfsAsList();
    }

    public Shelf closeShelf(Shelf s, List<Group> groups, List<MicroApp> appsToAdd)
            throws UserException, DeveloperException {
                System.out.println("start closing the shelf!");
        microAppsController.closeShelfsApps(s.getId(), appsToAdd);
        Relax.Relax(500);
        windowController.closeShelf(s, groups);
        s.markAsClosed();
        return s;
    }


    public void openShelf(Shelf s) throws UserException, DeveloperException {
        Shelf toUpdate = windowController.openShelf(s);
        microAppsController.openAppsForShelf(s.getId());

    }
    
    

}
