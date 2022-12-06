package cnit355.lab11.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
                    File root = new File(Environment.getExternalStorageDirectory(), "/Documents");
                    File passFiles = new File(root, user + ".txt");
                    passFiles.getParentFile().mkdirs();
                    if(!passFiles.exists())
                    {
                        try {
                            passFiles.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    final String secretKey = "355Project";
                    AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
                    String encryptedPass = aesEncryptionDecryption.encrypt(pass, secretKey);
                    String writeMe = name+","+user+","+encryptedPass;
                    try {
                        FileWriter fWriter = new FileWriter(passFiles);
                        fWriter.write(writeMe);
                        fWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
class AESEncryptionDecryption {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";

    public void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
/*class Request extends AsyncTask<String, String, String> {

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
}*/
