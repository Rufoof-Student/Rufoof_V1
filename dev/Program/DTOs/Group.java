package dev.Program.DTOs;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.Program.Backend.BusinessLayer.Shelf.Shelf;

public class Group {
    private List<Tab> tabs;
    private String nativeWindowId;
    private int chromeId;

    private int shelfId;
    private Colors color;
    private String name;

    private Group(String nativeWindowId2, int chromeGeneratedId, List<Tab> tabs2){
        nativeWindowId=nativeWindowId2 ;
        chromeId= chromeGeneratedId;
        tabs= tabs2;
    }

    public static List<Group> createGroupFromFreeTabsAnswerJSON(String data){
        JsonParser parser = new JsonParser();
        JsonElement parsedData=parser.parse(data) ;
        JsonArray arrayOfWindows=parsedData.getAsJsonArray();
        List<Group> groupToReturn= new ArrayList<>();
        for (JsonElement windowJson : arrayOfWindows) {
            JsonObject window = windowJson.getAsJsonObject();
            String nativeWindowId=window.get("windowId").getAsString();
            int chromeGeneratedId  = window.get("generatedId").getAsInt();
            JsonArray tabsAsJsonArray = window.get("tabs").getAsJsonArray();
            List<Tab> tabs= Tab.createTabsFromJsonArray(tabsAsJsonArray,nativeWindowId);
            groupToReturn.add(new Group(nativeWindowId,chromeGeneratedId,tabs));
        }
        return groupToReturn;
    }

    public void setShelfProperties(Shelf shelf){
        shelfId = shelf.getId();
        name = shelf.getName();
        color = shelf.getColor();
    }
}
