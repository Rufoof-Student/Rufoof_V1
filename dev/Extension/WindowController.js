export async function getWindow(chrome, windowId) {
    // return await new Promise(async (resolve, reject) => {
    //     await chrome.windows.get(windowId, { populate: true }, (window) => {
    //         if (chrome.runtime.lastError) {
    //             reject(chrome.runtime.lastError);
    //         } else {
    //             resolve(window);
    //         }
    //     });
    // });
    console.log('d');
}




export  async function createTabGroupInWindow(chrome,windowId, tabs) {
    // Array to hold the tab IDs
    let tabIds = [];

    // Create tabs with the given URLs in the specified window
    for (let tab of tabs) {
        let newTab = await chrome.tabs.create({ windowId: windowId, url: tab.url });
        tab.nativeTabId=newTab.id;
        tabIds.push(newTab.id);
    }

    // Now create a tab group with these tabs
    const groupId = await chrome.tabs.group({ tabIds: tabIds });

    return groupId;
}