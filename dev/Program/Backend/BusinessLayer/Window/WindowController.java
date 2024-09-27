package dev.Program.Backend.BusinessLayer.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.Program.Backend.BusinessLayer.Extractions.ChromeExtracion;
import dev.Program.Backend.BusinessLayer.Extractions.Extraction;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.*;

public class WindowController {
    private List<WindowType> types;
    private Map<WindowType,Extraction> extractions;
    private ChromeExtracion extChrome ;

    public WindowController(){
        ExtensionSocketServer socket = new ExtensionSocketServer(8887);
        socket.start();
        extChrome =new ChromeExtracion(socket);
    }

    public Shelf addGroupsToShelf(Shelf toCreate, List<Group> tabsToInclude) {
        return extChrome.createNewGroups(toCreate, tabsToInclude);
    }
    
    public List<Group> getFreeTabs(){
        return extChrome.getFreeTabs();
    }

    public Shelf openShelf(Shelf s) {
       return extChrome.runShelf(s);
    }

    public Shelf closeShelf(Shelf s , List<Group> groupToAdd){
        s=  extChrome.closeShelf(s, groupToAdd);
        s. markAsClosed();
        return s;
    }


}
