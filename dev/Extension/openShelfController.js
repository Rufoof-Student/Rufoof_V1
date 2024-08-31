import { setGeneratedWindowId,getGeneratedIdForWindow,getWindowId2ChromeId } from "./ChromeIdController";
import { getWindow } from "./WindowController";

export async function openAllGroups(chrome, groups) {
  for(const group of groups){
    const windowId = getWindowIdFor(group.chromeId);
    getWindow(chrome,windowId).then(window=>{
      
    }).catch(error=>{

    })
  }
}


const createNewWindow = async (window) => {
  let tabIdToDelete;
  const newWindow = await new Promise(async (resolve, reject) => {
    await chrome.windows.create(
      { url: "https://www.google.com", type: "normal" },
      (createdWindow) => {
        if (chrome.runtime.lastError) {
          reject(chrome.runtime.lastError);
        } else {
          console.log("getting the id...");
          tabIdToDelete = createdWindow.tabs[0].id;
          resolve(createdWindow);
        }
      }
    );
  });

  // Set the window ID once the new window is created
  window.id = newWindow.id;
  console.log(tabIdToDelete);
  return tabIdToDelete;
};

function setWindowGeneratedId(data, chrome, nativeWindowId) {
    chrome.storage.local.set({ [nativeWindowId]: data }, () => console.log('Data ' + data + ' added'));
}

function getWindowGeneratedId(nativeWindowId, chrome) {
    chrome.storage.local.get([nativeWindowId], function(result) {
        if (result && result[nativeWindowId]) {
            console.log('Data retrieved: ', result[nativeWindowId]);
            return (result[nativeWindowId]);  // Pass the data to a callback function
        } else {
            console.log('No data found for window ID: ', nativeWindowId);
            return (null);  // Pass null if no data is found
        }
    });
}


//==================================================================
