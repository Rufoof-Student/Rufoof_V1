// import { getWindowIdForGeneratedId } from "./IdGenerator";
// import { getWindow } from "./WindowController";

import {
  getGeneratedIdForWindow,
  setGeneratedWindowId,
  getWindowIdForGeneratedId,
} from "./IdGenerator.js";

export async function openAllGroups(chrome, groups) {
  for (let group of groups) {
    console.log(chrome);
    const windowId = await getWindowIdForGeneratedId(group.chromeId, chrome);
    let window = await getWindow(chrome, windowId).then((window) => window);

    if (window === null)
      window = await openWindowWithGeneratedId(chrome, group.chromeId);
    await getWindow(chrome, windowId);
    const groupId = await createTabGroupInWindow(chrome, window.id, group.tabs);
    group.groupId = groupId;
    group.nativeWindowId = window.id;
    await chrome.tabGroups.update(groupId, {
      title: group.name,
      color: group.color,
    });
    console.log(group);
  }
  return groups;
}
async function openWindowWithGeneratedId(chrome, chromeId) {
  const window = new Promise(async (resolve) => {
    await chrome.windows.create(
      {
        url: "https://www.google.com", // The URL to open in the new window
        type: "normal", // The type of window (can be "normal", "popup", "panel", "app", or "devtools")
        state: "maximized", // The state of the window (can be "normal", "minimized", "maximized", or "fullscreen")
      },
      async (newWindow) => {
        await setGeneratedWindowId(newWindow.id + "", chrome, chromeId + "");
        resolve(newWindow);
      }
    );
  });
  return window;
}
async function getWindow(chrome, windowId) {
  return await new Promise(async (resolve, reject) => {
    await chrome.windows.get(
      parseInt(windowId),
      { populate: true },
      (window) => {
        if (chrome.runtime.lastError) {
          resolve(null);
        } else {
          resolve(window);
        }
      }
    );
  });
}

async function createTabGroupInWindow(chrome, windowId, tabs) {
  // Array to hold the tab IDs
  let tabIds = [];

  // Create tabs with the given URLs in the specified window
  for (let tab of tabs) {
    let newTab = await chrome.tabs.create({ windowId: windowId, url: tab.url });
    tab.nativeTabId = newTab.id;
    tabIds.push(newTab.id);
  }

  // Now create a tab group with these tabs
  const groupId = await chrome.tabs.group({ tabIds: tabIds });

  return groupId;
}
// const createNewWindow = async (window) => {
//   let tabIdToDelete;
//   const newWindow = await new Promise(async (resolve, reject) => {
//     await chrome.windows.create(
//       { url: "https://www.google.com", type: "normal" },
//       (createdWindow) => {
//         if (chrome.runtime.lastError) {
//           reject(chrome.runtime.lastError);
//         } else {
//           console.log("getting the id...");
//           tabIdToDelete = createdWindow.tabs[0].id;
//           resolve(createdWindow);
//         }
//       }
//     );
//   });

//   // Set the window ID once the new window is created
//   window.id = newWindow.id;
//   console.log(tabIdToDelete);
//   return tabIdToDelete;
// };

//==================================================================
