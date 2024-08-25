package dev.Program.Backend.BusinessLayer;
import java.util.ArrayList;
import java.util.List;
public class ChromeWindow extends Window {
    private String windowId;
    private List<Tab> tabs;
    private ExtensionSocketServer extension;
    private boolean isOpenedInDesktop;

    public ChromeWindow(String windowId,List<Tab> tabs,ExtensionSocketServer server,boolean isOpened){
        this.windowId= windowId;
        this.tabs=tabs;
        extension=server;
        isOpenedInDesktop=isOpened;
    }

    

    @Override
    public String toString(){
        String s = "-Window with id:"+ windowId+" .\n";
        for (Tab tab : tabs) {
            s+=tab.toString()+'\n';
        } 
        return s;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public boolean openWindow(){
        
        if(extension.openWindows(this)==null) {
            //TODO open the chrome window then call the openwindow again
            return false;
        }else{

        }
    }


}
