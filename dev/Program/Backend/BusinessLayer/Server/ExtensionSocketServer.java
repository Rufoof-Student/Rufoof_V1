
package dev.Program.Backend.BusinessLayer.Server;

import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedHashTreeMap;

import dev.Program.Backend.BusinessLayer.Shelf.Shelf;
import dev.Program.DTOs.*;
import dev.Program.DTOs.Exceptions.DeveloperException;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExtensionSocketServer extends WebSocketServer {

    private ConcurrentLinkedQueue<ChromeWindow> googleWindows;
    private Thread workerThread;
    private ConcurrentLinkedQueue<WebSocket> conns;
    private Gson gson = new Gson();
    private JsonParser jsonParser = new JsonParser();
    private Object lock = new Object();
    private WebSocket edge;
    private WebSocket chrome;

    // =====
    private ConcurrentLinkedQueue<Answer> responses = new ConcurrentLinkedQueue<>();

    public ExtensionSocketServer(int port) {
        super(new InetSocketAddress(port));
        workerThread = new Thread();
        googleWindows = new ConcurrentLinkedQueue<>();
        conns = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        synchronized (lock) {
            if (edge.equals(conn))
                edge = null;
            else
                chrome = null;
            // conns.remove(conn);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("recieved msg:" + message);

        try {

            Map<String, Object> msgMap = gson.fromJson(message, Map.class);
            if (msgMap.get("tag").equals("Question")) {
                Question question = new Question((String) msgMap.get("type"), (String) msgMap.get("data"));
                // dealWithQuestion(question);
            } else if (msgMap.get("tag").equals("Answer")) {
                Answer answer = new Answer((String) msgMap.get("type"), (String) msgMap.get("data"));
                synchronized (lock) {
                    if (answer.type.equals("engineName")) {
                        // responses.remove();
                        if (answer.data.equals("chrome.exe")) {
                            chrome = conns.remove();
                            System.out.println("chrome has been initilaized ");
                        } else {
                            edge = conns.remove();
                            System.out.println("edge has been initilaized ");
                        }
                    } else
                        responses.add(answer);
                    lock.notifyAll();
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
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
    public List<Group> getCurrentFreeTabs(String engineName) {
        WebSocket conn = getConn(engineName);
        if (conn == null) {
            System.out.println("there is no connections in the server");
            return null;
        }

        try {
            Question question = new Question("getWindows");
            String toSend = gson.toJson(question);
            synchronized (lock) {

                conn.send(toSend);
                while (responses.size() == 0)
                    lock.wait();
                Answer res = responses.remove();
                System.out.println(res.type);

                if (!res.type.equals("Windows")) {
                    System.out.println("Windows not recived!");
                    return null;
                }
                return Group.createGroupFromFreeTabsAnswerJSON(res.data,engineName);
            }

        } catch (Exception ex) {
            System.out.println("An error occured with sinding -getWindows- to the client:");
            ex.printStackTrace();
            return null;
        }
    }

    private WebSocket getConn(String engineName) {
        return engineName.equals("chrome.exe") ? chrome : edge;
    }

    public boolean connectionIsOpenedWithGoogle(String engineName) {
        return engineName.equals("chrome.exe") ? chrome != null : edge != null;
    }

    public GroupPack createGroups(List<Group> groups, String engineName) throws DeveloperException {
        if (groups.size() == 0)
            return new GroupPack();
        Group[] groupsAsArray = new Group[groups.size()];
        Map<String, Group> dicGroup = new ConcurrentHashMap<>();
        for (int i = 0; i < groups.size(); i++) {
            groupsAsArray[i] = groups.get(i);
            dicGroup.put(groups.get(i).getNativeWindowId(), groups.get(i));
        }
        synchronized (lock) {
            Answer answer = sendAndReciveQeustion("createNewGroups", gson.toJson(groupsAsArray), "done", engineName);
            JsonArray jsonGroupArray = jsonParser.parse(answer.data).getAsJsonArray();
            List<Group> toRet = new ArrayList<>();
            for (JsonElement jsonElement : jsonGroupArray) {
                String windowId = jsonElement.getAsJsonObject().get("windowId").getAsString();
                Group g = dicGroup.get(windowId);
                g.setGroupId(jsonElement.getAsJsonObject().get("groupId").getAsString());
                toRet.add(g);
            }
            return new GroupPack(toRet);
        }
    }

    private Group[] getGroupAsArray(List<Group> groubsList) {
        Group[] groupsAsArray = new Group[groubsList.size()];
        for (int i = 0; i < groubsList.size(); i++) {
            groupsAsArray[i] = groubsList.get(i);
        }
        return groupsAsArray;
    }

    public List<Group> closeAllGroups(List<Group> groupsToClose, String engineName) throws DeveloperException {
        Group[] groupsAsArray = getGroupAsArray(groupsToClose);
        synchronized (lock) {
            Answer answer = sendAndReciveQeustion("closeAllGroups", gson.toJson(groupsAsArray), "closed", engineName);
            JsonArray jsonGroupArray = jsonParser.parse(answer.data).getAsJsonArray();
            for (JsonElement jsonElement : jsonGroupArray) {
                String windowId = jsonElement.getAsJsonObject().get("nativeWindowId").getAsString();
                for (Group group : groupsToClose) {
                    if (group.getNativeWindowId().equals(windowId)) {
                        group.addTabs(Tab.createTabsFromJsonArray(
                                jsonElement.getAsJsonObject().get("tabs").getAsJsonArray(), windowId));
                    }
                }
            }
            return groupsToClose;
        }
    }

    private Answer sendAndReciveQeustion(String type, String data, String resType, String engineName) throws DeveloperException {

        System.out.println("data sent to client :" + data);
        WebSocket con = engineName.equals("chrome.exe") ? chrome : edge;
        if (con == null)
            throw new DeveloperException("There is no connection with requested engine :"+engineName+". chrome is null?"+(chrome==null)+" . eedge is null?"+(edge==null));
        Question question = new Question(type, data);
        // try{
            con.send(gson.toJson(question));
        // }catch(WebsocketNotConnectedException ex){
        //     if (engineName.equals("chrome.exe")) {
        //         chrome=null;
        //     }else{
        //         edge=null;
        //     }
        // }
        while (responses.size() == 0)
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        Answer answer = responses.remove();
        if (!answer.type.equals(resType))
            throw new DeveloperException("the answer type recieved is \""+answer.type+"\" and not "+resType);
        return answer;
    }

    public List<Group> runAllGroups(List<Group> groupsToOpen, Shelf shelf, String engineName) {
        Group[] groupsToSend = getGroupAsArray(groupsToOpen);
        synchronized (lock) {
            Answer answer = sendAndReciveQeustion("runGroubs", gson.toJson(groupsToSend), "running", engineName);
            return Group.getAllGroupsAsOpened(answer.data, shelf,engineName);
        }
    }

    public String getUpdatedDataForTabAsJSON(String id, String engineName) {
        // String data = "{\"nativeTabId\":\""+id+"\"}";
        JsonObject data = new JsonObject();
        data.addProperty("nativeTabId", id);
        synchronized (lock) {
            Answer answer = sendAndReciveQeustion("getUrlFor", data.toString(), "url", engineName);
            return answer.data;
        }
    }

    public void filterEmptyTabs(String engineName) {
        synchronized (lock) {
            sendAndReciveQeustion("filter", "{}", "filtered", engineName);
        }
    }

}