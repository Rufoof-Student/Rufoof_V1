package dev.Program.Backend.BusinessLayer;

import dev.Program.Backend.BusinessLayer.ServerMessages.TabToSendSocket;

public class Tab {
    private String url;
    private String id;
    private String title;

    public Tab(String url,String id,String title){
        this.id=id;
        this.title=title;
        this.url=url;
    }
    public Tab(String url,String title){
        this.title=title;
        this.url=url;
    }
    


    public Tab(TabToSendSocket tabToSend) {
        id = tabToSend.id;
        title= tabToSend.tittle;
        url= tabToSend.url;
    }

    
    @Override
    public String toString(){
        return "Tab with id:"+id+",title:"+title+",url: "+url;
    }


    public String getUrl() {
        return url;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}
