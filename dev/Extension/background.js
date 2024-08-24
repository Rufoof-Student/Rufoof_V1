async function getAllTabsAndWindows() {
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


let socket = new WebSocket("ws://localhost:8887"); // Use your server's IP if testing from another PC

socket.onopen = function () {
  console.log("Connected to WebSocket server");
  socket.send("Hello from the browser console!");
};
let urlSended = false;
socket.onmessage = function (event) {
  console.log("Message from server: " + event.data);
  if (!urlSended) {
    urlSended = true;
    let toSend = "";
    (async () => {
      const windows1 = await getAllTabsAndWindows();

      if (windows1) {
        windows1.forEach((element) => {
          element.tabs.forEach((tab) => {
            toSend += tab.url + "\n";
          });
        });
        socket.send("" + toSend);
      } else {
        console.log("windows is not array " + typeof windows1);
        console.log(windows1);
      }
    })();
  }
  if (event.data === "giveMeUrls") {
    let toSend = "";
    (async () => {
      const windows1 = await getAllTabsAndWindows();

      if (windows1) {
        windows1.forEach((element) => {
          element.tabs.forEach((tab) => {
            toSend += tab.url + "\n";
          });
        });
        socket.send("" + toSend);
      } else {
        console.log("windows is not array " + typeof windows1);
        console.log(windows1);
      }
    })();
  }
};

socket.onclose = function (event) {
  console.log("WebSocket closed: " + event.code);
};

socket.onerror = function (error) {
  console.error("WebSocket error: " + error.message);
};
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  console.log("hii");
  if (request.action === "getTabsAndWindows") {
    chrome.tabs.query({}, (tabs) => {
      let urls = tabs.map((tab) => tab.url);
      sendResponse({ urls: urls });
    });
    return true; // Indicates asynchronous response
  }
});
console.log("hiiiii");
function hi() {
  console.log("hiiiii");
  chrome.tabs.query({}, (tabs) => {
    let urls = tabs.map((tab) => tab.url);
    console.log(urls);
  });
}

chrome.tabs.onRemoved.addListener((tabId, removeInfo) => {
  console.log(`Tab with ID ${tabId} was closed`);
  // Perform actions related to the tab closing
});
// Listener for messages from other parts of the extension
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  console.log("Message received:", message);
  sendResponse({ response: "Message received" });
});

// Listener for extension installation
chrome.runtime.onInstalled.addListener(async (details) => {
  if (details.reason === "install") {
    console.log("Extension installed");
    await chrome.alarms.create("myAlarm", { periodInMinutes: 1 });
    await chrome.alarms.onAlarm.addListener((alarm) => {
      if (alarm.name === "myAlarm") {
        console.log("Alarm triggered");
        // Perform periodic tasks here
      }
    });
  }
});
// Listener for browser startup
chrome.runtime.onStartup.addListener(() => {
  console.log("Browser started");
});

// Example: Set up an alarm to trigger a task periodically
