package dev.Program.Backend.BusinessLayer;

import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.m;

import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;
import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroAppController;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.BusinessLayer.Shelf.ShelfController;
import dev.Program.Backend.BusinessLayer.Window.WindowController;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.FreeWindowsToSend;
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

    private static final String chromeName = "chrome.exe";
    private static final String edgeName = "msedge.exe";

    public MainController() {
        shelfcController = new ShelfController();
        windowController = new WindowController(shelfcController.getAllShelfsAsList());
        processController = new ProcessController();
        microAppsController = new MicroAppController(
                shelfcController.getAllShelfsAsList().stream().map((s) -> s.getId()).toList());
        ProcessController.initProcesses();
    }

    public FreeWindowsToSend getFreeWindows() throws UserException{
        FreeWindowsToSend toRet = new FreeWindowsToSend(); 
        toRet.chromeGroups = getFreeOpenedTabsAsGroupsForChrome();
        toRet.edgeGroups = getFreeOpenedTabsAsGroupsForEdge();
        toRet.microsoftApps = getFreeOpenedMicroApps();
        return toRet;
    }

    private List<Group> getFreeOpenedTabsAsGroupsForChrome() throws UserException {
        return windowController.getFreeTabs(chromeName);
    }

    private List<Group> getFreeOpenedTabsAsGroupsForEdge() throws UserException {
        return windowController.getFreeTabs(edgeName);
    }

    private List<MicroApp> getFreeOpenedMicroApps() {
        return microAppsController.extractFreeRunningMicroApps();
    }

    public Shelf createNewShelf(String name, Colors color, FreeWindowsToSend freeWindowsToSend)
            throws DeveloperException {
        Shelf toCreate = shelfcController.getNewShelf(name, color);
        toCreate = windowController.addGroupsToShelf(toCreate, freeWindowsToSend.chromeGroups,chromeName);
        Relax.Relax(100);
        toCreate = windowController.addGroupsToShelf(toCreate, freeWindowsToSend.edgeGroups, edgeName);
                
        // =====================
        microAppsController.createShelf(toCreate.getId());
        microAppsController.addMicroAppToUsedList(freeWindowsToSend.microsoftApps, toCreate.getId());

        shelfcController.saveShelf(toCreate);
        return toCreate;
    }

    public List<Shelf> getAllShelfs() {
        return shelfcController.getAllShelfsAsList();
    }

    public Shelf closeShelf(Shelf s, FreeWindowsToSend free)
            throws UserException, DeveloperException {
                System.out.println("start closing the shelf!");
        microAppsController.closeShelfsApps(s.getId(), free.microsoftApps);
        Relax.Relax(500);
        windowController.closeShelf(s, free.chromeGroups,chromeName);
        Relax.Relax(300);
        windowController.closeShelf(s, free.edgeGroups, edgeName);
        s.markAsClosed();
        return s;
    }


    public void openShelf(Shelf s) throws UserException, DeveloperException {
        windowController.openShelf(s,chromeName);
        Relax.Relax(300);
        windowController.openShelf(s, edgeName);
        Relax.Relax(100);
        microAppsController.openAppsForShelf(s.getId());

    }
    
    public FreeWindowsToSend getWindowInShelf(int id){
        FreeWindowsToSend shelfWindows= new FreeWindowsToSend();
        shelfWindows.chromeGroups = windowController.getShelfGroups(chromeName,id);
        shelfWindows.edgeGroups = windowController.getShelfGroups(edgeName, id);
        shelfWindows.microsoftApps = microAppsController.getShelfApps(id);
        return shelfWindows;
    }
    
    public void updateShelf(int id , FreeWindowsToSend newShelfWindows){
        windowController.setShelfGroups(edgeName, newShelfWindows.edgeGroups, id);
        windowController.setShelfGroups(chromeName, newShelfWindows.chromeGroups, id);
        microAppsController.setShelfApps(id, newShelfWindows.microsoftApps);
    }


}
