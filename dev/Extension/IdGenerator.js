export async function sss() {
  return -1;
}

async function getAllchromeDataFromStorage(key, chrome) {
    console.log(chrome);
  return await new Promise(async (resolve) => {
    await chrome.storage.local.get([key], (res) =>
      chrome.runtime.lastError ? resolve(null) : resolve(res[key])
    );
  });
}

export async function getWindowIdForGeneratedId(generatedId, chrome) {
    const chromeIdDict = await getAllchromeDataFromStorage("chromeId", chrome);
  
    if (chromeIdDict === null) {
      return "-1"; // Return "-1" as a string if chromeIdDict is null
    }
  
    for (const pair of chromeIdDict) {
      if (pair.genId+"" === generatedId + "") {
        console.log("found match genId");
        return pair.windowId + "";
      }
    }
  
    return "-1"; // Return "-1" as a string if no match is found
  }

export async function getGeneratedIdForWindow(windowId, chrome) {
  const res = await getAllchromeDataFromStorage("chromeId", chrome).then(
    async (chromeIdDict) => {
      console.log("joining the get genereated id");
      //   return await new Promise(async (resolve) => {
      if (chromeIdDict === null) {
        return -1;
      }
      console.log(chromeIdDict);
      for (let pair of (chromeIdDict)) {
        if (windowId + "" ===  pair.windowId+"") {
          console.log("found match window id : " + windowId+' , '+pair.genId);
          return parseInt(pair.genId+"");
        }
      }
      console.log("new genId must be returned");
      const id =await getNewGeneratedChromeId(chrome).then(
        (id) => (id)
      );
      console.log(id);
      await setGeneratedWindowId(windowId,chrome,id);
      return id;
      //   });
    }
  );
  console.log(res);
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
        console.log(res);
        await chrome.storage.local.set({ idGenerator: res["idGenerator"] + 1 }, () =>
          console.log("generator has been increased")
        );
        resolve(res["idGenerator"]);
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
    getAllchromeDataFromStorage("chromeId", chrome).then(
      async (chromeIdDict) => {
        if (chromeIdDict === null||chromeIdDict===undefined) {
          chromeIdDict = [];
        }
        if (generatedChromeId === -1)
          getNewGeneratedChromeId(chrome).then(
            (id) => (generatedChromeId = id)
          );
          console.log(chromeIdDict);
          console.log(typeof chromeIdDict)
          console.log(generatedChromeId);
        chromeIdDict.push({
          genId: generatedChromeId,
          windowId: nativeWindowId,
        });

        await chrome.storage.local.set({ chromeId: chromeIdDict }, () =>
          console.log("data saved")
        );

        resolve(generatedChromeId);
      }
    );
  });
}
