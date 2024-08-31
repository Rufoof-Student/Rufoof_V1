export async function setGeneratedWindowId(
  nativeWindowId,
  chrome,
  generatedChromeId
) {
  await getAllchromeDataFromStorage("chromeId", chrome).then(
    async (chromeIdDict) => {
      if (chromeIdDict === null) {
        chromeIdDict = {};
      }

      chromeIdDict["" + generatedChromeId] = "" + nativeWindowId;

      await chrome.storage.local.set({ chromeId: chromeIdDict }, () =>
        console.log("data saved")
      );
    }
  );
}

export async function getGeneratedIdForWindow(windowId, chrome) {
  return await getAllchromeDataFromStorage("chromeId", chrome).then(
    async (chromeIdDict) => {
      if (chromeIdDict === null) {
        return -1;
      }
      for (let generatedId of chromeIdDict) {
        if(chromeIdDict[generatedId]===""+windowId) return parseInt(generatedId);
      }
      return -1;
    }
  );
}

export async function getWindowIdForGeneratedId(generatedId, chrome) {
    return await getAllchromeDataFromStorage("chromeId", chrome).then(
      async (chromeIdDict) => {
        if (chromeIdDict === null|| chromeIdDict[generatedId+""]===undefined) {
          return -1+"";
        }
        
        return chromeIdDict[generatedId+""];
      }
    );
  }


async function getAllchromeDataFromStorage(key, chrome) {
  return await new Promise((resolve) => {
    chrome.storage.local.get([key], (res) =>
      chrome.runtime.lastError ? resolve(null) : resolve(res)
    );
  });
}
