package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.util.ArrayList;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Relax;
import dev.Program.DTOs.Exceptions.UserException;

public class PPApp extends MicroApp {

    public PPApp() {
        super("Power Point");
        appName = "PowerPoint.Application";
        fileFormat = ".pptx";
        blankAppName = "PowerPoint";
        exeName = "POWERPNT.EXE";
        docType = "Presentations";
        // closeReq="Quit";
    }

    

    public void setPropsFromProcess(ProcessObj processObj) {
        String processTitle = getFileNameFromTitle(processObj.getTitle());
        fileName = getFileNameFromTitle(processTitle);
        fileIsOpened = true;
        initFilePath();
        System.out.println(fileName + "," + filePath);
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof PPApp) {

            return filePath.equals(((PPApp) other).filePath);
        }
        return false;
    }

    public void closeApp() {
        disableProtectedView();
        if (fileIsOpened) {
            ActiveXComponent microApp = ActiveXComponent.connectToActiveInstance(appName);
            microApp.setProperty("Visible", new Variant(true));
            Dispatch documents = microApp.getProperty(docType).toDispatch();
            // Dispatch windows = microApp.getProperty("Windows").toDispatch();
            int docCount = Dispatch.get(documents, "Count").getInt();
            System.out.println(docCount);
            Dispatch windowToClose = null;
            for (int i = 1; i <= docCount; i++) {
                // Get the first document
                Dispatch doc = Dispatch.call(documents, "Item", new Variant(i)).toDispatch();

                // Check if document is actually open
                if (doc != null) {
                    // Get the full path of the document
                    String docPath = Dispatch.get(doc, "FullName").toString();
                    System.out.println("The path of the first document is: " + docPath + "=?" + filePath);
                    if (docPath.equals(filePath)) {
                        System.out.println("we found the relevant file path to close " + docPath);
                        // windowToClose = Dispatch.call(documents, "Item", new Variant(i)).toDispatch();
                        Dispatch.call(doc, "Close");
                    }
                } else {
                    System.out.println("Unable to access the document.");
                }
            }
            // if (windowToClose != null)
            // if (closeReq.equals("Close"))
            // Dispatch.call(windowToClose, closeReq, true);
            // else
            // Dispatch.call(windowToClose, closeReq);
            fileIsOpened = false;
            ProcessController.removeBlankApp(blankAppName, exeName);
        }
    }

    public static void main(String[] args) {
        PPApp p = new PPApp();
       p.disableProtectedView();
    }

}
