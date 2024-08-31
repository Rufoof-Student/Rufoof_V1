export async function getWindow(chrome,windowId){
    return new Promise((resolve, reject) => {
        // Fetch the window information using the provided windowId
        chrome.windows.get(windowId, { populate: true }, (window) => {
            if (chrome.runtime.lastError) {
                // Handle errors, such as invalid window ID
                reject(chrome.runtime.lastError);
            } else {
                resolve(window);
            }
        });
    });
}