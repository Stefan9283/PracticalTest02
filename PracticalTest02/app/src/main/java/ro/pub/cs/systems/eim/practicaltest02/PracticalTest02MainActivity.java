package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02.databinding.ActivityPracticalTest02MainBinding;

public class PracticalTest02MainActivity extends AppCompatActivity {
    EditText address_edit_text;
    EditText port_edit_text;
    EditText url_edit_text;
    Button send_request_button;
    TextView server_message_text_view;
    ClientThread clientThread;

    ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        address_edit_text = (EditText)findViewById(R.id.server_address_edit_text);
        port_edit_text = (EditText)findViewById(R.id.server_port_edit_text);
        url_edit_text = (EditText)findViewById(R.id.params_edit_text);
        send_request_button = (Button) findViewById(R.id.request_button);
        server_message_text_view = (TextView) findViewById(R.id.server_message_text_view);

        address_edit_text.setText("0.0.0.0");
        port_edit_text.setText("2000");
        url_edit_text.setText("https://pokeapi.co/api/v2/pokemon/ditto");

        send_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PracticalTest02MainActivity", "SendingRequest");
                clientThread = new ClientThread(
                        server_message_text_view,
                        Integer.parseInt(port_edit_text.getText().toString()),
                        address_edit_text.getText().toString(),
                        url_edit_text.getText().toString()
                );
                clientThread.execute();
            }
        });

        {
            // server
            serverThread = new ServerThread();
            serverThread.startServer();
        }

    }
}