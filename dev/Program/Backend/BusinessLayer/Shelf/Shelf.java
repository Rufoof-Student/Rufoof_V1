package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.List;
import java.util.ArrayList;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Group;
import dev.Program.DTOs.GroupPack;
import dev.Program.DTOs.Window;

public class Shelf {
    private String name;
    private int id;
    private Colors color;
    private String note;
    private GroupPack groups;
    private List<Window> windows;
    private boolean isOpen;

    public Shelf(String name,int id,Colors color){
        this.id = id;
        this.name = name;
        this.color= color;
        // groups = new GroupPack();
    }

    public String toString(){
        return name+" with id "+id+" and color "+color.getColorName();
    }
    
    public int getId() {
        return id;
    }
    public String getName() {
       return name;
    }
    public String getColor() {
        return color.getColorName();
    }

    public void setGroups(GroupPack groups2) {
        groups=groups2;
    }

    public void initGroupPack(GroupPack g){
        if(groups==null){
            groups=g;
        }
    }

    public GroupPack getDiffGroups(GroupPack groups2) {
        for (int i = groups2.size() - 1; i >= 0; i--) {
            Group group2 = groups2.get(i);
            for (int j = 0; j < groups.size(); j++) {
                Group group = groups.get(j);
                if (group.getNativeWindowId().equals(group2.getNativeWindowId())) {
                    groups2.remove(i);
                }
            }
        }
        return groups2;
    }

    public void addForiegnGroups(GroupPack newGroups) {
        groups.addForiegnGroups(newGroups);
    }

    public GroupPack getGroups() {
        return groups;
    }

    public void markAsClosed() {
        groups.markAsClosed();
    }

   

    public Group hasGroup(Group group) {
        return groups.contains(group);
    }
}
