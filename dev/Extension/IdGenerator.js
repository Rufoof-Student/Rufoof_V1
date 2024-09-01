export async function sss(){
    return -1;
}


async function getAllchromeDataFromStorage(key, chrome) {
  return await new Promise(async (resolve) => {
    await chrome.storage.local.get([key], (res) =>
      chrome.runtime.lastError ? resolve(null) : resolve(res)
    );
  });
}


export async function getWindowIdForGeneratedId(generatedId, chrome) {
  return await getAllchromeDataFromStorage("chromeId", chrome).then(
    async (chromeIdDict) => {
      if (
        chromeIdDict === null ||
        chromeIdDict[generatedId + ""] === undefined
      ) {
        return -1 + "";
      }

      return chromeIdDict[generatedId + ""];
    }
  );
}


export async function getGeneratedIdForWindow(windowId, chrome) {
  const res = await getAllchromeDataFromStorage("chromeId", chrome).then(
    async (chromeIdDict) => {
      //   return await new Promise(async (resolve) => {
      if (chromeIdDict === null) {
        return -1;
      }
      for (let generatedId of chromeIdDict) {
        if (chromeIdDict[generatedId] === "" + windowId)
          return parseInt(generatedId);
      }
      return -1;
      //   });
    }
  );
  return res;
}


async function getNewGeneratedChromeId(chrome) {
  return await new Promise(async (resolve) => {
    await chrome.storage.local.get(["idGenerator"], async (res) => {
      if (chrome.runtime.lastError) {
        await chrome.storage.local.set({ idGenerator: 1 }, () =>
          console.log("generator has been declared")
        );
        resolve(0);
      } else {
        await chrome.storage.local.set({ idGenerator: res + 1 }, () =>
          console.log("generator has been increased")
        );
        resolve(res);
      }
    });
  });
}


export async function setGeneratedWindowId(
  nativeWindowId,
  chrome,
  generatedChromeId
) {
  return await new Promise(async (resolve) => {
    await getAllchromeDataFromStorage("chromeId", chrome).then(
      async (chromeIdDict) => {
        if (chromeIdDict === null) {
          chromeIdDict = {};
        }
        // if (generatedChromeId === -1)
        //   await getNewGeneratedChromeId(chrome).then(
        //     (id) => (generatedChromeId = id)
        //   );
        chromeIdDict["" + generatedChromeId] = "" + nativeWindowId;

        await chrome.storage.local.set({ chromeId: chromeIdDict }, () =>
          console.log("data saved")
        );

        resolve(generatedChromeId);
      }
    );
  });
}
