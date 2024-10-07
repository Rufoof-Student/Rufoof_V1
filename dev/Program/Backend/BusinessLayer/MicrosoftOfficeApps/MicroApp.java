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

import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Exceptions.UserException;

public abstract class MicroApp {

    protected String fileFormat;

    protected String appName;

    protected String filePath;

    protected String fileName;

    protected boolean fileIsOpened;

    public void setProps(String path) throws UserException {
        System.out.println("probs must been added");
        filePath = path;
        checkFile(path);
        fileName = "";
        int index = path.length() - 1;
        while (index > 0 && path.charAt(index) != '/') {
            fileName = path.charAt(index) + fileName;
            index--;
        }

    }

    public void openApp() {

        if (!fileIsOpened) {
            ActiveXComponent wordApp = new ActiveXComponent(appName);
            wordApp.setProperty("Visible", new Variant(true)); // Make Word visible
            Dispatch documents = wordApp.getProperty("Documents").toDispatch();
            System.out.println(filePath);

            Dispatch.call(documents, "Open", new Variant(filePath)).toDispatch();

        }
        fileIsOpened = true;
    }

    public String getFileNameFromTitle(String processTitle) {
        int formatIndex = processTitle.indexOf(fileFormat);
        return processTitle.substring(0, formatIndex) + fileFormat;
        // while(formatIndex>0){
        // toRet = processTitle.char
        // }

    }

    public void initFilePath() {
        if (fileIsOpened) {
            ActiveXComponent microApp = ActiveXComponent.connectToActiveInstance(appName);
            Dispatch documents = microApp.getProperty("Documents").toDispatch();
            int docCount = Dispatch.get(documents, "Count").getInt();
            System.out.println(docCount);
            for (int i = 1; i <= docCount; i++) {
                // Get the first document
                Dispatch doc = Dispatch.call(documents, "Item", new Variant(i)).toDispatch();

                // Check if document is actually open
                if (doc != null) {
                    // Get the full path of the document
                    String docPath = Dispatch.get(doc, "FullName").toString();
                    System.out.println("The path of the first document is: " + docPath);
                    if(docPath.endsWith(fileName)) filePath = docPath;
                } else {
                    System.out.println("Unable to access the document.");
                }
            }

        }
    }


    public void closeApp(){
        if (fileIsOpened) {
            ActiveXComponent microApp = ActiveXComponent.connectToActiveInstance(appName);
            Dispatch documents = microApp.getProperty("Documents").toDispatch();
            Dispatch windows = microApp.getProperty("Windows").toDispatch();
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
                    System.out.println("The path of the first document is: " + docPath);
                    if(docPath.equals(filePath)){
                        windowToClose = Dispatch.call(windows, "Item", new Variant(i)).toDispatch();

                        
                    }
                } else {
                    System.out.println("Unable to access the document.");
                }
            }
            if(windowToClose!=null) Dispatch.call(windowToClose, "Close", false);
            fileIsOpened=false;
        }
    }

    public abstract void setPropsFromProcess(ProcessObj processObj);

    public abstract void checkFile(String path) throws UserException;

    public String getFileName() {
        return fileName;
    }
}
