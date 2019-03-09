package com.example.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

public class IpScanner extends AppCompatActivity
{
    private Button button;
    static  TextView results;
    static int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_scanner);

        //create our toolbar for this activity
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        results = (TextView) findViewById(R.id.Results);
        button = findViewById(R.id.buttonScan);
        results.setMovementMethod(new ScrollingMovementMethod());
    }

    /**********************************************************************************************/
    private static class NetworkIPScanner extends AsyncTask<Void, String, Void>
    {
        private static final String TAG = "NetworkSearchIPsTask";
        private WeakReference<Context> mContextRef;

        private NetworkIPScanner(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            results.append("*************************************\n");
            results.append("- Starting Scan.....\n\n");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.i(TAG, "Let's search the network...");
            try {
                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress(); //Get IP address of phone
                    String ipString = Formatter.formatIpAddress(ipAddress); //cast to string

                    Log.i(TAG, "activeNetwork: " + String.valueOf(activeNetwork)); // Display the Info for this active Network
                    Log.i(TAG, "IP Of Phone: " + String.valueOf(ipString)); // Display the IP of this phone
                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.i(TAG, "prefix: " + prefix);    // Display the prefix

                    for (int i =0; i < 255; i++)
                    {
                        String testIp = prefix + String.valueOf(i); //IPs to be tested example (192.168.1.0 --> 192.168.1.254)
                        InetAddress address = InetAddress.getByName(testIp); //returns the localHost IP and name

                        String hostName = address.getCanonicalHostName(); //Gets the fully qualified domain name for this IP Address
                        if (address.isReachable(100))
                        {
                            Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                            String IPAddresses = "- Host: " + String.valueOf(hostName) + "\n- IP Address:" + String.valueOf(testIp) +"\n";
                            publishProgress((String) IPAddresses);
                        }
                    }
                }
            }

            catch (Throwable t)
            {
                Log.e(TAG, "Error trying to sniff the network.", t);
            }
            return null;
        }

        // Update the text field with every Active IP Address it finds
        @Override
        protected void onProgressUpdate(String... value)
        {
            cnt++;
            super.onProgressUpdate(value[0]);
            results.append("*************************************\n");
            results.append( value[0] + "\n");
        }

        protected void onPostExecute(final Void unused)
        {
            results.append("*************************************\n");
            results.append("- Found " + String.valueOf(cnt)+ " Active IP Addresses.\n");
            results.append("- IP Scanning Completed!\n");
            results.append("*************************************\n");
            cnt=0;
        }
    }
    /**********************************************************************************************/

    public void ReturnToAddCameraActivity(View view)
    {
        finish();
    }

    public void IPScanner(View view)
    {
        new NetworkIPScanner(this).execute();
        button.setEnabled(false);
    }

    /**********************************************************************************************/
}
