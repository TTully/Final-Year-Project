package com.example.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewIPCameras extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    // Insert your Video URL
    String VideoURL;
    String VideoURLStart = "rtsp://";
    String VideoUrlEnd=":554/12";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ipcameras);
        videoview = (VideoView) findViewById(R.id.VideoView);

        // Get the Intent continuing the Bundle sent from MainActivity
        Intent myLocalIntent= getIntent();

        // Look at the Bundle sent from the MainActivity and retrieve the data
        Bundle BundleReceived = myLocalIntent.getExtras();
        String Name = BundleReceived.getString("name");
        String IP = BundleReceived.getString("ip");

        //Video URL with the IP  address sent across
        VideoURL = VideoURLStart+IP+VideoUrlEnd;


        // Create a progressbar
        pDialog = new ProgressDialog(ViewIPCameras.this);
        // Set progressbar title
        pDialog.setTitle("Loading Live Feed ");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    ViewIPCameras.this);
            mediacontroller.setAnchorView(videoview);

            Uri stream = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(stream);
            videoview.requestFocus();
        }
        catch (Exception e)
        {
            Log.e("#Error: ", e.getMessage());
            e.printStackTrace();
        }

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });
    }
}
