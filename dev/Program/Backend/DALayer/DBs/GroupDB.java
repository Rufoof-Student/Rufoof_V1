package dev.Program.Backend.DALayer.DBs;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

import dev.Program.DTOs.Group;
import dev.Program.DTOs.GroupPack;
import dev.Program.DTOs.Tab;

public class GroupDB {
    

    @SerializedName("NativeWindowId")
    public String nativeWindowId;

    @SerializedName("ChromeGeneratedId")
    public int generatedChromeId;

    @SerializedName("GroupId")
    public String groupId;

    @SerializedName("Tabs")
    public List<TabDB> tabs ;


    public static GroupDB serialize(Group group){
        GroupDB result = new GroupDB();
        result.nativeWindowId = group.getNativeWindowId();
        result.generatedChromeId  = group.getGeneratedId();
        result.groupId = group.getGroupId();
        List<TabDB> tabs =new ArrayList<>();
        for (Tab tab : group.getTabs()) {
            tabs.add(TabDB.serialize(tab));
        }
        result.tabs=tabs;
        return result;
    }

    public static List<GroupDB> serialize(GroupPack groupPack){
        List<GroupDB> res = new ArrayList<>();
        for (Group group : groupPack.getList()) {
            res.add(serialize(group));
        }
        return res;
    }






}
