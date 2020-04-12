package ar.edu.davinci.audiocolor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class colorActivity extends AppCompatActivity {

    ImageView mImageView;
    Button back;
    TextView mResultTv;
    View mColorView;
    int r, g, b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
    }

    public void back(View v){
        back =  findViewById(R.id.button3);
        Intent intent = new Intent(colorActivity.this,MainActivity.class);
        intent.putExtra("R",r);
        intent.putExtra("G",g);
        intent.putExtra("B",b);



        startActivity(intent);
    }

    public void colorIT (View v){
        mImageView = findViewById(R.id.imageView);
        mResultTv = findViewById(R.id.resultTv);
        mColorView = findViewById(R.id.colorView);


        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache(true);

        //image view on touche listener
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    Bitmap bitmap = mImageView.getDrawingCache();

                    int pixel = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());

                    //getting RGB values

                     r = Color.red(pixel);
                     g = Color.green(pixel);
                     b = Color.blue(pixel);

                    //getting Hex value

                    String hex = "#" + Integer.toHexString(pixel);

                    //setBackground color according to the picker color
                    mColorView.setBackgroundColor(Color.rgb(r,g,b));

                    //set RGB, HEX values to textview

                    mResultTv.setText(("RGB: "+ r + ", "+ g + ", " + b + "\nHEX: " + hex));
                }
                return true;
            }
        });

    }
}
