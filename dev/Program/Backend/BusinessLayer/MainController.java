package dev.Program.Backend.BusinessLayer;

import java.util.List;

import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.Backend.BusinessLayer.Shelf.ShelfController;
import dev.Program.Backend.BusinessLayer.Window.WindowController;
import dev.Program.DTOs.Colors;
import dev.Program.DTOs.Tab;
import dev.Program.DTOs.Window;

public class MainController {
    private ProcessController processController;
    private ShelfController shelfcController;
    private WindowController windowController;

    public MainController(){
        shelfcController = new ShelfController();
        windowController = new WindowController();
        processController = new ProcessController();
    }

    public Shelf createNewShelf(String name,Colors color,List<Window> windowsToInclude,List<Tab> tabsToInclude){
        Shelf toCreate = shelfcController.getNewShelf(name,color);
        toCreate = windowController.addGroupsToShelf(toCreate,tabsToInclude);
        //=====================
        //TODO set the windows to the new shelf.

        return toCreate;
    }

}
