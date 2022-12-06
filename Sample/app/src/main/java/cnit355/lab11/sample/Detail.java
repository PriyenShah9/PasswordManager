package cnit355.lab11.sample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Detail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Detail.
     */
    // TODO: Rename and change types and number of parameters
    public static Detail newInstance(String param1, String param2) {
        Detail fragment = new Detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Button remove = getView().findViewById(R.id.del);
        TextView email = getView().findViewById(R.id.email);
        EditText password = getView().findViewById(R.id.WebPassword);
        CheckBox pas = getView().findViewById(R.id.checkBox);
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
        String website = "Netflix.com"; //REPLACE WITH VARIABLE
        String emailText = "nathang0515@gmail.com"; // REPLACE WITH VARIABLE
        String passwordText = "Newt1356"; //REPLACE WITH VARIABLE
        email.setText(emailText);
        password.setText(passwordText);
        password.setTransformationMethod(new PasswordTransformationMethod());
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        String encrypted = aesEncryptionDecryption.encrypt(passwordText, "355Project");
        /*remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> editing = new ArrayList<String>();
                String removeMe = website+","+emailText+","+encrypted;
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
                    for (String s : editing)
                    {
                        if(!s.equals(removeMe))
                        {
                            fWriter.write(s);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}
