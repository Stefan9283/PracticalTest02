package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends AsyncTask {
    TextView resultField;
    Integer port;
    String address;
    String url;
    public ClientThread(TextView server_message_text_view, int port, String address, String url) {
        resultField = server_message_text_view;
        this.port = port;
        this.address = address;
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Socket socket = new Socket(address, port);
            if (socket == null) return null;
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            printWriter.println(url);

            StringBuilder result = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                publishProgress(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        this.resultField.setText("");
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        this.resultField.append(values[0] + "\n");
    }

}
