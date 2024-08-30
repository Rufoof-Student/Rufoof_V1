package dev.Program.Backend.BusinessLayer.Extractions;

import java.util.ArrayList;
import java.util.List;

import dev.Program.Backend.BusinessLayer.Process.Process;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Server.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.ChromeWindow;
import dev.Program.DTOs.Tab;

public class ChromeExtracion {

    private ExtensionSocketServer server;
    private List<ChromeWindow> runningWindows;
    private int idCounter;

    public ChromeExtracion(ExtensionSocketServer server) {
        this.server = server;
        runningWindows = server.getCurrentGoogleOpenedWindows();
        for (ChromeWindow chromeWindow : runningWindows) {
            idCounter = idCounter<chromeWindow.getChromeId()?chromeWindow.getChromeId():idCounter;
        }
    }

    /**
     * Creates groups and moves specified tabs to these groups. Sets the tabs to the
     * shelf and returns the shelf with the updated group data.
     * 
     * @param shelf contains data for the new groups, such as name, color, etc.
     * @param tabs  the tabs to move to the groups.
     * @return the shelf with updated group information.
     */
    public Shelf createNewGroups(Shelf shelf, List<Tab> tabs) {
        // TODO
        
        return shelf;
    }

    /**
     * Opens the groups in the specified shelf.
     * 
     * @param shelf the shelf whose groups will be opened.
     * @return the shelf with updated data, such as group IDs.
     */
    public Shelf runShelf(Shelf shelf) {
        // TODO foreach group:
        // check chrome id , if not open to open one with this id.
        // adding the group to the chrome.
        return shelf;
    }

    /**
     * Adds tabs to their respective groups and closes all tabs in the new groups.
     * If a tab is not open, it will remain in the shelf.
     * 
     * @param shelf the shelf containing the groups and tabs.
     * @param tabs  the tabs to be added and/or closed.
     * @return the shelf with updated group and tab data.
     */
    public Shelf closeShelf(Shelf shelf, List<Tab> tabs) {
        // TODO
        return shelf;
    }

    /**
     * Returns all tabs that are not assigned to any group.
     * 
     * @return a list of ungrouped (free) tabs.
     */
    public List<Tab> getFreeTabs() {
        // TODO
        return server.getFreeTabs();
    }

    private void insureConnection() {
        // TODO if there is no connection To connect
    }

}
