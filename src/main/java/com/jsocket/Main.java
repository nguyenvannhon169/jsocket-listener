package com.jsocket;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JsListener listener = new JsListener(3456);

        listener.onEcho("connection", (client, response) -> {
            client.onEmit().dispatch("Hi", "OKE");
        });

        listener.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextLine()) {
                case "Exit":
                    listener.stop();
                    System.exit(0);
                    break;
                case "Check":
                    for (JsClient client: listener.clientList) {
                        System.out.println(client.id + " | " + client.socket.isClosed());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}