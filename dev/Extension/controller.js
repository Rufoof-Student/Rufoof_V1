export async function getAllTabsAndWindows(chrome) {
  return new Promise((resolve) => {
    chrome.windows.getAll({ populate: true }, (windows) => {
      const tabsInfo = windows.map((window) => ({
        windowId: window.id,
        tabs: window.tabs.map((tab) => ({
          tabId: tab.id,
          title: tab.title,
          url: tab.url,
        })),
      }));
      console.log(tabsInfo);
      resolve(tabsInfo); // Resolve the promise with tabsInfo
    });
  });
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

export async function openAllTabsInthisWindow(chrome, windowsDataJson) {
  console.log(windowsDataJson);
  let window = JSON.parse(windowsDataJson);
  let additionalTabId;
  if (!window.id) additionalTabId = await createNewWindow(window);
  window.id = parseInt("" + window.id);
  console.log(window);
  console.log(window.id);
  console.log(typeof window.id);

  await chrome.windows.get(window.id, async () => {
    if (chrome.runtime.lastError) {
      additionalTabId = await createNewWindow(window);
      console.log("opening new window");
    }
  });

  console.log(window);
  await window.tabs.forEach(async (tab) => {
    const newTab = await new Promise((resolve, reject) => {
      chrome.tabs.create(
        { windowId: parseInt(window.id), url: tab.url },
        (newTab) =>
          chrome.runtime.lastError
            ? reject(chrome.runtime.lastError)
            : resolve(newTab)
      );
    });
    console.log(newTab);
    tab.id = newTab.id;
  });
  console.log(additionalTabId);
  // Close the new tab
  if (additionalTabId) {
    await chrome.tabs.remove([additionalTabId], () => {
      if (chrome.runtime.lastError) {
        console.error("Error closing tab: " + chrome.runtime.lastError.message);
      }
    });
  }

  console.log(window);
  return window;
}
