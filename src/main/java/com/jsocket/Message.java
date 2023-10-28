package com.jsocket;

public class Message {
    public String command;
    public String content;

    public Message() {}

    public Message(String command, String content) {
        this.command = command;
        this.content = content;
    }
}
