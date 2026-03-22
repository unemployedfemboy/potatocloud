package net.potatocloud.node.utils;

import java.io.IOException;
import java.net.ServerSocket;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    public static boolean isPortFree(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
