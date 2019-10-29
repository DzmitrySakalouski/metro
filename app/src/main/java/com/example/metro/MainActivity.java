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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    int count = 1;
    int bpm = 80;
    TextView textViewBpm;
    boolean isPlaying = false;
    RadioButton noIncrease;
    RadioGroup radioGroup;
    RadioGroup rgAddTempo;
    int increaseValueBar = 4;
    int increaseValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewBpm = findViewById(R.id.viewBpm);
        textViewBpm.setText("Bpm = " + bpm);

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
                String b = String.valueOf(count);

                textView.setText(b);
                if(count++ % increaseValueBar == 0) {
                    stopClick();
                    bpm += increaseValue;
                    count = 1;
                    startClick();
                }
            }
        };
        Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);
        noIncrease = findViewById(R.id.none);
        noIncrease.setChecked(true);

        radioGroup = findViewById(R.id.radioGroup);
        rgAddTempo = findViewById(R.id.rgAddTempo);

        rgAddTempo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radPlus1:
                        increaseValue = 1;
                        Toast.makeText(getApplicationContext(), "Первый переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radPlus2:
                        increaseValue = 2;
                        Toast.makeText(getApplicationContext(), "Второй переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radPlus3:
                        increaseValue = 3;
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radPlus4:
                        increaseValue = 4;
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radPlus5:
                        increaseValue = 5;
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bar1:
                        increaseValueBar = 4;
                        Toast.makeText(getApplicationContext(), "Первый переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bar2:
                        increaseValueBar = 8;
                        Toast.makeText(getApplicationContext(), "Второй переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bar3:
                        increaseValueBar = 12;
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bar4:
                        increaseValueBar = 16;
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.none:
                        Toast.makeText(getApplicationContext(), "Третий переключатель",
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public void onStartClick(View view) {
        startClick();
    }

    private void startClick() {
        if (timer != null) {
            stopClick();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                play();
                handler.sendEmptyMessage(count);

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

    private void play() {
        soundPool.play(soundIdShot, 1, 1,0,0,0);
    }

    public void onReset(View v) {
        if (timer != null) {
            stopClick();
        }
        bpm = 80;
        count = 0;
        String countStr = String.valueOf(count);
        textViewBpm.setText("Bpm = " + bpm);
        textView.setText(countStr);

        noIncrease.setChecked(true);
        rgAddTempo.clearCheck();
    }

    public void onIncreaseBpm(View view) {
        bpm+=1;
        textViewBpm.setText("Bpm = " + bpm);

        if (isPlaying) startClick();
    }

    public void onDeccreaseBpm(View view) {
        bpm-=1;
        textViewBpm.setText("Bpm = " + bpm);

        if (isPlaying) startClick();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }
}
