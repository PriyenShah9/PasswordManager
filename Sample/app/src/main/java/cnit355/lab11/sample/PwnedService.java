package cnit355.lab11.sample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;


import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PwnedService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;
    @Override
    public IBinder onBind (Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log.e( TAG , "onStartCommand" ) ;
        super.onStartCommand(intent , flags , startId) ;
        startTimer() ;
        return START_STICKY ;
    }
    @Override
    public void onCreate () {
        Log. e ( TAG , "onCreate" ) ;
    }
    @Override
    public void onDestroy () {
        Log. e ( TAG , "onDestroy" ) ;
        stopTimerTask() ;
        super .onDestroy() ;
    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer () {
        timer = new Timer() ;
        initializeTimerTask() ;
        timer.schedule( timerTask , 5000 , Your_X_SECS * 1000 ) ; //
    }
    public void stopTimerTask () {
        if ( timer != null ) {
            timer.cancel() ;
            timer = null;
        }
    }
    public List<String> PassList = new ArrayList<String>();
    public void initializeTimerTask () {
        timerTask = new TimerTask() {
            public void run () {
                handler.post( new Runnable() {
                    public void run () {
                        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                        if(!root.exists()){
                            root.mkdirs();
                        }
                        File passFiles = new File(root, "savedData.txt");
                        try
                        {
                            FileReader fr = new FileReader(passFiles);
                            BufferedReader br = new BufferedReader(fr);
                            StringBuffer sb = new StringBuffer();
                            String line;
                            while((line=br.readLine())!=null)
                            {
                                PassList.add(line);
                            }
                            fr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CheckVulnerable check = new CheckVulnerable();
                        for(String z: PassList)
                        {
                            check.execute(z);
                        }
                        //createNotification() ;
                    }
                }) ;
            }
        } ;
    }
    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "My Notification" ) ;
        mBuilder.setContentText( "Notification Listener Service Example" ) ;
        mBuilder.setTicker( "Notification Listener Service Example" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
    ProgressDialog progressDialog;
    List<String> passes = new ArrayList<>();
    public interface TimeOut {
        void ShuaXin();
    }
    public class CheckVulnerable extends AsyncTask<String, Integer, String> {

        private InputStream getServetReponse(String passHash, HttpClient mHttpCLient, HttpGet post) {
            try {
                ClassicHttpResponse mResponse = (ClassicHttpResponse) mHttpCLient.execute(post);
                HttpEntity mEntity = mResponse.getEntity();
                Log.i("WORK", EntityUtils.toString(mEntity, "UTF-8"));
                return (mEntity.getContent());
            } catch (SocketTimeoutException e) {
                Log.i("lzw","connection_timeout");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("lzw","IOException");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return (null);
        }
        @Override
        protected String doInBackground(String... strings) {
            String hash = strings[0];
            String firstFive = "";
            String sha1 = "";
            String url = "";
            try{
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(hash.getBytes("utf8"));
                sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
                firstFive = sha1.substring(0, 5);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            url = "https://api.pwnedpasswords.com/range/" + firstFive;
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            getServetReponse(sha1, httpClient, httpGet);
            BaseHttpConn baseHttpConn = new BaseHttpConn(new TimeOut() {
                @Override
                public void ShuaXin() {
                }
            });
            return null;
        }
        class BaseHttpConn {
            private final String TAG = "BaseHttpConn";
            private int TimeOut = 3 * 1000;
            private TimeOut shuaxin;

            public HttpURLConnection httpConn(String baseUrl, HashMap<String, String> map) {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(baseUrl);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(TimeOut);
                    httpURLConnection.setReadTimeout(TimeOut);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setUseCaches(true);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(getByte(map));
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    httpURLConnection.disconnect();
                    shuaxin.ShuaXin();

                }
                return httpURLConnection;
            }

            private byte[] getByte(HashMap<String, String> map) {
                JSONObject jsonObject = new JSONObject();
                for (String key : map.keySet()) {
                    try {
                        jsonObject.put(key, map.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return jsonObject.toString().getBytes();
            }

            public BaseHttpConn(TimeOut timeOut) {
                this.shuaxin = timeOut;
            }
        }


    }
}