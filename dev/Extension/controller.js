import { getGeneratedIdForWindow,setGeneratedWindowId } from "./IdGenerator.js";

export async function getAllTabsAndWindows(chrome) {
  return new Promise(async (resolve) => {
    chrome.windows.getAll({ populate: true }, (windows) => {
      const tabsInfo = windows.map(async (window) => ({
        windowId: window.id,
        generatedId: await getGeneratedIdForWindow(window.id, chrome).then(
          (res) => res
        ),
        tabs: getAllFreeTabsDataInWindow(window),
      }));
      console.log(tabsInfo);
      resolve(tabsInfo); // Resolve the promise with tabsInfo
    });
  });
}

function getAllFreeTabsDataInWindow(window) {
  return window.tabs
    .map((tab) => ({
      nativeTabId: tab.id,
      title: tab.title,
      url: tab.url,
      groupId: tab.groupId,
    }))
    .filter((tab) => tab.groupId === -1);
}

export async function createGroups(data, chrome) {
  const groups = JSON.parse(data);

  let returendGroups = [];

  // Loop through each group sequentially
  for (const group of groups) {
    if (
      (await getGeneratedIdForWindow(group.nativeWindowId, chrome).then(
        (res) => res
      )) === -1
    ) {
      await setGeneratedWindowId(
        group.nativeWindowId,
        chrome,
        group.chromeId
      ).then((chromeId) => (group.chromeId = chromeId));
    }

    // Only attempt to create a group if there are tabs to add
    if (group.tabs.length > 0) {
      const toAdd = await openNewGroup(group, chrome).then((res) => res);
      returendGroups.push(toAdd);
      console.log("Added group to returendGroups");
    }
  }

  // Check the length of returendGroups
  console.log("Number of returned groups:", returendGroups.length);
  return returendGroups;
}

export async function openNewGroup(group, chrome) {
  console.log(group);
  const ids = group.tabs.map((tab) => parseInt(tab.nativeTabId));
  console.log(ids + "");

  new Promise(async (resolve) => {
    await chrome.windows.update(parseInt(group.nativeWindowId), {
      focused: true,
    });
    console.log("window is focused");
    resolve();
  });

  const groupId = await new Promise((resolve) => {
    chrome.tabs.group({ tabIds: ids }, (groupId) => resolve(groupId));
  });
  console.log(groupId);
  return await new Promise(async (resolve) => {
    await chrome.tabGroups.update(
      groupId,
      {
        title: group.name,
        color: group.color,
      },
      async () => console.log("updated")
    );
    resolve({ groupId: groupId, windowId: group.nativeWindowId });
  });
}
