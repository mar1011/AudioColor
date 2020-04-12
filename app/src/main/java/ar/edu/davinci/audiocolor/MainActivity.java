package ar.edu.davinci.audiocolor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    ImageView imageView2;
    Button colorPicker;
    Button info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
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










}
