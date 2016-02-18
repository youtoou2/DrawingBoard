package utoo.drawingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class TouchDrawView extends View {
    private Bitmap  bgBitmap;
    private Bitmap  pathBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint 	mPathPaint;
    private Paint 	mPathEraser;
    private String imagePath;
    private ArrayList<Point> touchedPoint;
	private float viewHeight;
	private float viewWidth;
	private float bitmapScale;
    public boolean eraser = false;
    private boolean isNewPath = true;
    public TouchDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public TouchDrawView(Context context){
    	super(context);


    }
  
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);
        mPathPaint.setColor(Color.GRAY);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeWidth(30);
        mPathEraser = new Paint();
        mPathEraser.setAntiAlias(true);
        mPathEraser.setDither(true);
        mPathEraser.setStyle(Paint.Style.STROKE);
        mPathEraser.setStrokeJoin(Paint.Join.ROUND);
        mPathEraser.setStrokeCap(Paint.Cap.ROUND);
        mPathEraser.setStrokeWidth(40);
        mPathEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPathEraser.setAlpha(0);
        mPath = new Path();
        bgBitmap = Bitmap.createBitmap(w,h,Config.ARGB_8888);
        pathBitmap = Bitmap.createBitmap(w,h,Config.ARGB_8888);
        mCanvas = new Canvas(pathBitmap);
        
        touchedPoint = new ArrayList<Point>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
       		canvas.drawBitmap(pathBitmap, 0, 0, mPathPaint);
           	canvas.drawPath(mPath, mPathPaint);
 
        super.dispatchDraw(canvas);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
    	
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2); 
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
       mCanvas.drawPath(mPath, mPathPaint);
        // kill this so we don't double draw
       mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Point point = new Point();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	point.x = (int)x;
            	point.y = (int)y;
            	setPoint(point);
            	isNewPath = false;
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
            	point.x = (int)x;
            	point.y = (int)y;
            	setPoint(point);
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            	point.x = (int)x;
            	point.y = (int)y;
            	setPoint(point);
            	isNewPath = true;
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
    
    private void setPoint(Point point){
    	if (isNewPath == true){
    		if(eraser == false){
    		touchedPoint.add(new Point(0,0));
    		}else{
    		touchedPoint.add(new Point(-1,-1));
    		}
    	}
    	touchedPoint.add(point);
    }

    //method
	//asyn task
    /*
private class getResizedImageFromPathTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			System.gc();
			String imagePath = params[0];
			int exifOrientation = BitmapHelper.getImageOrientation(CarPhotoModifierActivity.context, imagePath);
			Bitmap originalBitmap = BitmapHelper.decodeFromFile(CarPhotoModifierActivity.context, imagePath,AppConfig.MAX_UPLOAD_IMG_DIMEN, AppConfig.MAX_UPLOAD_IMG_DIMEN,BitmapHelper.getAngleFromOrientation(exifOrientation),true);
			if ((float)originalBitmap.getWidth()/viewWidth >= (float)originalBitmap.getHeight()/viewHeight){
			bitmapScale = (float)originalBitmap.getWidth()/viewWidth;}
			else{
			bitmapScale = (float)originalBitmap.getHeight()/viewHeight;}
			int newWidth =Math.round((float)originalBitmap.getWidth()/bitmapScale);
			int newHeight = Math.round((float)originalBitmap.getHeight()/bitmapScale);
			bgBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
			pathBitmap = Bitmap.createBitmap(newWidth, newHeight, bgBitmap.getConfig());

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setViewSize(bgBitmap.getWidth(),bgBitmap.getHeight());
			setBgBitmap();
		}

	} */
    
	   //getter setter
		private boolean setViewSize(int width,int height){
			LayoutParams params = (LayoutParams) this.getLayoutParams();
			params.height = (int) height;
		    params.width = (int) width;
		    this.setLayoutParams(params);
			return true;
		}
		@SuppressWarnings("deprecation")
		private void setBgBitmap(){
	        mCanvas = new Canvas(pathBitmap);
	        Drawable drawable = new BitmapDrawable(getResources(),bgBitmap);
	        this.setBackgroundDrawable(drawable);
	    }
		public ArrayList<Point> getPoint(){
		    	return touchedPoint;
		}
		
		public float getBitmapScale(){
			return bitmapScale;
		}
		
		public Paint getPaint(){
			return mPathPaint;
		}
		public Paint getEraser(){
			return mPathEraser;
		}
		public void setPrepare(String imPath, float width, float height){
			imagePath = imPath;
			viewHeight = height;
			viewWidth = width;
            setViewSize((int)width,(int)height);
	       	//new getResizedImageFromPathTask().execute(imagePath);
	        // Instantiating the thread
		}
		
		public void resetCanvas(){
			touchedPoint.clear();
			mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			invalidate();
		}

    public void setPaintColor(int color){
        mPathPaint.setColor(color);
    }
		
		
		
}