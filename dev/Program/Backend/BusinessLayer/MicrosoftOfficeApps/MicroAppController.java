package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import java.util.Map;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MicroAppController {
    
    public List<MicroApp> appsOnUsage = new ArrayList<>();



    public List<MicroApp> extractFreeRunningMicroApps(){
        List<ProcessObj> running = ProcessController.getRunningProccesses();
        List<MicroApp> allFreeApps = new ArrayList<>();
        for (ProcessObj processObj : running) {
            MicroApp app = process2MicroApp(processObj);  
            if(app!=null && !appsOnUsage.contains(app)){
                allFreeApps.add(app);
                System.out.println(app.getFileName() +" <<<<<<<<<<<<<<<<<<<");
            }
        }
        return allFreeApps;
    }



    private MicroApp process2MicroApp(ProcessObj processObj) {
        String exeName = processObj.getExeName().toLowerCase();
        // System.out.println(exeName);
        switch (exeName) {
            case "winword.exe":
                WordApp toRet =  new WordApp();
                toRet.setPropsFromProcess(processObj);
                System.out.println("it is word process");
                return toRet;
                // break;
        
            default:
            return null;
                // break;
        }
    }

    public static void main(String[] args){
        MicroAppController a = new MicroAppController();
        List<MicroApp> apps =a.extractFreeRunningMicroApps();
        //ProcessController.initProcesses();
        MicroApp toClose = apps.get(0);
        System.out.println("to close "+toClose.getFileName());
        toClose.closeApp();
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        toClose.openApp();

    }
    
}
