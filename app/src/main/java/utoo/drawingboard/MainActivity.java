package utoo.drawingboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_PHOTO_SRC = "EXTRA_PHOTO_SRC";
    public static String EXTRA_PHOTO_DST = "EXTRA_PHOTO_DST";

    private TouchDrawView mTouchDrawView;
    private String srcBitmapPath = "";
    private float bitmapScale = 0;
    private float viewWidth = 0;
    private float viewHeight = 0;

    private float edgeForView = 0.9f;
    public static Context context;
    private ImageView iv_pen;
    private ImageView iv_eraser;
    private Button red,green,blue,orange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        // Getting reference to the PaintView
        initView();

    }



    private void initView() {
        mTouchDrawView = (TouchDrawView)findViewById(R.id.drawing);
        iv_pen = (ImageView) findViewById(R.id.btn_pen);
        iv_eraser = (ImageView) findViewById(R.id.btn_eraser);
        red = (Button)findViewById(R.id.btn_red);
        green = (Button)findViewById(R.id.btn_green);
        blue = (Button)findViewById(R.id.btn_blue);
        orange = (Button)findViewById(R.id.btn_orange);
        //set surface background to bitmap
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        viewHeight = (dm.heightPixels-100)*edgeForView; //prepare for the image at the bottom of the screen which at least cost 50px
        viewWidth  = dm.widthPixels*edgeForView;
        // Get bitmap path from intent
       // srcBitmapPath = getIntent().getStringExtra(EXTRA_PHOTO_SRC);
        //prepare every thing for drawview
        mTouchDrawView.setPrepare("", viewWidth, viewHeight);
        initOnClick();

    }

    private void initOnClick(){
        iv_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.eraser = false;
            }
        });
        iv_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.resetCanvas();
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.setPaintColor(Color.RED);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.setPaintColor(Color.GREEN);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.setPaintColor(Color.BLUE);
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mTouchDrawView.setPaintColor(Color.YELLOW);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
