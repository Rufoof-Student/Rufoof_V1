import { createGroups, getAllTabsAndWindows } from "./controller.js";
import { openAllGroups } from "./openShelfController.js";
import { closeAllGroups } from "./CloseController.js";


let socket = new WebSocket("ws://localhost:8887"); // Use your server's IP if testing from another PC

socket.onopen = function () {
  console.log("Connected to WebSocket server");
  socket.send("Hello from the browser console!");
  // print5ra();
};
let urlSended = false;
socket.onmessage = async function (event) {
  console.log("Message from server: " + event.data);
  const msg = JSON.parse(event.data);
  if (msg.tag === "Question") {
    if (msg.type === "getWindows") {
      let toSend = "";
      (async () => {
        const windowsToSend = await getAllTabsAndWindows(chrome);
        const dataJson = JSON.stringify(windowsToSend);
        const toSend = {
          tag: "Answer",
          type: "Windows",
          data: dataJson,
        };
        console.log(toSend);
        const packetToSend = JSON.stringify(toSend);
        console.log(packetToSend);
        socket.send(packetToSend);
      })();
    } else if (msg.type === "runGroups") {
      const windowToSendBack = JSON.stringify(
        await openAllGroups(chrome, msg.data)
      );
      console.log(windowToSendBack);
      const packetToSend = {
        tag: "Answer",
        type: "running",
        data: windowToSendBack,
      };
      socket.send(JSON.stringify(packetToSend));
    } else if (msg.type === "close") {
      //TODO
    } else if (msg.type === "createNewGroups") {
      const dataToSend = await createGroups(msg.data, chrome);
      const dataJson = JSON.stringify(dataToSend);
      const packetToSend = {
        tag: "Answer",
        type: "done",
        data: dataJson,
      };
      socket.send(JSON.stringify(packetToSend));
    }else if(msg.type==="closeAllGroups"){
      const newgroubs = await closeAllGroups(chrome,msg.data);
      const dataToSend = JSON.stringify(newgroubs);
      const packetToSend = {
        tag: "Answer",
        type: "closed",
        data: dataToSend,
      };
      console.log(dataToSend)
      socket.send(JSON.stringify(packetToSend));
    }
  }
};

socket.onclose = function (event) {
  console.log("WebSocket closed: " + event.code);
};

socket.onerror = function (error) {
  console.error("WebSocket error: " + error.message);
};

console.log("hiiiii");

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
      }
    });
  }
});
// Listener for browser startup
chrome.runtime.onStartup.addListener(() => {
  console.log("Browser started");
});

async function test(windowId) {
  await chrome.windows.update(windowId, { focused: true });
  return await chrome.windows.getAll({ windowTypes: ["normal"] });
}
