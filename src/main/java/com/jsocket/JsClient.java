package com.jsocket;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class JsClient {
    protected String id;
    protected Socket socket;
    protected PrintWriter output;
    protected BufferedReader input;
    protected Thread thread;
    protected Emit emit;

    public JsClient(Socket socket, JsListener listener) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.emit = new Emit();
        this.runThread(listener);
    }

    protected void runThread(JsListener listener)
    {
        this.thread = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    if (input.read() != -1) {
                        String[] response = input.readLine().split("=##=");
                        if(response.length == 2 && !response[1].isBlank()) {
                            Message message = new Gson().fromJson(response[1], Message.class);
                            if (listener.echoHandlers.containsKey(message.command)) {
                                listener.echoHandlers.get(message.command).handle(this, message.content);
                            }
                        }
                    }
                    listener.removeDisconnect();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }

                if (socket.isClosed()) break;
            }
        });

        thread.start();
    }

    protected Emit onEmit() {
        emit.clientList = List.of(this);
        return emit;
    }

    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
            thread.join();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
