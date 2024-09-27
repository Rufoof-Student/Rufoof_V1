


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

export async function getDataForTab(chrome,tabDataJson){
    const tabData = JSON.parse(tabDataJson);
    console.log("we have to return data for "+tabData.nativeTabId);
    const tabId = parseInt(tabData.nativeTabId);
    return await new Promise(async resolve=>{
        await chrome.tabs.get(tabId,tab=>{
            if (chrome.runtime.lastError) {
                resolve({url:null})
            } else{
                resolve({url:tab.url});  
            } 
        })
    })

}