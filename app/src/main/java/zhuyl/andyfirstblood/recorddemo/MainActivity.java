package zhuyl.andyfirstblood.recorddemo;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.media.MediaRecorder.AudioSource.MIC;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private Boolean isRecoeding = false;
    private File file;
    MediaRecorder mediaRecorder;
    Button startAudio;
    Button stopAudio;
    Button pcmActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startAudio = (Button) findViewById(R.id.start_record);
        stopAudio = (Button) findViewById(R.id.end_record);
        pcmActivityButton = (Button) findViewById(R.id.start_pcm_record);

        startAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio();
            }
        });
        stopAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });

        pcmActivityButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PcmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void stopAudio() {
        if (isRecoeding) {
            mediaRecorder.stop();
            mediaRecorder.release();
            Toast.makeText(this, "结束", LENGTH_SHORT).show();
            startAudio.setEnabled(true);
            stopAudio.setEnabled(false);
        } else {
            Toast.makeText(this, "尚未开始录音", LENGTH_SHORT).show();
        }
    }

    private void startAudio() {
        mediaRecorder = new MediaRecorder();
        final String filePath = Environment.getExternalStorageDirectory() + "/aaaaa.mp3";

        mediaRecorder.setAudioSource(MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(filePath);

        try {
            isRecoeding = true;
            Toast.makeText(this, "开始", LENGTH_SHORT).show();

            mediaRecorder.prepare();
            mediaRecorder.start();
            startAudio.setEnabled(false);
            stopAudio.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
