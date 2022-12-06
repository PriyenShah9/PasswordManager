package cnit355.lab11.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class RegisterAccount extends AppCompatActivity {
    TextView FullName;
    TextView Username;
    TextView Password;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        FullName = findViewById(R.id.fullName);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.pass);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = FullName.getText().toString();
                String user = Username.getText().toString();
                String pass = Password.getText().toString();
                if(name.length() > 0 && user.length()>0 && pass.length()>0)
                {
                    Intent mTent = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(mTent);
                    //new DatabaseRequest().execute(name, user, pass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are filled in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
class Request extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        String res = null;
        String name = strings[0];
        String user = strings[1];
        String pass = strings[2];
        try {
            URL url = new URL("https://partisan-rap.000webhostapp.com/insertData.php/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            String data = "{\"name\":\"" + name + "\", \"user_name\":\"" + user + "\", \"password\":\"" + pass + "\"}";

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            res = http.getResponseMessage();
            http.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
