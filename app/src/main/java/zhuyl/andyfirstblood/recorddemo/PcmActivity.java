package zhuyl.andyfirstblood.recorddemo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.*;

import static android.widget.Toast.LENGTH_SHORT;

public class PcmActivity extends AppCompatActivity {

    Button startPcmButton;
    Button stopPcmButton;
    Button playPcmButton;

    AudioRecord audioRecord;
    File file;
    Boolean isRecording = false;
    DataOutputStream dataOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcm);

        startPcmButton = (Button) findViewById(R.id.start_pcm);
        stopPcmButton = (Button) findViewById(R.id.stop_pcm);
        playPcmButton = (Button) findViewById(R.id.paly_pcm);

        startPcmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startPcm();
                    }
                });

                thread.start();
                Toast.makeText(PcmActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
                startPcmButton.setEnabled(false);
                stopPcmButton.setEnabled(true);
            }
        });

        stopPcmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = false;
                Toast.makeText(PcmActivity.this, "录音结束", LENGTH_SHORT).show();
                startPcmButton.setEnabled(true);
                stopPcmButton.setEnabled(false);
            }
        });
    }

    private void startPcm() {
        System.out.println("开始录音");
        int frequency = 16000;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pcm");
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);
            audioRecord.startRecording();
            isRecording = true;
            short[] buffer = new short[bufferSize];
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dataOutputStream.writeShort(buffer[i]);
                }
            }
            audioRecord.stop();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
