package dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.util.ArrayList;
import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.DTOs.Exceptions.UserException;

public class ExcelApp extends MicroApp {

    public ExcelApp() {
        super("Excel");
        appName = "Excel.Application";
        fileFormat = ".xlsx";
        blankAppName="Excel";
        exeName="EXCEL.EXE";
        docType="Workbooks";
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
        
        if(other instanceof ExcelApp){
            
            return filePath.equals(((ExcelApp)other).filePath);
        }   
        return false;
    }

}
