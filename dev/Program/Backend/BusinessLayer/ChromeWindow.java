package dev.Program.Backend.BusinessLayer;
import java.util.ArrayList;
import java.util.List;

import dev.Program.Backend.BusinessLayer.ServerMessages.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.ServerMessages.TabToSendSocket;
import dev.Program.Backend.DTOs.Errors;
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

    

    public ChromeWindow(ChromeWindowToSendSocket chromeToSend, ExtensionSocketServer extensionSocketServer,boolean isOpened) {
        windowId = chromeToSend.id;
        extension=extensionSocketServer;
        tabs =getTabsFromTabsToSend(chromeToSend.tabs);
        isOpenedInDesktop=isOpened;
    }

    private List<Tab> getTabsFromTabsToSend(TabToSendSocket[] tabsToSend){
        List<Tab> tabsToRet = new ArrayList<>();
        for (TabToSendSocket tab : tabsToSend) {
            tabsToRet.add(new Tab(tab));
        }
        return tabsToRet;
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


    /**
     * Will open the tabs , if the id is exist will open in the same window , if not will open new window then get the new IDs of window and tabs
     * Use when opening the whole tabs and the window is not opened .
     * @return true if everything passed.
     * @throws Exception if the window already opened.
     */
    public boolean openWindow() throws Exception{
        if(isOpenedInDesktop) throw new IllegalArgumentException(Errors.window_already_opened);
        isOpenedInDesktop =true;
        if(extension.connectionIsOpenedWithGoogle()) {
            //TODO open the chrome window and connect to the server.
        }
        ChromeWindowToSendSocket openedWindow = extension.openWindows(this);
        if(openedWindow==null) {
            return false;
        }
        this.windowId = openedWindow.id;
        this.tabs=( getTabsFromTabsToSend(openedWindow.tabs));
        return true;
    }


    public boolean openNewTabs(List<TabToSendSocket> newTabs){
        return false;
    }


	public String getId() {
		return windowId;
	}


}
