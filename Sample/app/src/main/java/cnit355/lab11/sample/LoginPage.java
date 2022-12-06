package cnit355.lab11.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    TextView Username;
    TextView Password;
    Button log;
    Button reg;
    public List<String> users = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Username = findViewById(R.id.email);
        Password = findViewById(R.id.pass);
        log = findViewById(R.id.log);
        reg = findViewById(R.id.addAnother);
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
                String user = Username.getText().toString()+".txt";
                String pass = Password.getText().toString();

                if(user.length()>0 && pass.length()>0)
                {
                    File usr = new File(Environment.getExternalStorageDirectory(), "/Documents");
                    String[] userList = usr.list();
                    String theUser = "";
                    for(String file : userList)
                    {
                        if(file.equals(user))
                        {
                            theUser=user;
                        }
                    }
                    //new DatabaseRequest().execute(user, pass);
                    if(theUser != "")
                    {
                        File curUSR = new File(usr, theUser);
                        try(BufferedReader br = new BufferedReader(new FileReader(curUSR))){
                            String line;
                            int count = 0;
                            while((line = br.readLine()) != null)
                            {
                                if(count == 0) {
                                    final String secretKey = "355Project";
                                    String[] split = line.split(",");
                                    String tempPass = split[2];
                                    AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
                                    String decryptedPass = aesEncryptionDecryption.decrypt(tempPass, secretKey);
                                    if (pass.equals(decryptedPass)) {
                                        File root = new File(Environment.getExternalStorageDirectory(), "/Documents");
                                        File passFiles = new File(root, "latestLog.txt");
                                        passFiles.getParentFile().mkdirs();
                                        if (!passFiles.exists()) {
                                            try {
                                                passFiles.createNewFile();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        FileWriter fWriter = new FileWriter(passFiles);
                                        fWriter.write(theUser);
                                        fWriter.close();
                                        Toast.makeText(getApplicationContext(), "You have logged in!", Toast.LENGTH_SHORT).show();
                                        Intent main = new Intent(LoginPage.this, MainActivity.class);
                                        main.putExtra("user", theUser);
                                        main.putExtra("name", split[0]);
                                        startActivity(main);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                count+=1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
    @Override
    protected void onStop() {
        super.onStop();
        //startService(new Intent(this, PwnedService.class));
    }
}

/*class DatabaseRequest extends AsyncTask<String, String, String> {

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
}*/
