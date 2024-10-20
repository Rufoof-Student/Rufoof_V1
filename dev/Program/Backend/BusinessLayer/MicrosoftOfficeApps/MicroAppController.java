package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import java.util.Map;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.Backend.DALayer.DAOs.Writer;
import dev.Program.Backend.DALayer.DBs.MicrosoftAppDB;
import dev.Program.Backend.DALayer.DBs.ShelfDB;
// import dev.Program.Backend.DALayer.MainDBControlers.MicrosoftAppsDBController;
import dev.Program.DTOs.Relax;
import dev.Program.DTOs.Exceptions.DeveloperException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MicroAppController {

    private Writer Writer=new Writer();

    public List<MicroApp> appsOnUsage = new ArrayList<>();
    public Map<Integer, List<MicroApp>> shelfId2App = new HashMap<>();

    public MicroAppController(List<ShelfDB> shelfsFromDB){
        for (ShelfDB shelfDB : shelfsFromDB) {
            int idForShelf = shelfDB.shelfId;
            List<MicrosoftAppDB> appsOnShelfAsDO = shelfDB.windows;
            List<MicroApp> appsToAdd = new ArrayList<>();
            for (MicrosoftAppDB appInShelfAsDO : appsOnShelfAsDO) {
                MicroApp app =exeNameToMicroApp(appInShelfAsDO.exeName);
                if(app!=null){
                    app.setDataFromDB(appInShelfAsDO);
                    appsToAdd.add(app);
                    appsOnUsage.add(app);
                }

            }
            shelfId2App.put(idForShelf, appsToAdd);
        }
    }

    public List<MicroApp> extractFreeRunningMicroApps() {
        List<ProcessObj> running = ProcessController.getRunningProccesses();
        List<MicroApp> allFreeApps = new ArrayList<>();
        for (ProcessObj processObj : running) {
            MicroApp app = process2MicroApp(processObj);
            if (app != null && app.getFilePath() != null && !appsOnUsage.contains(app)) {
                allFreeApps.add(app);
                System.out.println(app.getFileName() + " <<<<<<<<<<<<<<<<<<<");
            }
        }
        return allFreeApps;
    }

    public void addMicroAppToUsedList(List<MicroApp> appsToAdd, int shelfId) {
        appsOnUsage.addAll(appsToAdd);
        if (!shelfId2App.containsKey(shelfId))
            // throw new DeveloperException("trying to add apps ")
            shelfId2App.put(shelfId, new ArrayList<>());

        for (MicroApp microApp : appsToAdd) {
            microApp.addAppToShelf(shelfId);
        }
        shelfId2App.get(shelfId).addAll(appsToAdd);
        
    }

    public void removeAppsFromShelf(List<MicroApp> apps, int shelfId) throws DeveloperException {
        appsOnUsage.removeAll(apps);
        if (!shelfId2App.containsKey(shelfId))
            throw new DeveloperException("trying to remove micro apps from shelf id that not exist");
        shelfId2App.get(shelfId).removeAll(apps);
        Writer.updateShelfMicroApps(shelfId,shelfId2App.get(shelfId));    }

    public void createShelf(int id) throws DeveloperException {

        if (shelfId2App.containsKey(id))
            throw new DeveloperException("in microapp controller trying to add existing id for shelf");
        shelfId2App.put(id, new ArrayList<>());
    }

    public void removeShelf(int id) throws DeveloperException {
        if (!shelfId2App.containsKey(id))
            throw new DeveloperException("in microapp controller there is no shelf with id " + id);
        appsOnUsage.removeAll(shelfId2App.get(id));
    }

    private MicroApp process2MicroApp(ProcessObj processObj) {
        String exeName = processObj.getExeName().toLowerCase();
        MicroApp result = exeNameToMicroApp(exeName);
        if(result!=null) result.setPropsFromProcess(processObj);
        return result;
    }

    private MicroApp exeNameToMicroApp(String exeName){
        MicroApp toRet;
        switch (exeName.toLowerCase()) {
            case "winword.exe":
                toRet = new WordApp();
                break;
            case "excel.exe":
                toRet = new ExcelApp();
                // toRet.setPropsFromProcess(processObj);
                // return toRet;
                break;
            case "powerpnt.exe":
                toRet = new PPApp();
                break;
            default:
                return null;
        }
        
        return toRet;
    }

    public static void main(String[] args) {
        MicroAppController a = new MicroAppController(new ArrayList<>());
        List<MicroApp> apps = a.extractFreeRunningMicroApps();
        MicroApp toClose = apps.get(0);
        System.out.println("to close " + toClose.getFileName());
        toClose.closeApp();
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        toClose.openApp();

    }

    public void closeShelfsApps(int id, List<MicroApp> appsToAdd) throws DeveloperException {
        if (!shelfId2App.containsKey(id))
            throw new DeveloperException("in microapp controller there is no shelf with id " + id);
        if (appsToAdd != null)
            addMicroAppToUsedList(appsToAdd, id);
        for (MicroApp microApp : shelfId2App.get(id)) {
            Relax.Relax(500);
            System.out.println("closing " + microApp.getFilePath() + " from shelf " + id);
            microApp.closeApp();
        }
        Writer.updateShelfMicroApps(id,shelfId2App.get(id));
    }

    public void openAppsForShelf(int id) throws DeveloperException {
        if (!shelfId2App.containsKey(id))
            throw new DeveloperException("in microapp controller there is no shelf with id " + id);
        for (MicroApp microApp : shelfId2App.get(id)) {
            Relax.Relax(500);
            microApp.openApp();
        }
        Writer.updateShelfMicroApps(id,shelfId2App.get(id));
    }

    public List<MicroApp> getShelfApps(int id) {
        return shelfId2App.get(id);
    }

    public List<MicroApp> getShelfApps(int id) {
        return shelfId2App.get(id) == null ? new ArrayList<>() : shelfId2App.get(id);
    }

    public void setShelfApps(int id, List<MicroApp> apps) {
        if (apps != null)
            shelfId2App.put(id, apps);
    }

}
