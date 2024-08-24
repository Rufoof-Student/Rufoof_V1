package dev.Program.Backend.ServiceLayer;

public class ShelfService {

    /**
     * Retrieves all shelves for the user.
     * 
     * @return JSON string containing all shelves, each with a list of windows, time spent on the shelf, and the note. Each window includes its ID, tabs' URLs if it's a Google Chrome window, and the type of page.
     */
    public String getAllShelves() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the data of a specific shelf.
     * 
     * @param id The ID of the shelf.
     * @return JSON string containing the shelf's data, including a list of windows, each with its ID, tabs' URLs if it's a Google Chrome window, the type of page, the time spent on the shelf, and the note.
     */
    public String getShelfData(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Creates a new shelf.
     * @pre `getAllOpenedWindows()` has been called, and for each ID in `windowToInclude`, the corresponding window is active.
     * @post A new shelf with the specified name and windows has been created.
     * 
     * @param name The name of the new shelf.
     * @param windowToInclude Array of window IDs to include in the new shelf.
     * @return JSON response: if an error occurred, returns the error; otherwise, returns `true`.
     */
    public String createNewShelf(String name, int[] windowToInclude) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Opens the windows of a specific shelf by ID.
     * @pre The shelf is not currently open.
     * @post All windows included in the shelf are opened.
     * 
     * @param id The ID of the shelf.
     * @return JSON response: `true` if the shelf was opened successfully.
     */
    public String openShelf(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes a specific shelf.
     * @pre The shelf with the specified ID exists.
     * @post The shelf has been removed from the database.
     * 
     * @param id The ID of the shelf.
     * @return JSON response: `true` if the shelf was removed successfully.
     */
    public String removeShelf(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Closes the specified shelf.
     * @pre `getAllOpenedWindows()` has been called, and for each ID in `windowsToClose`, the corresponding window is active. The shelf with the specified ID is currently open.
     * @post All windows with IDs included in `windowsToClose` are closed.
     * 
     * @param id The ID of the shelf to close.
     * @param windowsToClose Array of window IDs to close.
     * @return JSON response: `true` if the shelf was closed successfully.
     */
    public String closeShelf(int id, int[] windowsToClose) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Updates the specified shelf.
     * @pre `getAllOpenedWindows()` has been called, and for each ID in `windowsToInclude`, the corresponding window is active. The shelf with the specified ID is currently open.
     * @post The shelf with the specified ID has been updated with the new windows.
     * 
     * @param id The ID of the shelf to update.
     * @param windowsToInclude Array of window IDs to update in the shelf.
     * @return JSON response: `true` if the shelf was updated successfully.
     */
    public String updateShelf(int id, boolean[] savedData) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Pauses the timer for a specific shelf by ID.
     * 
     * @param id The ID of the shelf.
     * @return Response string indicating the result of the operation: `true` if successful.
     */
    public String pauseTimerForShelf(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Resumes the timer for a specific shelf by ID.
     * 
     * @param id The ID of the shelf.
     * @return Response string indicating the result of the operation: `true` if successful.
     */
    public String resumeTimerForShelf(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Restarts the timer for a specific shelf by ID.
     * 
     * @param id The ID of the shelf.
     * @return Response string indicating the result of the operation: `true` if successful.
     */
    public String restartTimerForShelf(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves all currently opened windows on the desktop.
     * 
     * @return JSON response containing a list of windows.
     */
    public String getAllOpenedWindows() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
