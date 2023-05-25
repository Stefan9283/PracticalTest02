package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;


public class CommunicationThread extends Thread {
    Socket socket;
    ServerThread server;

    CommunicationThread(ServerThread serverThread, Socket socket) {
        this.socket = socket;
        this.server = serverThread;
    }

    @Override
    public void run() {
        try {

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String url = null;
            url = bufferedReader.readLine();

            Log.d("SERVER", url);


            String info = null;

            Map<String, String> responseByUrl = server.getResponseByUrl();

            if (responseByUrl.containsKey(url)) {
                Log.d("CommunicationThread", "Response already cached");
                info = (String) responseByUrl.get(url);
            } else {
                // do http request
                HttpURLConnection httpURLConnection;
                StringBuilder result = new StringBuilder();

                URL url1 = new URL(url);
                URLConnection urlConnection = null;
                try {
                    urlConnection = url1.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (urlConnection instanceof HttpURLConnection) {
                    httpURLConnection = (HttpURLConnection)urlConnection;
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String currentLineContent;
                    while (true) {
                        if ((currentLineContent = bufferedReader2.readLine()) == null) break;
                        result.append(currentLineContent + "\n");
                    }
                }

                info = result.toString();
                server.setData(url, info);
            }

            printWriter.println(info);

            socket.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
