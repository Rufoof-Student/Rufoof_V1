package dev.Program.Backend.BusinessLayer.Extractions;

import dev.Program.Backend.BusinessLayer.Process.ProcessObj;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.DTOs.Window;
import java.util.List;

public interface Extraction<T extends Window> {

    

    /**
     * This function returns a Window object with complete data based on the given
     * process ID.
     * This function will return null if cant get the window from process id
     * 
     * @param process the process from which to extract the window data
     * @return a Window object containing full data
     */
    public T extract(String id);

    /**
     * This function closes all windows associated with the given object on the
     * desktop.
     * 
     * If the process ID or window-related data is -1 or null, the windows are
     * considered closed.
     * 
     * @param window the window to close
     * @return a Window object containing data to save; the returned window is
     *         considered closed
     */
    public T close(T window);

    /**
     * This function runs the window and retrieves window-related data such as IDs.
     * 
     * @param window the window to execute
     * @return a Window object containing complete data
     */
    public T execute(T window);

    /**
     * This Function will Return all Instances of the windows from this type that
     * running in the desktop.
     * 
     * @return
     */
    public List<T> getAllInstances();

}
