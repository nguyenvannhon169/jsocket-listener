package com.jsocket;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class Emit {

    protected List<JsClient> clientList;
    protected Gson gson;

    public Emit(List<JsClient> clientList) {
        this.clientList = clientList;
        this.gson = new Gson();
    }

    public void dispatch(String command, String content) {
        for (JsClient client: this.clientList) {
            try {
                client.output.writeUTF(gson.toJson(new Message(command, content)));
            } catch (IOException e) {
                System.out.println("Cannot send data to client" + client.id);
                throw new RuntimeException(e);
            }
        }
    }
}
