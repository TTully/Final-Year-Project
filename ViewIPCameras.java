/*
This project uses an Unofficial VLC Android SDK pushed to JCenter.
https://wiki.videolan.org/LibVLC/#libVLC_on_Android
*/
package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.Toast;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ViewIPCameras extends Activity implements IVLCVout.Callback {
    public final static String TAG = "ViewIpCameras";
    String RTSP_URL;
    String URL_Start = "rtsp://";
    String Url_End=":554/12";
    private SurfaceView mSurface;
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    public int mVideoWidth;
    public int mVideoHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ipcameras);

        mSurface = (SurfaceView) findViewById(R.id.surface);

        // Get the width and height of the device the app is running on
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mVideoHeight= displayMetrics.heightPixels;
        mVideoWidth = displayMetrics.widthPixels;

        // Get the Intent continuing the Bundle sent from MainActivity
        Intent myLocalIntent= getIntent();

        // Look at the Bundle sent from the MainActivity and retrieve the data
        Bundle BundleReceived = myLocalIntent.getExtras();
        String Name = BundleReceived.getString("name");
        String IP = BundleReceived.getString("ip");

        //Video URL with the IP  address sent across
        RTSP_URL = URL_Start+IP+Url_End;
    }

    /**********************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        createPlayer(RTSP_URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    /**********************************************************************************************/

    //Creates MediaPlayer and plays video
    private void createPlayer(String media)
    {
        try
        {
            if (media.length() > 0)
            {

                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                toast.show();
            }
            ArrayList<String> options = new ArrayList<>();
            options.add("-vvv");
            options.add("--network-caching=500");
            options.add("--aout=opensles");

            libvlc = new LibVLC(this,options);

            // Creating media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            // Seting up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            vout.addCallback(this);
            vout.attachViews();

        } catch (Exception e) {
            Toast.makeText(this, "Error in creating player!", Toast
                    .LENGTH_LONG).show();
        }
    }

    /**********************************************************************************************/
    private void releasePlayer()
    {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }


    @Override
    public void onSurfacesCreated(IVLCVout vout)
    {
        Media media = new Media(libvlc, Uri.parse(RTSP_URL));
        mMediaPlayer.setMedia(media);
        mMediaPlayer.setAspectRatio("16:9");
        mMediaPlayer.getVLCVout().setWindowSize(mVideoWidth,mVideoHeight);
        mMediaPlayer.play();

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vout) {

    }

    /**********************************************************************************************/
    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private static class MyPlayerListener implements MediaPlayer.EventListener
    {
        private WeakReference<ViewIPCameras> mOwner;

        public MyPlayerListener(ViewIPCameras owner)
        {
            mOwner = new WeakReference<ViewIPCameras>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event)
        {
            ViewIPCameras player = mOwner.get();

            switch (event.type)
            {
                case MediaPlayer.Event.EndReached:
                    Log.d(TAG, "Media Player End Reached");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }
    /**********************************************************************************************/
}