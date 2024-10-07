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
    private String processTitle;

    public void setId(IntByReference id){
        this.id=id;
    }
    

    public void setExePath(String path){
        exePath = path;
    }


    public String getExePath(){
        return exePath;
    }


    public String getExeName() {
        String toRet = "";
        int index = exePath.length() - 1;
        while (index > 0 &&( exePath.charAt(index) != '/' && exePath.charAt(index) != '\\') ) {
            toRet = exePath.charAt(index) + toRet;
            index--;
        }
        return toRet;
    }


    public void setProcessTitle(String wText) {
        processTitle=wText;
    }


    public String getTitle() {
        return processTitle;
    }
    

}
