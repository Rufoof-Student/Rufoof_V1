package dev.Program.Backend.BusinessLayer.Process;


import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;


public class ProcessObj {
    
    private String exePath ;
    private IntByReference id;


    public void setId(IntByReference id){
        this.id=id;
    }
    

    public void setExePath(String path){
        exePath = path;
    }


    public String getExePath(){
        return exePath;
    }
    

}
