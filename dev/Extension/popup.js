// Function to be called from the background script
function showMessage(message) {
  alert(message);
}

// Listener to receive messages from the background script
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.action === "showMessage") {
    showMessage(request.message);
    sendResponse({ status: "Message shown" });
  }
});

// // // Optional: Button click to send a message to the background script
// document.getElementById("refreshConnection").addEventListener('click', () => {
//     chrome.runtime.sendMessage({ action: "refreshConnection" });
// });

// Use DOMContentLoaded to ensure the DOM is fully loaded before attaching listeners
document.addEventListener('DOMContentLoaded', () => {
    const myButton = document.getElementById('refreshConnection');
    
    if (myButton) { // Check if myButton is not null
        myButton.addEventListener('click', () => {
            // Send a message to the background script when the button is clicked
            chrome.runtime.sendMessage({ action: "refreshConnection" });
        });
    } else {
        console.error('Button not found!');
    }
});


// Optionally, load the initial state when the popup opens
document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get('toggleState', (data) => {
        document.getElementById('toggleButton').checked = data.toggleState || false;
    });
});

// function hi() {
//   chrome.runtime.sendMessage({ action: "refreshConnection" });
// }
