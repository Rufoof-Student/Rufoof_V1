package dev.Program.Backend.BusinessLayer;

public class Tab {
    private String url;
    private String id;
    private String title;

    public Tab(String url,String id,String title){
        this.id=id;
        this.title=title;
        this.url=url;
    }
    public Tab(String url){
        
        this.url=url;
    }


    @Override
    public String toString(){
        return "Tab with id:"+id+",title:"+title+",url: "+url;
    }


    public String getUrl() {
        return url;
    }
}
