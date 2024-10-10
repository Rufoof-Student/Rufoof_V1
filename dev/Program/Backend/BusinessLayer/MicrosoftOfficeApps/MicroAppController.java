package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import java.util.Map;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Relax;
import dev.Program.DTOs.Exceptions.DeveloperException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MicroAppController {

    public List<MicroApp> appsOnUsage = new ArrayList<>();
    public Map<Integer, List<MicroApp>> shelfId2App = new HashMap<>();

    public MicroAppController(List<Integer> shelfsIds) {
        for (Integer id : shelfsIds) {
            shelfId2App.put(id, new ArrayList<>());
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
            shelfId2App.put(shelfId, new ArrayList<>());
        shelfId2App.get(shelfId).addAll(appsToAdd);
    }

    public void removeAppsFromShelf(List<MicroApp> apps, int shelfId) throws DeveloperException {
        appsOnUsage.removeAll(apps);
        if (!shelfId2App.containsKey(shelfId))
            throw new DeveloperException("trying to remove micro apps from shelf id that not exist");
        shelfId2App.get(shelfId).removeAll(apps);
    }

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
        MicroApp toRet;
        switch (exeName) {
            case "winword.exe":
                toRet = new WordApp();
                toRet.setPropsFromProcess(processObj);
                return toRet;
            case "excel.exe":
                toRet = new ExcelApp();
                toRet.setPropsFromProcess(processObj);
                return toRet;
            case "powerpnt.exe":
                toRet = new PPApp();
                toRet.setPropsFromProcess(processObj);
                return toRet;
            default:
                return null;
        }
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
        if(appsToAdd!=null) addMicroAppToUsedList(appsToAdd, id);
        for (MicroApp microApp : shelfId2App.get(id)) {
            Relax.Relax(500);
            System.out.println("closing "+microApp.getFilePath()+" from shelf "+id);
            microApp.closeApp();
        }
    }

    public void openAppsForShelf(int id) throws DeveloperException {
        if (!shelfId2App.containsKey(id))
            throw new DeveloperException("in microapp controller there is no shelf with id " + id);
        for (MicroApp microApp : shelfId2App.get(id)) {
            Relax.Relax(500);
            microApp.openApp();
        }
    }

}
