
package dev.Program.Backend.BusinessLayer;

import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.Program.Backend.BusinessLayer.ServerMessages.Answer;
import dev.Program.Backend.BusinessLayer.ServerMessages.ChromeWindowToSendSocket;
import dev.Program.Backend.BusinessLayer.ServerMessages.Question;
import dev.Program.Backend.BusinessLayer.ServerMessages.TabToSendSocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class ExtensionSocketServer extends WebSocketServer {

    private List<ChromeWindow> googleWindows;
    private Thread workerThread;
    private List<WebSocket> conns;
    private Gson gson = new Gson();
    private JsonParser jsonParser = new JsonParser();
    private Object lock = new Object();
    private boolean isRecollection = false;
    private boolean isOpenProccess;

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
        conns.remove(conn);
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
                dealWithAnswer(answer);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void dealWithAnswer(Answer answer) {
        System.out.println("recieed data :\n" + answer.data + " \nwith type:" + answer.type);

        if (answer.type.equals("Windows")) {
            refillWindowsFromJSON(answer.data);
        } else if (answer.type.equals("OpenProcessIsFinished")) {
            synchronized (lock) {
                isOpenProccess = false;
                lock.notifyAll();
            }
        }

    }

    private void refillWindowsFromJSON(String JSONdata) {
        synchronized (lock) {
            googleWindows = new ArrayList<>();
            JsonArray data = jsonParser.parse(JSONdata).getAsJsonArray();
            for (JsonElement jsonElement : data) {
                JsonObject windowJsonObject = jsonElement.getAsJsonObject();
                String windowId = windowJsonObject.get("windowId").getAsString();
                System.out.println("navigating with window id:" + windowId);
                JsonArray tabsJsonArray = windowJsonObject.get("tabs").getAsJsonArray();
                List<Tab> tabs = new ArrayList<>();
                for (JsonElement tabJsonElement : tabsJsonArray) {
                    JsonObject tabJsonObject = tabJsonElement.getAsJsonObject();
                    Tab tabToAdd = new Tab(tabJsonObject.get("url").getAsString(),
                            tabJsonObject.get("tabId").getAsString(),
                            tabJsonObject.get("title").getAsString());
                    tabs.add(tabToAdd);
                }
                googleWindows.add(new ChromeWindow(windowId, tabs,this,true));
            }
            if (isRecollection) {
                lock.notifyAll();
                isRecollection = false;
            }
        }
        System.out.println("google windows recolected successfuly!");

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
     * This functioin will aks the extension/client to send the opened google
     * windows .
     * 
     * @return the updated google opened windows. null if there is no connection.
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
                isRecollection = true;
                conn.send(toSend);
                lock.wait();

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
                    tabs.add(new Tab("https://mail.google.com/mail/u/0/#inbox"));
                    ChromeWindow windows ;
                    windows=new ChromeWindow(null, tabs,s);
                    
                    List<ChromeWindow> b = s.openWindows(windows);

                    for (ChromeWindow chromeWindow : b) {

                        System.out.println(chromeWindow);
                    }
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
    public boolean openWindows(ChromeWindow windowToOpen) {
        if (conns.size() == 0) {
            return false;
        }
        WebSocket conn = conns.get(0);
        String jsonData = getWindowsToSendAsJson(windowToOpen);
        Question question = new Question("openWindows", jsonData);
        synchronized (lock) {
            isOpenProccess = true;
            conn.send(gson.toJson(question));
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return ;
        }
        // googleWindows = getCurrentGoogleOpenedWindows();
    }

    private String getWindowsToSendAsJson(ChromeWindow windowToOpen) {

        List<Tab> tabs = windowToOpen.getTabs();
        TabToSendSocket[] tabsToSend = new TabToSendSocket[tabs.size()];
        for (int j = 0; j < tabsToSend.length; j++) {
            tabsToSend[j] = new TabToSendSocket(tabs.get(j).getUrl(), null, null);
        }
        ChromeWindowToSendSocket windowsToSend = new ChromeWindowToSendSocket(null, tabsToSend);

        return gson.toJson(windowsToSend);
    }
}