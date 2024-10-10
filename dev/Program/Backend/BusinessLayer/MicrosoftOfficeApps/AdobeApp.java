package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;




import java.util.ArrayList;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Exceptions.UserException;

public class AdobeApp extends MicroApp {

    public AdobeApp() {
        super("Adobe Acrobat Reader (64-bit)");
        appName = "AcroExch.App";
        fileFormat = ".pdf";
        blankAppName="Adobe Acrobat Reader (64-bit)";
        exeName="Acrobat.exe";
        docType="AVDocs";
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
        
        if(other instanceof AdobeApp){
            
            return filePath.equals(((AdobeApp)other).filePath);
        }   
        return false;
    }

    public void openApp() {

        if (!fileIsOpened) {
            // Create a File object representing the PDF file
        File file = new File(filePath);

        // Check if Desktop is supported on the current platform
        if (Desktop.isDesktopSupported()) {
            // Get the Desktop instance
            Desktop desktop = Desktop.getDesktop();

            // Check if the file exists
            if (file.exists()) {
                try {
                    // Open the PDF file with the default PDF viewer
                    desktop.open(file);
                    System.out.println("Opened file: " + filePath);
                } catch (IOException e) {
                    System.out.println("An error occurred while trying to open the file.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("The file does not exist: " + filePath);
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
        }
        fileIsOpened = true;
    }
    
   

    public static void main(String[] args){
        // AdobeApp a = new AdobeApp();
        // a.filePath = "C:\\Users\\bhaah\\OneDrive\\שולחן העבודה\\Q1,2,3.pdf";
        // a.openApp();
        try {
            // Run the Handle.exe tool from Sysinternals Suite to get the open files
            Process process = Runtime.getRuntime().exec("handle");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Display each line of output
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
