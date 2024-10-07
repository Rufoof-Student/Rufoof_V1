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

import dev.Program.DTOs.Exceptions.UserException;

public abstract class MicroApp {
    // static {
    //     try {
    //         // Load the JACOB library
    //         System.load("C:\\Users\\bhaah\\Rufoof_V1\\lib\\jacob-1.21-x64.dll");
    //         LibraryLoader.loadJacobLibrary();
    //     } catch (UnsatisfiedLinkError e) {
    //         System.err.println("JACOB DLL not found or unable to load.");
    //         e.printStackTrace();
    //     }
    // }

    protected String fileFormat;

    protected String appName;

    protected String filePath;

    protected String fileName;

    protected boolean fileIsOpened;

    public void setProps(String path) throws UserException {
        System.out.println("probs must been added");
        filePath=path;
        checkFile(path);
        fileName = "";
        int index = path.length() - 1;
        while (index > 0 && path.charAt(index) != '/') {
            fileName = path.charAt(index) + fileName;
            index--;
        }

    }

    public abstract void checkFile(String path) throws UserException;

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

}
