package com.example.metro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {
    TextView textView;
    SoundPool soundPool;
    AudioAttributes audioAttributes;
    Handler handler;
    int soundIdShot;
    final String LOG_TAG = "myLogs";
    Timer timer;
    TimerTask task;
    EditText bpmInput;
    int count = 1;
    int bpm;
    TextView textViewBpm;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bpmInput = findViewById(R.id.bpm);
        bpmInput.setFocusableInTouchMode(true);
        textViewBpm = findViewById(R.id.viewBpm);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_UNKNOWN)
                .build();

        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        soundPool.setOnLoadCompleteListener(this);

        soundIdShot = soundPool.load(this, R.raw.sound1, 1);
        textView = findViewById(R.id.txt);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                textView.setText("Count = " + msg.what);
                isPlaying = true;
            }
        };
        Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);
    }

    public void onStartClick(View view) {
        String bpmValue = bpmInput.getText().toString();
        bpm = Integer.parseInt(bpmValue);

        startClick(bpm);
    }

    private void startClick(int bpmVal) {
        if (timer != null) {
            stopClick();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                play();
                handler.sendEmptyMessage(count);
                if(count++ % 4 == 0) {
                    stopClick();
                    // bpm+=5;
                    count = 1;
                    startClick(bpm);
                }
            }
        };

        if (bpm >= 400) return;

        textViewBpm.setText("Bpm = " + bpm);
        timer.schedule(task, 60000/bpm, 60000/bpm);
    }

    public void onCancelClick(View v) {
        stopClick();
    }

    private void stopClick() {
        timer.cancel();
        timer.purge();
        isPlaying = false;
        timer = null;
    }

    public void pause() {

    }

    private void play() {
        soundPool.play(soundIdShot, 1, 1,0,0,0);
    }

    private void count(int c) {
        System.out.println(c);
    }

    public void onIncreaseBpm(View view) {
        bpm+=1;

        if (isPlaying) startClick(bpm);
    }

    public void onDeccreaseBpm(View view) {
        bpm-=1;

        if (isPlaying) startClick(bpm);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }
}
