
package dev.Program.Backend.BusinessLayer.Server;

import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.Program.DTOs.*;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExtensionSocketServer extends WebSocketServer {

    private List<ChromeWindow> googleWindows;
    private Thread workerThread;
    private List<WebSocket> conns;
    private Gson gson = new Gson();
    private JsonParser jsonParser = new JsonParser();
    private Object lock = new Object();
    // private boolean isRecollection = false;
    // private boolean isOpenProccess;

    // =====
    private ConcurrentLinkedQueue<Answer> responses = new ConcurrentLinkedQueue<>();

    public ExtensionSocketServer(int port) {
        super(new InetSocketAddress(port));
        workerThread = new Thread();
        googleWindows = new ArrayList<>();
        conns = new ArrayList<>();
    }

    /**
     * Get the opened google windows ,without refreshing.
     * 
     * @return List<CromeWindow> for the opened windows.
     */
    public List<ChromeWindow> getLastUpdateGoogleOpenedWindows() {
        synchronized (lock) {
            return googleWindows;
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        synchronized(lock){
            conns.remove(conn);
            lock.notifyAll();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("recieved msg:" + message);

        try {

            Map<String, Object> msgMap = gson.fromJson(message, Map.class);
            if (msgMap.get("tag").equals("Question")) {
                Question question = new Question((String) msgMap.get("type"), (String) msgMap.get("data"));
                dealWithQuestion(question);
            } else if (msgMap.get("tag").equals("Answer")) {
                Answer answer = new Answer((String) msgMap.get("type"), (String) msgMap.get("data"));
                synchronized (lock) {
                    responses.add(answer);
                    lock.notifyAll();
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void refillWindowsFromJSON(String JSONdata) {
        // synchronized (lock) {
        googleWindows = new ArrayList<>();
        JsonArray data = jsonParser.parse(JSONdata).getAsJsonArray();
        for (JsonElement windowJsonElement : data) {
            JsonObject windowJsonObject = windowJsonElement.getAsJsonObject();
            googleWindows.add(new ChromeWindow(convertFromJsonToWindow(windowJsonObject),true));
        }

    }

    private void dealWithQuestion(Question question) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dealWithQuestion'");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println(ex.getMessage());
    }

    @Override
    public void onStart() {

    }

    /**
     * This functioin will ask the extension/client to send the opened google
     * windows . and wait for response .
     * 
     * @pre There is connection between the sever and the client.
     * 
     * @return the updated google opened windows. null if there is no connection. or
     *         something went wrong.
     */
    public List<ChromeWindow> getCurrentGoogleOpenedWindows() {
        if (conns.size() == 0) {
            return null;
        }
        WebSocket conn = conns.get(0);
        try {
            Question question = new Question("getWindows");
            String toSend = gson.toJson(question);
            synchronized (lock) {

                conn.send(toSend);
                while (responses.size() == 0)
                    lock.wait();
                Answer res = responses.remove();
                if (!res.type.equals("Windows"))
                    return null;
                refillWindowsFromJSON(res.data);
            }
            return googleWindows;

        } catch (Exception ex) {
            System.out.println("An error occured with sinding -getWindows- to the client:");
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ExtensionSocketServer s = new ExtensionSocketServer(8887);
        s.start();
        Scanner sc = new Scanner(System.in);
        while (true) {

            switch (sc.nextLine()) {
                case "1":

                    List<ChromeWindow> a = s.getCurrentGoogleOpenedWindows();

                    for (ChromeWindow chromeWindow : a) {

                        System.out.println(chromeWindow);
                    }
                    break;
                case "2":
                    List<Tab> tabs = new ArrayList<>();
                    tabs.add(new Tab("https://mail.google.com/mail/u/0/#inbox",
                            "Inbox (1,201) - bhaa.hillou@gmail.com - Gmail"));
                    ChromeWindow windows;
                    windows = new ChromeWindow(sc.nextLine(), tabs, false);

                    ChromeWindowToSendSocket b = s.openWindows(windows);

                    // for (ChromeWindow chromeWindow : b) {

                    // System.out.println(chromeWindow);
                    // }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Open the given windows on the desktop. current thread will wait untill
     * process finished.
     * 
     * @param windowsToOpen is the list of windows that will open.
     * @return the list of new opened tabs.
     */
    public ChromeWindowToSendSocket openWindows(ChromeWindow windowToOpen) {
        if (conns.size() == 0) {
            return null;
        }
        WebSocket conn = conns.get(0);
        String jsonData = getWindowsToSendAsJson(windowToOpen);
        Question question = new Question("openWindows", jsonData);
        synchronized (lock) {
            // isOpenProccess = true;
            conn.send(gson.toJson(question));
            try {
                while (responses.size() == 0)
                    lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Answer res = responses.remove();
            if (!res.type.equals("OpenProcessIsFinished"))
                return null;
            System.out.println(res.data);
            return getChromeFromJson(res.data);
            // return null;
        }
        // googleWindows = getCurrentGoogleOpenedWindows();
    }

    private ChromeWindowToSendSocket getChromeFromJson(String data) {

        JsonObject windowJsonObject = jsonParser.parse(data).getAsJsonObject();
        return convertFromJsonToWindow(windowJsonObject);
    }

    private ChromeWindowToSendSocket convertFromJsonToWindow(JsonObject windowJsonObject) {
        String id = windowJsonObject.get("id").getAsString();
        JsonArray tabsJsonArray = windowJsonObject.get("tabs").getAsJsonArray();
        TabToSendSocket[] tabs = new TabToSendSocket[tabsJsonArray.size()];
        int counter =0;
        for (JsonElement tabJsonElement : tabsJsonArray) {
            JsonObject tabJsonObject = tabJsonElement.getAsJsonObject();
            TabToSendSocket tabToAdd = new TabToSendSocket(tabJsonObject.get("url").getAsString(),
                    tabJsonObject.get("title").getAsString(), tabJsonObject.get("id").getAsString());
            tabs[counter]=(tabToAdd);
            counter++;
        }
        return new ChromeWindowToSendSocket(id, tabs);
    }

    private String getWindowsToSendAsJson(ChromeWindow windowToOpen) {

        List<Tab> tabs = windowToOpen.getTabs();
        TabToSendSocket[] tabsToSend = new TabToSendSocket[tabs.size()];
        for (int j = 0; j < tabsToSend.length; j++) {
            tabsToSend[j] = new TabToSendSocket(tabs.get(j).getUrl(), tabs.get(j).getTitle(), tabs.get(j).getId());
        }
        ChromeWindowToSendSocket windowsToSend = new ChromeWindowToSendSocket(windowToOpen.getId(), tabsToSend);

        return gson.toJson(windowsToSend);
    }

    public boolean connectionIsOpenedWithGoogle() {
        return conns.size()!=0;
    }

    public ChromeWindow closeAllTabs(ChromeWindow window){

        List<Tab> tabsToClose = window.getTabs();
        String[] ids = new String[tabsToClose.size()];
        for(int i=0;i<ids.length;i++){
            ids[i] = tabsToClose.get(i).getId();
        }
        String dataToSend = gson.toJson(ids);
        Question qes = new Question("Close", dataToSend);
        String qesToSend = gson.toJson(qes);
        synchronized(lock){
            if(conns.size()==0) return null;
            WebSocket con =conns.get(0);
            con.send(qesToSend);
            try {
                lock.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Answer res = responses.remove();
            if(!res.type.equals("closed")) return null;
            JsonElement data = jsonParser.parse(res.data);
            if(data.getAsBoolean()){
                window.markAsClosed();
            }
            window.markTabsAsClosed();
            return window;
        }
    }
}