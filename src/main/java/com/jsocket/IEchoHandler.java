package com.jsocket;

@FunctionalInterface
public interface IEchoHandler {
    public void handle(JsClient client, String response);
}
