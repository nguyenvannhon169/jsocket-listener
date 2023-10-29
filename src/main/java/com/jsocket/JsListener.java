package com.jsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsListener {
    protected int port;
    protected boolean isDisconnect;
    protected ServerSocket serverSocket;
    protected List<JsClient> clientList;
    protected HashMap<String, IEchoHandler> echoHandlers;
    protected Thread thread;
    protected Emit emit;

    public JsListener(int port) {
        this.port = port;
        this.isDisconnect = false;
        this.clientList = new ArrayList<>();
        this.echoHandlers = new HashMap<>();
        this.emit = new Emit();

    }

    public void start() {
        this.thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server start to port " + port);
                while (!isDisconnect) {

                    if (isDisconnect()) {
                        serverSocket.close();
                        break;
                    }

                    try {
                        clientList.add(new JsClient(serverSocket.accept(), this));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Can not start server.");
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

    public void onEcho(String command ,IEchoHandler echoHandler) {
        if (echoHandlers.containsKey(command)) {
            System.out.println("Command ["+command+"] duplicated.");
        }
        echoHandlers.put(command, echoHandler);
    }

    public Emit onEmit() {
        emit.clientList = clientList;
        return emit;
    }

    public Emit onEmitTo(String id) {
        emit.clientList = clientList;
        return emit;
    }

    public Emit onEmitToMany(List<String> ids) {
        emit.clientList = clientList;
        return emit;
    }

    public Emit onEmitToRoom(String room) {
        emit.clientList = clientList;
        return emit;
    }

    public Emit onEmitToRooms(List<String> rooms) {
        emit.clientList = clientList;
        return emit;
    }

    public void stop() {
        for (JsClient client: clientList) {
            client.close();
        }
        isDisconnect = true;
        System.out.println("Server stopped.");
    }

    public boolean isDisconnect() {
        return isDisconnect;
    }

    public void removeDisconnect() {
        List<JsClient> jsClients = clientList.stream().filter(c -> c.socket.isClosed()).toList();
        clientList.removeAll(jsClients);
    }
}
