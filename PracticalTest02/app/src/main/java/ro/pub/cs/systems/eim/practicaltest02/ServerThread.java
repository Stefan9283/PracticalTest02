package ro.pub.cs.systems.eim.practicaltest02;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private boolean isRunning;
    Map<String, String> responseByUrl = new HashMap<String, String>();

    public void setData(String url, String response) {
        responseByUrl.put(url, response);
    }

    public Map<String, String> getResponseByUrl() {
        return responseByUrl;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(2000, 50, InetAddress.getByName("0.0.0.0"));

            while (isRunning) {
                Socket socket;
                socket = serverSocket.accept();

                if (socket != null) {
                    new CommunicationThread(this, socket).start();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {
        isRunning = true;
        start();
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}