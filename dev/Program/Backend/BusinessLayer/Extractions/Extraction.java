package dev.Program.Backend.BusinessLayer.Extractions;

import dev.Program.Backend.BusinessLayer.Process.Process;
import dev.Program.Backend.BusinessLayer.Process.ProcessController;
import dev.Program.DTOs.Window;

public interface Extraction<T extends Window> {

    public static  ProcessController pc =null;

    /**
 * This function returns a Window object with complete data based on the given process ID.
 * 
 * @param process the process from which to extract the window data
 * @return a Window object containing full data
 */
public T extract(Process process);

/**
 * This function closes all windows associated with the given object on the desktop.
 * 
 * If the process ID or window-related data is -1 or null, the windows are considered closed.
 * @param window the window to close
 * @return a Window object containing data to save; the returned window is considered closed
 */
public T close(T window);

/**
 * This function runs the window and retrieves window-related data such as IDs.
 * 
 * @param window the window to execute
 * @return a Window object containing complete data
 */
public T execute(T window);

}
