import { sleep } from "./controller.js";


export async function closeAllGroups(chrome, groupsJSON) {
  console.log(groupsJSON);
  let groups = JSON.parse(groupsJSON);
  let newExtenalTabs = [];
  for (let group of groups) {
    for(let tabRecievedFromServer of group.tabs){
      const nativeTab = await getTab(parseInt(tabRecievedFromServer.nativeTabId));
      if (nativeTab && nativeTab.groupId === chrome.tabGroups.TAB_GROUP_ID_NONE) {
        newExtenalTabs.push(nativeTab.id);
      }
    }
    console.log(group);
    group.groupId = parseInt(group.groupId);
    await getOpenedTabsForThisGroupId(group.groupId).then(
      (tabs) => (group.tabs = tabs)
    );
    await closeGroup(group.groupId);
  }
  await chrome.tabs.remove(newExtenalTabs,()=>console.log("tabs removed"));
  return groups;
}

async function closeGroup(groupId) {
  // Query for all tabs in the specified group
  await chrome.tabs.query({ groupId: groupId }, async function (tabs) {
    if (tabs.length > 0) {
      const tabIds = tabs.map((tab) => tab.id);
      await sleep(750);
      // Remove all tabs in the group, effectively closing the group
      chrome.tabs.remove(tabIds, () => {
        console.log(`Closed group with ID ${groupId}`);
      });
    } else {
      console.log("No tabs found in this group.");
    }
  });
}

async function getOpenedTabsForThisGroupId(groupId) {
  return await new Promise((resolve, reject) => {
    // Use the chrome.tabs.query API to get tabs belonging to the specified groupId
    console.log(parseInt(groupId));
    console.log(groupId);
    chrome.tabs.query({ groupId:parseInt(groupId) }, function (tabs) {
        if (chrome.runtime.lastError) {
            reject(chrome.runtime.lastError);
        } else {
          let toRet = [];
          for (const tab of tabs) {
            toRet.push({
              nativeTabId: tab.id,
              title: tab.title,
              url: tab.url,
              groupId: tab.groupId,
            });
          }
        resolve(toRet);
      }
    });
  });
}


async function moveTabToGroup(tabId) {
  try {
    // Get the current tab
    const tab = await getTab(tabId);
    
    if (tab.groupId === chrome.tabGroups.TAB_GROUP_ID_NONE) {
      // If the tab is not in any group, create a new group or use an existing one
      const groupId = await groupTab(tabId);
      // await updateTabGroup(groupId, "My Group", "blue");
      console.log("Tab moved to group with groupId:", groupId);
    } else {
      console.log("Tab is already in a group");
    }
  } catch (error) {
    console.error("Error moving tab to group:", error);
  }
}

// Helper function to wrap chrome.tabs.get in a Promise
function getTab(tabId) {
  return new Promise((resolve, reject) => {
    tabId?chrome.tabs.get(tabId, (tab) => {
      if (chrome.runtime.lastError) {
        resolve(null);
      } else {
        resolve(tab);
      }
    }):null;
  });
}

// Helper function to group a tab, returns the groupId
function groupTab(tabId) {
  return new Promise((resolve, reject) => {
    chrome.tabs.group({ tabIds: tabId }, (groupId) => {
      if (chrome.runtime.lastError) {
        reject(chrome.runtime.lastError);
      } else {
        resolve(groupId);
      }
    });
  });
}

// Helper function to update a group's title and color
function updateTabGroup(groupId, title, color) {
  return new Promise((resolve, reject) => {
    chrome.tabGroups.update(groupId, { title, color }, (result) => {
      if (chrome.runtime.lastError) {
        reject(chrome.runtime.lastError);
      } else {
        resolve(result);
      }
    });
  });
}
