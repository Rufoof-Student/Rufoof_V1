package dev.Program.Backend.DALayer.DBs;

import com.google.gson.annotations.SerializedName;

import dev.Program.DTOs.Tab;

public class TabDB {
    
    @SerializedName("URL")
    public String url;


    @SerializedName("Title")
    public String title; 

    @SerializedName("TabId")
    public String nativeTabId;

    public static TabDB serialize(Tab tab) {
        TabDB res = new TabDB();
        res.nativeTabId=tab.getNativeTabId();
        res.title = tab.getTitle();
        res.url=tab.getUrl();
        return res;
    }


    

}
