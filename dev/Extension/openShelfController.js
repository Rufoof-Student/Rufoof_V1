// import { getWindowIdForGeneratedId } from "./IdGenerator";
// import { getWindow } from "./WindowController";
import { sleep } from "./controller.js";
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
    let defaultGoogleTabToClose = undefined;
    if (window === null)
      await openWindowWithGeneratedId(chrome, group.chromeId).then(
        (windowRes, defaultTabRes) => {
          defaultGoogleTabToClose = windowRes.tabs[0].id;
          window = windowRes;
        }
      );
    console.log(defaultGoogleTabToClose);
    const groupId = await createTabGroupInWindow(chrome, window.id, group.tabs);
    group.groupId = groupId;
    group.nativeWindowId = window.id;
    await chrome.tabGroups.update(groupId, {
      title: group.name,
      color: group.color,
    });
    console.log(group);
    if (defaultGoogleTabToClose)
      await chrome.tabs.remove(
        defaultGoogleTabToClose,
        () => (defaultGoogleTabToClose = undefined)
      );
  }
  return groups;
}
async function openWindowWithGeneratedId(chrome, chromeId) {
  const window = await new Promise(async (resolve) => {
    await chrome.windows.create(
      {
        url: "https://www.google.com", // The URL to open in the new window
        type: "normal", // The type of window (can be "normal", "popup", "panel", "app", or "devtools")
        state: "maximized", // The state of the window (can be "normal", "minimized", "maximized", or "fullscreen")
      },
      async (newWindow) => {
        await setGeneratedWindowId(newWindow.id + "", chrome, chromeId + "");
        console.log(newWindow);
        resolve(newWindow, newWindow.tabs[0].id);
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
    await sleep(750);
  }

  // Now create a tab group with these tabs
  const groupId = await chrome.tabs.group({ tabIds: tabIds });

  return groupId;
}

