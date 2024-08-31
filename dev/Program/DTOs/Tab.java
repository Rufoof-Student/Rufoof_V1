package dev.Program.DTOs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.Program.Backend.BusinessLayer.Server.TabToSendSocket;

public class Tab {
    private String url;
    private String nativeTabId;
    private String title;
    private String nativeWindowId;

    public Tab(String url,String tabId,String title,String windowId){
        this.nativeTabId=tabId;
        this.title=title;
        this.url=url;
        this.nativeWindowId = windowId;
    }
    public Tab(String url,String title){
        this.title=title;
        this.url=url;
    }
    



    public Tab(TabToSendSocket tabToSend) {
        nativeTabId = tabToSend.id;
        title= tabToSend.tittle;
        url= tabToSend.url;
    }


    @Override
    public String toString(){
        return "Tab with id:"+nativeTabId+",title:"+title+",url: "+url+",windowId:"+nativeWindowId;
    }


    public boolean equals(Object another){
        if(another instanceof Tab){
            Tab anotherTab = (Tab) another;
            return anotherTab.getNativeTabId().equals(nativeTabId);
        }   
        return false;
    }

    public String getUrl() {
        return url;
    }
    public String getNativeTabId() {
        return nativeTabId;
    }
    public String getTitle() {
        return title;
    }

    /**
     * JSON structure:[{"windowId": String | int ,"generatedId": int ,"tabs":[{"id": String | int ,"url": String ,"groupId": int(-1) }]
     * @param data
     * @return
     */
    public static List<Tab> convertJson2FreeTabs(String data){
        JsonParser parser = new JsonParser();
        JsonElement parsedData=parser.parse(data) ;
        JsonArray arrayOfWindows=parsedData.getAsJsonArray();
        List<Tab> TabsToReturn= new ArrayList<>();
        for (JsonElement windowJson : arrayOfWindows) {
            JsonObject window = windowJson.getAsJsonObject();
            String nativeWindowId=window.get("windowId").getAsString();
            JsonArray tabsAsJsonArray = window.get("tabs").getAsJsonArray();
            TabsToReturn.addAll(createTabsFromJsonArray(tabsAsJsonArray, nativeWindowId));
        }
        return TabsToReturn;
    }

    public static List<Tab> createTabsFromJsonArray(JsonArray tabsAsJsonArray, String nativeWindowId) {
        List<Tab> toRet = new ArrayList<>();
        for (JsonElement tabAsJsonElement : tabsAsJsonArray) {
            JsonObject jsonTab = tabAsJsonElement.getAsJsonObject();
            toRet.add(Tab.convertJson2OneTab(jsonTab, nativeWindowId));
        }
        return toRet;
    }

    public static Tab convertJson2OneTab(JsonObject jsonTab,String nativeWindowId){
        String url = jsonTab.get("url").getAsString();
        String nativeTabId = jsonTab.get("nativeTabId").getAsString();
        String title = jsonTab.get("title").getAsString();
        return new Tab(url, nativeTabId, title, nativeWindowId);
    }
    public void markAsClosed() {
        nativeTabId=null;
        nativeWindowId = null;
    }
}
