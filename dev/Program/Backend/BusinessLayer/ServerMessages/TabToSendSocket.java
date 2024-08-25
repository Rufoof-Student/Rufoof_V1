package dev.Program.Backend.BusinessLayer.ServerMessages;

public class TabToSendSocket {
    public String url;
    public String tittle;
    public String id;
    

    
    public TabToSendSocket(String url,String tittle,String id){
        this.id= id;
        this.tittle = tittle;
        this.url=url;
    }

    public TabToSendSocket(String json){
        //TODO convert from json to TabToSendSocket
    }

    
}
