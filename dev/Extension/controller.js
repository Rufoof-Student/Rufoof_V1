export async function getAllTabsAndWindows(chrome) {
  return new Promise((resolve) => {
    chrome.windows.getAll({ populate: true }, (windows) => {
      const tabsInfo = windows.map((window) => ({
        windowId: window.id,
        generatedId: getGeneratedIdForWindow(window),
        tabs: getAllFreeTabsDataInWindow(window)
      }));
      console.log(tabsInfo);
      resolve(tabsInfo); // Resolve the promise with tabsInfo
    });
  });
}

function getGeneratedIdForWindow(window) {
  return -1;
}

function getAllFreeTabsDataInWindow(window){
  return window.tabs.map((tab) => ({
    id: tab.id,
    title: tab.title,
    url: tab.url,
    groupId: tab.groupId,
  })).filter((tab) => tab.groupId === -1);
}