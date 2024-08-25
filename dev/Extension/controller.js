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
    const newWindow = await new Promise((resolve, reject) => {
      chrome.windows.create({ url: "https://www.google.com", type: "normal" }, (createdWindow) => {
        if (chrome.runtime.lastError) {
          reject(chrome.runtime.lastError);
        } else {
          resolve(createdWindow);
        }
      });
    });
  
    // Set the window ID once the new window is created
    window.id = newWindow.id;
  };

export async function openAllTabsInthisWindow(chrome, windowsDataJson) {
  let window = JSON.parse(windowsDataJson)[0];
  let recievedUrls = window.tabs;
  if (!window.id) await createNewWindow(window);
  console.log(window)
  console.log(window.id);
  console.log(typeof window.id);
  chrome.windows.get(parseInt(window.id), (window) => {
    if (chrome.runtime.lastError) {
      createNewWindow(window);
      console.log("opening new window");
    }
  });

  console.log(window);
  await window.tabs.forEach(async (tab) => {
    await chrome.tabs.create({ windowId: window.id, url: tab.url });
  });
}
