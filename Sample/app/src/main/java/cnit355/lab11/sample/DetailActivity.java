package cnit355.lab11.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    EditText WebEmail, WebPassword;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Button remove = findViewById(R.id.del);
        EditText email = findViewById(R.id.WebEmail);
        EditText password = findViewById(R.id.WebPassword);
        CheckBox pas = findViewById(R.id.checkBox);
        Button change = findViewById(R.id.change);
        pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pas.isChecked())
                {
                    password.setTransformationMethod(null);
                }
                if(!pas.isChecked())
                {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        Intent mIntent = getIntent();
        String user = mIntent.getStringExtra("user");
        String websiteText = mIntent.getStringExtra("website");
        String emailText = mIntent.getStringExtra("email");
        String passwordText = mIntent.getStringExtra("password");
        String name = mIntent.getStringExtra("name");
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = password.getText().toString();
                if(test.equals(passwordText))
                {
                    Toast.makeText(getApplicationContext(), "Please choose a different password...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    List<String> editing = new ArrayList<String>();
                    AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
                    String encrypted = aesEncryptionDecryption.encrypt(passwordText, "355Project");
                    String newEncrypted = aesEncryptionDecryption.encrypt(password.getText().toString(), "355Project");
                    String removeMe = websiteText+","+emailText+","+encrypted;
                    String addMe = websiteText+","+emailText+","+newEncrypted;
                    File root = new File(Environment.getExternalStorageDirectory(), "/Documents");
                    File passFiles = new File(root, MainActivity.user);
                    try(BufferedReader br = new BufferedReader(new FileReader(passFiles)))
                    {
                        String line;
                        int count = 0;
                        while((line = br.readLine()) != null)
                        {
                            if(count >0)
                            {
                                editing.add("\n"+line);
                            }
                            else
                            {
                                editing.add(line);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try
                    {
                        FileWriter fWriter = new FileWriter(passFiles);
                        int count = 0;
                        boolean added = false;
                        for (String s : editing)
                        {
                            if(s.equals(removeMe))
                            {
                                fWriter.write("\n"+addMe);
                                added = true;

                            }
                            else if(count == 0)
                            {
                                fWriter.write(s);
                            }
                            else
                            {
                                if(!added)
                                {
                                    fWriter.write("\n"+s);
                                }
                            }
                            count+=1;
                            added = false;
                        }
                        fWriter.close();
                        Toast.makeText(getApplicationContext(), "Your password has successfully been changed!", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        email.setText(emailText);
        password.setText(passwordText);
        password.setTransformationMethod(new PasswordTransformationMethod());
        backButton = (Button) findViewById(R.id.buttonBack);
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        String encrypted = aesEncryptionDecryption.encrypt(passwordText, "355Project");
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> editing = new ArrayList<String>();
                String removeMe = websiteText+","+emailText+","+encrypted;
                File root = new File(Environment.getExternalStorageDirectory(), "/Documents");
                File passFiles = new File(root, MainActivity.user);
                try(BufferedReader br = new BufferedReader(new FileReader(passFiles)))
                {
                    String line;
                    int count = 0;
                    while((line = br.readLine()) != null)
                    {
                        if(count >0)
                        {
                            editing.add("\n"+line);
                        }
                        else
                        {
                            editing.add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try
                {
                    FileWriter fWriter = new FileWriter(passFiles);
                    int count = 0;
                    for (String s : editing)
                    {
                        if(count == 0)
                        {
                            fWriter.write(s);
                        }
                        else if(!s.equals(removeMe))
                        {
                            fWriter.write("\n"+s);
                        }
                        count+=1;
                    }
                    fWriter.close();
                    Toast.makeText(getApplicationContext(), "You have removed your " + websiteText+ " account!", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(DetailActivity.this, MainActivity.class);
                mIntent.putExtra("user", user);
                mIntent.putExtra("name", name);
                startActivity(mIntent);
            }
        });

    }
}