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
    private String color;
    private String name;

    private String groupId;

    private Group(String nativeWindowId2, int chromeGeneratedId, List<Tab> tabs2) {
        nativeWindowId = nativeWindowId2;
        chromeId = chromeGeneratedId;
        tabs = tabs2;
    }

    private static JsonArray parseArrayData(String data) {
        JsonParser parser = new JsonParser();
        JsonElement parsedData = parser.parse(data);
        return parsedData.getAsJsonArray();
    }

    public static List<Group> createGroupFromFreeTabsAnswerJSON(String data) {
        JsonArray arrayOfWindows = Group.parseArrayData(data);
        List<Group> groupToReturn = new ArrayList<>();
        for (JsonElement windowJson : arrayOfWindows) {
            JsonObject window = windowJson.getAsJsonObject();
            String nativeWindowId = window.get("nativeWindowId").getAsString();
            int chromeGeneratedId = window.get("chromeId").getAsInt();
            JsonArray tabsAsJsonArray = window.get("tabs").getAsJsonArray();
            List<Tab> tabs = Tab.createTabsFromJsonArray(tabsAsJsonArray, nativeWindowId);
            groupToReturn.add(new Group(nativeWindowId, chromeGeneratedId, tabs));
        }
        return groupToReturn;
    }

    /**
     * this function will update the native ids for groups (include tabs)
     * INSURE :
     * 
     * @param data JSON format is include this - { tabs : [ nativeTabId : string |
     *             int , ...] , nativeWindowId : String | int , groupId : String |
     *             int ,... }
     */
    public static List<Group> getAllGroupsAsOpened(String data,Shelf shelfForGroups) {
        JsonArray arrayOfWindows = Group.parseArrayData(data);
        List<Group> groupToReturn = new ArrayList<>();
        for (JsonElement windowJson : arrayOfWindows) {
            JsonObject window = windowJson.getAsJsonObject();
            String nativeWindowId = window.get("nativeWindowId").getAsString();
            int chromeGeneratedId = window.get("chromeId").getAsInt();
            JsonArray tabsAsJsonArray = window.get("tabs").getAsJsonArray();
            List<Tab> tabs = Tab.createTabsFromJsonArray(tabsAsJsonArray, nativeWindowId);
            Group g = (new Group(nativeWindowId, chromeGeneratedId, tabs));
            g.setShelfProperties(shelfForGroups);
            g.setGroupId(window.get("groupId").getAsString());
            groupToReturn.add(g);
        }
        return groupToReturn;
    }

    public void markAsClosed() {
        groupId = null;
        nativeWindowId = null;
        for (Tab tab : tabs) {
            tab.markAsClosed();
        }
    }

    public void setShelfProperties(Shelf shelf) {
        shelfId = shelf.getId();
        name = shelf.getName();
        color = shelf.getColor();
    }

    public int getGeneratedId() {
        return chromeId;
    }

    public void setGeneratedId(int newId) {
        chromeId = newId;
    }

    public String getNativeWindowId() {
        return this.nativeWindowId;
    }

    public void setGroupId(String asString) {
        groupId = asString;
    }

    // TODO clean it
    public void addTabs(List<Tab> tabsFromJsonArray) {
        for (Tab tab : tabsFromJsonArray) {
            if (!tabs.contains(tab)) {
                tabs.add(tab);
            }
        }
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void removeTab(String idToDel) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getNativeTabId().equals(idToDel))
                tabs.remove(i);
        }
    }

    public boolean hasDiffTabs(Group group) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasDiffTabs'");
    }

    public void union(Group group) {
        this.addTabs(group.getTabs());
    }

    public void setTabs(List<Tab> tabsToKeep) {
        tabs=tabsToKeep;
    }

}
