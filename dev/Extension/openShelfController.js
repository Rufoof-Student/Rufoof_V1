export async function openAllTabsInthisWindow(chrome, windowsDataJson) {
  console.log(windowsDataJson);
  let window = JSON.parse(windowsDataJson);
  let additionalTabId;
  if (!window.id) additionalTabId = await createNewWindow(window);
  window.id = parseInt("" + window.id);
  console.log(window);
  console.log(window.id);
  console.log(typeof window.id);

  additionalTabId = await new Promise(async (resolve, reject) => {
    await chrome.windows.get(window.id, async () => {
      if (chrome.runtime.lastError) {
        console.log("we joined the creating window");
        additionalTabId = await createNewWindow(window);
        console.log("opening new window");
        resolve(additionalTabId);
      } else {
        resolve("");
      }
    });
  });

  console.log(window);
  await new Promise(async (resolve, reject) => {
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
      console.log("getting the id of the tab");
      console.log(newTab);
      tab.id = newTab.id;
      resolve();
    });
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
