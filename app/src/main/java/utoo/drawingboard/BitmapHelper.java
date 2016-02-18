package utoo.drawingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapHelper {
	
	private static Options sBitmapOptions; 
	
	private static Options getBitmapOptions() {
		if (sBitmapOptions == null) {
			sBitmapOptions = new Options();
			sBitmapOptions.inPurgeable = true;
			sBitmapOptions.inInputShareable = true;
		}
		return sBitmapOptions;
	}

	public static Bitmap decodeFromRes(Context context, int resId){
    	if(resId == 0) return null;
    	InputStream inputStream = context.getResources().openRawResource(resId);
    	Bitmap returnBitmap = BitmapFactory.decodeStream(inputStream, null, getBitmapOptions());
    	try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return returnBitmap;
    }
	
	public static boolean saveBitmapToFile(Context context, Bitmap bitmap, String filePath) {
		return saveBitmapToFile(context, bitmap, filePath, 100);
	}
	
	public static boolean saveBitmapToFile(Context context, Bitmap bitmap, String filePath, int quality) {
		boolean isSuccess = false;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		try {
			File outputFile = new File(filePath);
			outputFile.createNewFile();
			FileOutputStream fo = new FileOutputStream(outputFile);
			fo.write(bytes.toByteArray());
			fo.close();
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    return inSampleSize;
	}
	
	public static Bitmap decodeFromFile(Context context, String filePath) {
		return decodeFromFile(context, filePath, 0, 0, 0);
	}
	
	public static Bitmap decodeFromFile(Context context, String filePath, int reqWidth, int reqHeight, int rotateAngle) {
		return decodeFromFile(context, filePath, reqWidth, reqHeight, rotateAngle, false);
	}
	
	public static Bitmap decodeFromFile(Context context, String filePath, int reqWidth, int reqHeight, int rotateAngle, boolean isReturnScaledBitmap) {
		if(TextUtils.isEmpty(filePath)) return null;
		Bitmap returnBitmap = null;
    	try {
    		final Options options = new Options();
    		if (reqWidth != 0 && reqHeight != 0) {
    			InputStream stream = new FileInputStream(filePath);
	    	    options.inJustDecodeBounds = true;
	    	    BitmapFactory.decodeStream(stream, null, options);
	    	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    	    options.inJustDecodeBounds = false;
	    	    stream.close();
    		}
    		InputStream inputStream = new FileInputStream(filePath);
    		options.inInputShareable = true;
    	    options.inPurgeable = true;
    		returnBitmap = BitmapFactory.decodeStream(inputStream, null, options);
    		
    		if (isReturnScaledBitmap) {
		        int height = returnBitmap.getHeight();
		        int width = returnBitmap.getWidth();
		        
		        if (height > reqHeight || width > reqWidth) {
			        double y = Math.sqrt((reqWidth*reqHeight) / (((double) width) / height));
			        double x = (y / height) * width;
	
			        Bitmap scaledBitmap = Bitmap.createScaledBitmap(returnBitmap, (int) x, (int) y, true);
			        returnBitmap.recycle();
			        returnBitmap = scaledBitmap;
		        }
			}
    		
    		if (rotateAngle != 0 ) {
    			Matrix matrix = new Matrix();
    			matrix.postRotate(rotateAngle);
    			Bitmap rotatedBitmap = Bitmap.createBitmap(returnBitmap, 0, 0, returnBitmap.getWidth(), returnBitmap.getHeight(), matrix, true);
    			returnBitmap.recycle();
    			returnBitmap = rotatedBitmap;
    		}
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        return returnBitmap;
	}
	
	public static int getImageOrientation(Context context, String imagePath){
		int orientation = 0;
		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	    return orientation;
	}
	
	public static int getAngleFromOrientation(int exifOrientation) {
		int angle = 0;
		switch (exifOrientation) {
		case ExifInterface.ORIENTATION_NORMAL:
			angle = 0;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			angle = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			angle = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			angle = 90;
			break;
		}
		return angle;
	}
	
}