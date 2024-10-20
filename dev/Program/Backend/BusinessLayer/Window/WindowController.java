package dev.Program.Backend.BusinessLayer.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.Program.Backend.BusinessLayer.Extractions.ChromeExtracion;
import dev.Program.Backend.BusinessLayer.Extractions.Extraction;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.DALayer.DAOs.Writer;
import dev.Program.Backend.DALayer.DBs.ShelfDB;
import dev.Program.DTOs.*;
import dev.Program.DTOs.Exceptions.UserException;

public class WindowController {

    private ChromeExtracion extChrome;
    private ChromeExtracion extEdg;
    // public WindowController(List<Shelf> shelfs){
    // ExtensionSocketServer socket = new ExtensionSocketServer(8887);
    // socket.start();
    // extChrome =new ChromeExtracion(socket,"chrome.exe",shelfs);
    // extEdg = new ChromeExtracion(socket, "msedge.exe",shelfs);
    // }

    private Writer writer = new Writer();

    public WindowController(List<ShelfDB> allShelfsFromDB) {

        ExtensionSocketServer socket = new ExtensionSocketServer(8887);
        socket.start();

        extChrome = new ChromeExtracion(socket, "chrome.exe", allShelfsFromDB);
        extEdg = new ChromeExtracion(socket, "msedge.exe", allShelfsFromDB);
    }

    public Shelf addGroupsToShelf(Shelf toCreate, List<Group> tabsToInclude, String chromeEngineName) {

        if (tabsToInclude == null || tabsToInclude.size() == 0)
            return toCreate;
        for (Group group : tabsToInclude) {
            group.setShelfProperties(toCreate);
            System.out.println(chromeEngineName + ":" + group.getTabs().size());

        }
        ChromeExtracion ext = getExt(chromeEngineName);
        ext.createNewGroups(toCreate.getId(), tabsToInclude);
        return toCreate;
    }

    public List<Group> getFreeTabs(String engineName) throws UserException {
        ChromeExtracion ext = getExt(engineName);
        return ext.getFreeTabs();
    }

    private ChromeExtracion getExt(String engineName) {
        return engineName == "chrome.exe" ? extChrome : extEdg;
    }

    public Shelf openShelf(Shelf s, String engineName) throws UserException {

        GroupPack updatedGroupPack = getExt(engineName).runShelf(s);
        if (engineName.equals("chrome.exe"))
            writer.updateShelfGroupsForChrome(s.getId(), updatedGroupPack);
        else
            writer.updateShelfGroupsForEdge(s.getId(), updatedGroupPack);
        return s;
    }

    public Shelf closeShelf(Shelf s, List<Group> groupToAdd, String engineName) throws UserException {

        GroupPack closedGroupPack = getExt(engineName).closeShelf(s, groupToAdd);
        if (engineName.equals("chrome.exe"))
            writer.updateShelfGroupsForChrome(s.getId(), closedGroupPack);
        else
            writer.updateShelfGroupsForEdge(s.getId(), closedGroupPack);

        return s;
    }

    public void persistWindows(int id) {
        writer.updateShelfGroupsForChrome(id, extChrome.getShelfPack(id));
        writer.updateShelfGroupsForEdge(id, extEdg.getShelfPack(id));
    }

}
