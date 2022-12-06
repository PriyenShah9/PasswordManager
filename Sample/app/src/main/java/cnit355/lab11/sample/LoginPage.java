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
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginPage extends AppCompatActivity {
    TextView Username;
    TextView Password;
    Button log;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.pass);
        log = findViewById(R.id.log);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), RegisterAccount.class);
                startActivity(main);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Username.getText().toString();
                String pass = Password.getText().toString();
                if(user.length()>0 && pass.length()>0)
                {
                    //new DatabaseRequest().execute(user, pass);
                    if(user.equals("NDawg") && pass.equals("Newt1356"))
                    {
                        Toast.makeText(getApplicationContext(), "Login Success! Redirecting...", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.putExtra("user", user);
                        startActivity(main);
                    }
                    else if(user.equals("Priyen") && pass.equals("Password1"))
                    {
                        Toast.makeText(getApplicationContext(), "Login Success! Redirecting...", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.putExtra("user", user);
                        startActivity(main);
                    }
                    else if(user.equals("Charushi") && pass.equals("Password1"))
                    {
                        Toast.makeText(getApplicationContext(), "Login Success! Redirecting...", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        main.putExtra("user", user);
                        startActivity(main);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are filled in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
class DatabaseRequest extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        String res = null;
        String user = strings[0];
        String pass = strings[1];

        try {
            URL url = new URL("https://partisan-rap.000webhostapp.com/validateData.php/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            String data = "{\"user_name\":\"" + user + "\", \"password\":\"" + pass + "\"}";

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