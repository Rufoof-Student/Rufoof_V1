package dev.Program.Backend.BusinessLayer.Server;

public class ChromeWindowToSendSocket {
    public String id;
    public TabToSendSocket[] tabs;

    public ChromeWindowToSendSocket(String id,TabToSendSocket[] tabs){
        this.id=id;
        this.tabs=tabs;

    }


    
}
