package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Exceptions.UserException;

public class WordApp extends MicroApp {


    public WordApp() {
        super("Word");
        appName = "Word.Application";
        fileFormat = ".docx";
        blankAppName="Word";
        exeName="WINWORD.EXE";
        docType = "Documents";
    }

    public void setPropsFromProcess(ProcessObj processObj) {
        String processTitle = getFileNameFromTitle(processObj.getTitle());
        fileName = getFileNameFromTitle(processTitle);
        fileIsOpened = true;
        initFilePath();
        System.out.println(fileName+","+filePath);
    }

    @Override
    public boolean equals(Object other){
        System.out.println("check if equal");
        if(other == null  ) return false;
        if(other instanceof WordApp){
            
            System.out.println("curr path :"+filePath);
            System.out.println("other path :"+((WordApp)other).filePath);
            return filePath.equals(((WordApp)other).filePath);
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
                    System.out.println("The path of the first document is: " + docPath+"=?"+filePath);
                    if (docPath.equals(filePath)) {
                        System.out.println("we found the relevant file path to close "+docPath);
                        windowToClose = Dispatch.call(documents, "Item", new Variant(i)).toDispatch();
                        // Dispatch.call(doc, "Close", true);
                    }
                } else {
                    System.out.println("Unable to access the document.");
                }
            }
            if (windowToClose != null)
                Dispatch.call(windowToClose, "Close", true);
            fileIsOpened = false;
            ProcessController.removeBlankApp(blankAppName, exeName);
        }
    }

}
