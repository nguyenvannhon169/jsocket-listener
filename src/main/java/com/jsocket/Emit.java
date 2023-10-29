package com.jsocket;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class Emit {
    protected List<JsClient> clientList;
    protected Gson gson;

    public Emit() {
        this.gson = new Gson();
    }

    public void dispatch(String command, String content) {
        for (JsClient client: this.clientList) {
            try {
                client.output.println("JSocket =##= " + gson.toJson(new Message(command, content)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
