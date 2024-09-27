import { createGroups, getAllTabsAndWindows } from "./controller.js";
 import { openAllGroups } from "./openShelfController.js";
import { closeAllGroups } from "./CloseController.js";

//=====================================================


let minutes = 0;
let seconds = 0;
let toStop = false;
const startTheTimer=()=>{
  const count=()=>{
    seconds++;
    if(seconds>=60) {
      seconds=0;
      minutes++;
    }
    if(!toStop) setTimeout(count,1000);
    console.log(minutes+":"+seconds);
  }
  count();
}
function stopAndPrintTimer(){
  toStop=true;
  console.log(minutes+" before disconnect");
}
startTheTimer();

export function hi(){
  console.log('hi');
}


//=====================================================

const connectToServer = ()=>{
  try{
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
          sendDataBackToSocket(await getAllTabsAndWindows(chrome), "Windows");
        } else if (msg.type === "close") {
          //TODO
        } else if (msg.type === "createNewGroups") {
          createGroups(msg.data, chrome).then((res) =>
            sendDataBackToSocket(res, "done")
          );
        } else if (msg.type === "closeAllGroups") {
          closeAllGroups(chrome, msg.data).then((res) =>
            sendDataBackToSocket(res, "closed")
          );
        } else if(msg.type==="runGroubs"){
          openAllGroups(chrome,JSON.parse(msg.data)).then(res=>sendDataBackToSocket(res,"running"));
        }
      }
    };
    
    function sendDataBackToSocket(data, type) {
      console.log(data);
      const dataToSend = JSON.stringify(data);
      const packetToSend = {
        tag: "Answer",
        type: type,
        data: dataToSend,
      };
      console.log(dataToSend);
      socket.send(JSON.stringify(packetToSend));
    }
    
    socket.onclose = function (event) {
      console.log("WebSocket closed: " + event.code);
      stopAndPrintTimer();
    };
    
    socket.onerror = function (error) {
      console.error("WebSocket error: " + error.message);
    };

  }catch(e){
    console.log(e);
  }
}
connectToServer();


console.log("hiiiii");

chrome.tabs.onRemoved.addListener((tabId, removeInfo) => {
  console.log(`Tab with ID ${tabId} was closed`);
  // Perform actions related to the tab closing
});
// Listener for messages from other parts of the extension
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  console.log("Message received:", message);
  if(message.action==="refreshConnection"){
    console.log("connecting to server ...")
    connectToServer();
  }
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
        connectToServer()
      }
    });
  }
});
// Listener for browser startup
chrome.runtime.onStartup.addListener(() => {
  connectToServer();
});
