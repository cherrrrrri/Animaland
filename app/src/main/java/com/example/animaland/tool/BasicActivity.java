package com.example.animaland.tool;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animaland.R;

public class BasicActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayer1;
    static Context context;  //上下文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplication().getApplicationContext();
        mediaPlayer1 = MediaPlayer.create(context, R.raw.backmusic);
        mediaPlayer1.setLooping(true);
    }

    public static void playMusic(){
        if(mediaPlayer1!=null){
            mediaPlayer1.reset();
            mediaPlayer1 = MediaPlayer.create(context, R.raw.backmusic);
        }
        mediaPlayer1.start();
    }

    public static void pauseMusic(){
        mediaPlayer1.pause();
    }

}
