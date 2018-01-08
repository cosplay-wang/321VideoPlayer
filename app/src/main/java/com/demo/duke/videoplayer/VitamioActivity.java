package com.demo.duke.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioActivity extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.activity_vitamio);
        findViews();
        playfunction();
    }

    private void findViews() {
        videoView = findViewById(R.id.vitamio_videoview);
    }

    void playfunction() {
        String path = "http://192.168.1.106:8080/Demo/Video/飘花电影piaohau.com末日崩塌HD1280高清中英双字.mkv";
        // path="http://gslb.miaopai.com/stream/3D~8BM-7CZqjZscVBEYr5g__.mp4";

        if (path == "") {
            // Tell the user to provide a media file URL/path.
            Toast.makeText(VitamioActivity.this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
            return;
        } else {
            /*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
            videoView.setVideoPath(path);
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
        }
    }
}
