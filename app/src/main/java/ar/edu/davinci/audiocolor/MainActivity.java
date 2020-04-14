package ar.edu.davinci.audiocolor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.Image;
import android.media.MediaRecorder;
import android.media.MicrophoneInfo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.UserPresenceUnavailableException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


public class MainActivity extends AppCompatActivity
{
    private static final int MY_PERMISSIONS = 0;
    ImageView imageView2;
    Button colorPicker;
    Button info;
    Random random = new Random();

    private long lastPitch = 1;
    private long minPitch = 900;
    public byte[] lastColor;

    public AudioProcessor pitchProcessor;
    public AudioDispatcher dispatcher;
    public Thread audioThread;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        Bundle extras = getIntent().getExtras();
        startDispatch();
        if(extras!=null)
        {
            int r = extras.getInt("R");
            int g = extras.getInt("G");
            int b = extras.getInt("B");
            imageView2 = findViewById(R.id.background);
            imageView2.setBackgroundColor(Color.rgb(r, g, b));
        }

    }

    public void pantallaColor(View v){
        colorPicker =  findViewById(R.id.button);
        Intent intent = new Intent(MainActivity.this,colorActivity.class);
        startActivity(intent);
    }

    public void pantallaInfo(View v){
        info =  findViewById(R.id.button2);
        Intent intent = new Intent(MainActivity.this,infoActivity.class);
        startActivity(intent);
    }



    private void requestPermission()
    {
        if  (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS);

        }
    }



    private void startDispatch()
    {

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int pitch =  pitchInHz > 0 ? (int) pitchInHz : 1;
                        processPitch(pitch);
                        Log.d("Prueba",String.valueOf(pitch));
                    }
                });
            }
        };

        pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    private void processPitch(int pitch)
    {
        if(pitch > 1 )
        {
            int sensitive = 10;

            if((pitch - lastPitch) >= sensitive * 10)
            {
                byte[] rgb = getLedBytes(random.nextInt(600000000) + 50000);
                setBackgraound(rgb);
            }

            if(pitch == lastPitch)
            {
                byte xa = (byte) random.nextInt(256);
                byte xb = (byte) random.nextInt(256);

                   if (lastColor[1] > 100)
                   {
                        lastColor[2] = xa;
                        lastColor[3] = xb;
                   }
                   if (lastColor[2] > 100)
                   {
                       lastColor[1] = xa;
                       lastColor[3] = xb;
                   }
                if (lastColor[3] > 100)
                {
                    lastColor[1] = xa;
                    lastColor[2] = xb;
                }
                Log.d("lasColor","Si");
                setBackgraound(lastColor);
            }

            if(minPitch > pitch)
            {
                minPitch = pitch;
            }

        }

        lastPitch = pitch;
    }


    private byte[] getLedBytes(int newColor)
    {
        byte[] rgb = new byte[5];
        int color = (int)Long.parseLong(String.format("%06X", (0xFFFFFF & newColor)), 16);
        rgb[0] = (byte)0xA1;
        rgb[1] = (byte)((color >> 16) & 0xFF);
       rgb[2]= (byte)((color >> 8) & 0xFF);
        rgb[3] = (byte)((color) & 0xFF);
        return rgb;
    }

    private void setBackgraound(byte[] color)
    {
        imageView2 = findViewById(R.id.background);
        imageView2.setBackgroundColor(Color.rgb(color[1],color[2],color[3]));
        lastColor = color;
    }
}