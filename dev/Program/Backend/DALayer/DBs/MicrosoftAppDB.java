package dev.Program.Backend.DALayer.DBs;

import com.google.gson.annotations.SerializedName;

import dev.Program.Backend.BusinessLayer.MicrosoftOfficeApps.MicroApp;

public class MicrosoftAppDB {
    
    @SerializedName("ExeName")
    public String exeName;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("IsOpen")
    public boolean isOpen;



    @Override
    public String toString() {
        return "Window{" +
                
                " path='" + filePath + '\'' +
                '}';
    }



    public static MicrosoftAppDB Serialize(MicroApp microApp) {
        MicrosoftAppDB result = new MicrosoftAppDB();
        result.exeName = microApp.getExeName();
        result.filePath = microApp.getFilePath();
        result.isOpen = microApp.getIsOpen();
        return result;
    }
}
