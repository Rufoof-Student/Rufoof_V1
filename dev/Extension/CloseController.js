export async function closeAllGroups(chrome, groupsJSON) {
  let groups = JSON.parse(groupsJSON);
  let newGroups = [];
  for (let group of groups) {
    group.groupId = parseInt(group.groupId);
    await getTabsForThisGroup(group.groupId).then(
      (tabs) => (group.tabs = tabs)
    );
    closeGroup(group.groupId);
  }
  return groups;
}

async function closeGroup(groupId) {
  // Query for all tabs in the specified group
  await chrome.tabs.query({ groupId: groupId }, function (tabs) {
    if (tabs.length > 0) {
      const tabIds = tabs.map((tab) => tab.id);

      // Remove all tabs in the group, effectively closing the group
      chrome.tabs.remove(tabIds, () => {
        console.log(`Closed group with ID ${groupId}`);
      });
    } else {
      console.log("No tabs found in this group.");
    }
  });
}

async function getTabsForThisGroup(groupId) {
  return await new Promise((resolve, reject) => {
    // Use the chrome.tabs.query API to get tabs belonging to the specified groupId
    chrome.tabs.query({ groupId: groupId }, function (tabs) {
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
