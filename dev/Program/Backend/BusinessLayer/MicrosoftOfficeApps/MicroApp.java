package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import java.io.InputStream;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Exceptions.UserException;

public abstract class MicroApp {

    protected String fileFormat;

    protected String appName;

    protected String filePath;

    protected String fileName;

    protected String blankAppName;

    protected String exeName;

    protected boolean fileIsOpened;

    protected String docType;

    protected String closeReq = "Close";

    public void setProps(String path) throws UserException {
        System.out.println("probs must been added");
        filePath = path;
        fileName = "";
        int index = path.length() - 1;
        while (index > 0 && path.charAt(index) != '/') {
            fileName = path.charAt(index) + fileName;
            index--;
        }

    }

    public void disableProtectedView() {
        ActiveXComponent powerPointApp = ActiveXComponent.connectToActiveInstance(appName);
        
        // Get the ProtectedViewWindows collection
        Dispatch protectedViewWindows = powerPointApp.getProperty("ProtectedViewWindows").toDispatch();
        int protectedViewCount = Dispatch.get(protectedViewWindows, "Count").getInt();
        
        if (protectedViewCount > 0) {
            System.out.println("Files in Protected View: " + protectedViewCount);
            for (int i = 1; i <= protectedViewCount; i++) {
                // Get each Protected View window
                Dispatch protectedViewWindow = Dispatch.call(protectedViewWindows, "Item", new Variant(i)).toDispatch();
                Dispatch.call(protectedViewWindow, "Edit");
            }
        } else {
            System.out.println("No files are in Protected View.");
        }
    }

    public void openApp() {

        if (!fileIsOpened) {
            
            ActiveXComponent wordApp = ActiveXComponent.connectToActiveInstance(appName);
            if(wordApp==null) wordApp = new ActiveXComponent(appName);
            wordApp.setProperty("Visible", new Variant(true)); // Make Word visible
            Dispatch documents = wordApp.getProperty(docType).toDispatch();
            System.out.println(filePath);

            Dispatch.call(documents, "Open", new Variant(filePath), new Variant(false),               // ConfirmConversions
            new Variant(false) ).toDispatch();

        }
        fileIsOpened = true;
    }

    public String getFileNameFromTitle(String processTitle) {
        int formatIndex = processTitle.indexOf(fileFormat);
        if (formatIndex <= 0)
            return "";
        return processTitle.substring(0, formatIndex) + fileFormat;
        // while(formatIndex>0){
        // toRet = processTitle.char
        // }

    }

    public void initFilePath() {
        disableProtectedView();
        if (fileIsOpened) {
            ActiveXComponent microApp = ActiveXComponent.connectToActiveInstance(appName);
            Dispatch documents = microApp.getProperty(docType).toDispatch();
            int docCount = Dispatch.get(documents, "Count").getInt();
            System.out.println(docCount);
            for (int i = 1; i <= docCount; i++) {
                Dispatch doc = Dispatch.call(documents, "Item", new Variant(i)).toDispatch();
                if (doc != null) {
                    // Get the full path of the document
                    String docPath = Dispatch.get(doc, "FullName").toString();
                    System.out.println("The path of the document is: " + docPath);
                    if (docPath.endsWith(fileName))
                        filePath = docPath;
                } 
            }

        }
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
               if(closeReq.equals("Close")) Dispatch.call(windowToClose, closeReq, true);
               else Dispatch.call(windowToClose, closeReq);
            fileIsOpened = false;
            ProcessController.removeBlankApp(blankAppName, exeName);
        }
    }

    public abstract void setPropsFromProcess(ProcessObj processObj);


    public String getFileName() {
        return fileName;
    }

    public String getBlankAppName(){
        return blankAppName;
    }

    protected  String getFilePath(){
        return filePath;
    }
}
