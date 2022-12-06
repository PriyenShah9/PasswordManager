package cnit355.lab11.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {
    EditText WebEmail, WebPassword;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent mIntent2 = getIntent();
        WebEmail = (EditText) findViewById(R.id.WebEmail);
        WebPassword = (EditText) findViewById(R.id.WebPassword);
        String em = mIntent2.getStringExtra("Email");
        String pass = mIntent2.getStringExtra("Password");

        WebEmail.setText(em);
        WebPassword.setText(pass);

        backButton = (Button) findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        });

    }
}