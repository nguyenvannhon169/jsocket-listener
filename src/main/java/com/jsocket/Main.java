package com.jsocket;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JsListener listener = new JsListener(3333);

        final String connection = "connection";

        listener.onEcho(connection, (client, response) -> {
            client.onEmit().dispatch("OKE", "OKE");
        });

        listener.start();

        Scanner scanner = new Scanner(System.in);
        if (scanner.nextBoolean()) {
            listener.stop();
            System.exit(0);
        }
    }
}