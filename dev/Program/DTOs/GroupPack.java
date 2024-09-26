package dev.Program.DTOs;

import java.util.List;
import java.util.ArrayList;

public class GroupPack {
    private List<Group> groups ; 

    public GroupPack(List<Group> groups){
        this.groups=groups;
    }

    public GroupPack(){
        groups=new ArrayList<>();
    }

    public void getDiffGroups(GroupPack groupPack2){
        for (int i = groupPack2.size() - 1; i >= 0; i--) {
            Group group2 = groupPack2.get(i);
            for (int j = 0; j < groups.size(); j++) {
                Group group = groups.get(j);
                if (group.getNativeWindowId().equals(group2.getNativeWindowId())) {
                    this.remove(i);
                }
            }
        }
    }

    public void remove(int i) {
        groups.remove(i);
    }

    public Group get(int i) {
        return groups.get(i);
    }

    public int size() {
       return groups.size();
    }

    public List<Group> getList() {
        return groups;
    }


    public void setGroups(List<Group> groups){
        this.groups=groups;
    }

    public void markAsClosed() {
        for (Group group : groups) {
            group.markAsClosed();
        }
    }

    public Group contains(Group groupToCheck) {
        for (Group group : groups) {
            if(groupToCheck.getNativeWindowId().equals(group.getNativeWindowId())) return group;
        }
        return null;
    }

    /**
     * WARNING : each group in new groups is not exist in the pack. 
     * @param newGroups
     */
    public void addForiegnGroups(GroupPack newGroups) {
        groups.addAll(newGroups.getList());
    }
}
