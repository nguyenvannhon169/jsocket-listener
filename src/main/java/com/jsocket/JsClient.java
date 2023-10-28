package com.jsocket;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class JsClient {
    protected String id;
    protected Socket socket;
    protected DataOutputStream output;
    protected DataInputStream input;
    protected Thread thread;

    public JsClient(Socket socket, JsListener listener) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.output = new DataOutputStream(socket.getOutputStream());
        this.input = new DataInputStream(socket.getInputStream());
        this.runThread(listener);
    }

    protected void runThread(JsListener listener)
    {
        this.thread = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    if (input.read() != -1) {
                        String response = input.readUTF();
                        Message message = new Gson().fromJson(response, Message.class);
                        if (listener.echoHandlers.containsKey(message.command)) {
                            listener.echoHandlers.get(message.command).handle(this, message.content);
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (socket.isClosed()) break;
            }
        });

        thread.start();

        this.close();
    }

    protected Emit onEmit() {
        return new Emit(List.of(this));
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
