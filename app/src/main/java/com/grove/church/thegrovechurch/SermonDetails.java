package com.grove.church.thegrovechurch;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

public class SermonDetails extends AppCompatActivity {
    String mp3Path = "";
    String mp4Path = "";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int playbackPosition=0;
    private int playbackPositionVideo=0;

    private VideoView mVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sermon_details_view);

        TextView txtProduct = (TextView) findViewById(R.id.product_label);

        Intent i = getIntent();
        // getting attached intent data
        String jsonString = i.getStringExtra("details");

        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            mp3Path = jsonObj.getString("audioURL");
            mp4Path = jsonObj.getString("videoURL");
            txtProduct.setText(jsonObj.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button startButton = findViewById(R.id.startPlayerBtn);
        startButton.setOnClickListener(new View.OnClickListener() {
            // Restart paused state.
            public void onClick(View v) {
                if (playbackPosition > 0) {
                    mediaPlayer.seekTo(playbackPosition);
                    mediaPlayer.start();
                    return;
                }

                // Start from beginning.
                killMediaPlayer();

                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(mp3Path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final Button stopButton = findViewById(R.id.stopPlayerBtn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer.stop();
                playbackPosition = 0;
            }
        });


        final Button pauseButton = findViewById(R.id.pausePlayerBtn);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playbackPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        });

        mVideoView = (VideoView)findViewById(R.id.videoview);
        final Uri uri = Uri.parse(mp4Path);

        final Button startButtonVideo = findViewById(R.id.startPlayerBtnVideo);
        startButtonVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (playbackPositionVideo > 0) {
                    mVideoView.seekTo(playbackPositionVideo);
                    mVideoView.start();
                    return;
                }

                playbackPositionVideo = 0;
                mVideoView.setVideoURI(uri);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });

        final Button stopButtonVideo = findViewById(R.id.stopPlayerBtnVideo);
        stopButtonVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playbackPositionVideo = 0;
                mVideoView.stopPlayback();
            }
        });

        final Button pauseButtonVideo = findViewById(R.id.pausePlayerBtnVideo);
        pauseButtonVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playbackPositionVideo = mVideoView.getCurrentPosition();
                mVideoView.pause();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
