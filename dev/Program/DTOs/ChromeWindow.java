package dev.Program.DTOs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.Program.Backend.BusinessLayer.Server.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.Server.TabToSendSocket;
public class ChromeWindow {
    private int chromeId;
    private String windowId;
    private List<Tab> freeTabs;
    private Map<Integer,Group> groupInShelfId;
    // private ExtensionSocketServer extension;
    private boolean isOpenedInDesktop;

    public ChromeWindow(String windowId,List<Tab> tabs,boolean isOpened){
        this.windowId= windowId;
        this.freeTabs=tabs;
        // extension=server;
        isOpenedInDesktop=isOpened;
    }

    public void setChromeId(int id){
        chromeId=id;
    }
    

    public ChromeWindow(ChromeWindowToSendSocket chromeToSend, boolean isOpened) {
        windowId = chromeToSend.id;
        // extension=extensionSocketServer;
        freeTabs =getTabsFromTabsToSend(chromeToSend.tabs);
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
        for (Tab tab : freeTabs) {
            s+=tab.toString()+'\n';
        } 
        return s;
    }

    public List<Tab> getFreeTabs() {
        return freeTabs;
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
        // if(extension.connectionIsOpenedWithGoogle()) {
        //     //TODO open the chrome window and connect to the server.
        // }
        // ChromeWindowToSendSocket openedWindow = extension.openWindows(this);
        // if(openedWindow==null) {
        //     return false;
        // }
        // this.windowId = openedWindow.id;
        // this.tabs=( getTabsFromTabsToSend(openedWindow.tabs));
        return true;
    }


    public boolean openNewTabs(List<TabToSendSocket> newTabs){
        return false;
    }


	public String getId() {
		return windowId;
	}



    public void markAsClosed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAsClosed'");
    }



    public void markTabsAsClosed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markTabsAsClosed'");
    }

    public int getChromeId() {
        return chromeId;
    }


}
