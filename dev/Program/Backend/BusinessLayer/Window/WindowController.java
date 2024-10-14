package dev.Program.Backend.BusinessLayer.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.Program.Backend.BusinessLayer.Extractions.ChromeExtracion;
import dev.Program.Backend.BusinessLayer.Extractions.Extraction;
import dev.Program.Backend.BusinessLayer.Server.ExtensionSocketServer;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.*;
import dev.Program.DTOs.Exceptions.UserException;

public class WindowController {
    private List<WindowType> types;
    private Map<WindowType,Extraction> extractions;
    private ChromeExtracion extChrome ;
    private ChromeExtracion extEdg;
    public WindowController(List<Shelf> shelfs){
        ExtensionSocketServer socket = new ExtensionSocketServer(8887);
        socket.start();
        extChrome =new ChromeExtracion(socket,"chrome.exe",shelfs);
        extEdg = new ChromeExtracion(socket, "msedge.exe",shelfs);
    }

    

    public Shelf addGroupsToShelf(Shelf toCreate, List<Group> tabsToInclude,String chromeEngineName) {
        for (Group group : tabsToInclude) {
            group.setShelfProperties(toCreate);
        }
        ChromeExtracion ext = getExt(chromeEngineName);
        ext.createNewGroups(toCreate.getId(), tabsToInclude);
        return toCreate;
    }
    
    public List<Group> getFreeTabs(String engineName) throws UserException{
        ChromeExtracion ext = getExt(engineName);
        return ext.getFreeTabs();
    }

    private ChromeExtracion getExt(String engineName) {
        return engineName=="chrome.exe"?extChrome:extEdg;
    }

    public Shelf openShelf(Shelf s,String engineName) throws UserException {
        
       return getExt(engineName).runShelf(s);
    }

    public Shelf closeShelf(Shelf s , List<Group> groupToAdd,String engineNeme) throws UserException{
        
        s=  getExt(engineNeme).closeShelf(s, groupToAdd);
        
        return s;
    }



    public List<Group> getShelfGroups(String chromename, int id) {
        return getExt(chromename).getShelfGroups(id);
    }

    public void setShelfGroups(String engineName,List<Group> groups,int id){
        getExt(engineName).setShelfGroups(groups,id);
    }


}
