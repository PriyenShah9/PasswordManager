package cnit355.lab11.sample;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.keepitlogo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.KeepItColor));
        }
        websiteText = findViewById(R.id.website);
        emailText = findViewById(R.id.email);
        passText = findViewById(R.id.pass);
        ImageButton back = findViewById(R.id.backBUTT);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = getIntent();
                String user = mIntent.getStringExtra("user");
                String name = mIntent.getStringExtra("name");
                Intent mntent = new Intent(getApplicationContext(), MainActivity.class);
                mntent.putExtra("name", name);
                mntent.putExtra("user", user);
                startActivity(mntent);
            }
        });
        ImageButton add = findViewById(R.id.addAnother);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lines = new ArrayList<String>();
                Intent mIntent = getIntent();
                String user = mIntent.getStringExtra("user");
                String name = mIntent.getStringExtra("name");
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
                    int inter = 0;
                    for(String s : lines)
                    {
                        try {
                            if(inter != 0)
                            {
                                fWriter.write("\n"+s);
                            }
                            else
                            {
                                fWriter.write(s);
                            }
                            inter+=1;
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
                    mntent.putExtra("name", name);
                    mntent.putExtra("user", user);
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
