package cnit355.lab11.sample;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    public static String user = "";
    public static String name = "";
    private RecyclerView recyclerview;
    private List<String> sites = new ArrayList<String>();
    private List<String> email = new ArrayList<String>();
    private List<String> password = new ArrayList<String>();
    private ArrayList<Websites> websiteArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Intent login = getIntent();
        name = login.getStringExtra("name");
        user = login.getStringExtra("user");
        setTitle("Hello " + name + "!");




        dataInitialize();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);

        MyAdapter adapter = new MyAdapter(websiteArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //replaceFragment(new Home());
    }



    public String tempusr = "";

    private void dataInitialize() {
        File usr = new File(Environment.getExternalStorageDirectory(), "/Documents");
        tempusr = MainActivity.user;
        File curUSR = new File(usr, tempusr);
        try(BufferedReader br = new BufferedReader(new FileReader(curUSR))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null)
            {
                if(count == 0)
                {

                }
                else
                {
                    AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
                    final String secretKey = "355Project";
                    String[] split = line.split(",");
                    String pass = aesEncryptionDecryption.decrypt(split[2], secretKey);
                    sites.add(split[0]);
                    email.add(split[1]);
                    password.add(pass);
                }
                count+=1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        websiteArrayList = new ArrayList<Websites>();

        for (int i  = 0; i <sites.size(); i++){
            Websites web = new Websites(sites.get(i), email.get(i), password.get(i));
            websiteArrayList.add(web);
        }
        ImageButton addNew = findViewById(R.id.AddNew);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, addItem.class);
                mIntent.putExtra("user", tempusr);
                mIntent.putExtra("name", name);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent mIntent = new Intent(MainActivity.this, DetailActivity.class);
        mIntent.putExtra("user", user);
        mIntent.putExtra("website", websiteArrayList.get(position).Name);
        mIntent.putExtra("email", String.valueOf(websiteArrayList.get(position).Email));
        mIntent.putExtra("password", String.valueOf(websiteArrayList.get(position).Password));
        mIntent.putExtra("name", name);
        startActivity(mIntent);

    }




}