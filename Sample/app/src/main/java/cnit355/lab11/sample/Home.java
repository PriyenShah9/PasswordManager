package cnit355.lab11.sample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Websites> websiteArrayList;
    private String[] headings;
    private RecyclerView recyclerview;
    private List<String> sites = new ArrayList<String>();
    private List<String> email = new ArrayList<String>();
    private List<String> password = new ArrayList<String>();
    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize();
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        MyAdapter adapter = new MyAdapter(getContext(), websiteArrayList);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        File usr = new File(Environment.getExternalStorageDirectory(), "/Documents");
        String tempusr = MainActivity.user+".txt";
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
        /*
        headings = new String[]{
                "Website 1",
                "Website 2",
                "Website 3",
                "website 4",
                "Website 5",
                "Website 6",
                "Website 7",

        };

         */

        for (int i  = 0; i <sites.size(); i++){
            Websites web = new Websites(sites.get(i), email.get(i), password.get(i));
            websiteArrayList.add(web);
        }
    }
}