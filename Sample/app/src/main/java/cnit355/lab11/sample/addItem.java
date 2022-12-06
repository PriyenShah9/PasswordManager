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

public class addItem extends AppCompatActivity {
    TextView websiteText;
    TextView emailText;
    TextView passText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        websiteText = findViewById(R.id.website);
        emailText = findViewById(R.id.email);
        passText = findViewById(R.id.pass);
        Button add = findViewById(R.id.addAnother);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lines = new ArrayList<String>();
                Intent mIntent = getIntent();
                String user = mIntent.getStringExtra("user");
                String website = websiteText.getText().toString();
                String email = emailText.getText().toString();
                String pass = passText.getText().toString();
                if(website.length() > 0 && email.length()>0 && pass.length()>0) {
                    File root = new File(Environment.getExternalStorageDirectory(), "/Documents");
                    File passFiles = new File(root, user);
                    try(BufferedReader br = new BufferedReader(new FileReader(passFiles))){
                        String line;
                        int count = 0;
                        while((line = br.readLine()) != null)
                        {
                            lines.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String secretKey = "355Project";
                    AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
                    String encryptedPass = aesEncryptionDecryption.encrypt(pass, secretKey);
                    String writeMe = website+","+email+","+encryptedPass;
                    lines.add(writeMe);
                    FileWriter fWriter = null;
                    try {
                        fWriter = new FileWriter(passFiles);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(String s : lines)
                    {
                        try {
                            fWriter.write("\n"+s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        fWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Your " + website + " account has been added!", Toast.LENGTH_SHORT).show();
                    Intent mntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mntent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are filled in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}