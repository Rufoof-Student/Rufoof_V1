package dev.Program.Backend.BusinessLayer.Shelf;

import java.util.List;

import dev.Program.DTOs.Group;
import dev.Program.DTOs.Window;

public class Shelf {
    private String name;
    private int id;
    private String note;
    private List<Group> groups;
    private List<Window> windows;
    private boolean isOpen;
}
