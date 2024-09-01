 import { getWindowIdForGeneratedId } from "./IdGenerator";
import { getWindow,openTabsOnWindow } from "./WindowController";


export async function openAllGroups(chrome, groups) {
  for(const group of groups){
    const windowId = getWindowIdForGeneratedId(group.chromeId).then(res=>res).catch(error=>error);
    getWindow(chrome,windowId).then(window=>{
      openTabsOnWindow(chrome,windowId,group.tabs).then(tabs=>{
        group.tabs = tabs;

      })
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



//==================================================================
