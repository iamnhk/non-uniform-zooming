package uh.interactive.fisheye;

import uh.interactive.fisheye.R;
import uh.interactive.fisheye.FisheyeZoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Zoom extends GraphicsActivity{
	
	private static final String TAG = "Touch";
	static FisheyeZoom obj = new FisheyeZoom();
	
	//Zoom Variables
	    
	
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {		   
		   super.onCreate(savedInstanceState);
	       setContentView(new SampleView(this));		   	      
	   }

	   private static class SampleView extends View  {
		   		   
		// These matrices will be used to move and zoom image
		   static Matrix matrix = new Matrix();
		   Matrix savedMatrix = new Matrix();
		   
		   

		   // We can be in one of these 3 states
		   static final int NONE = 0;
		   static final int DRAG = 1;
		   static final int ZOOM = 2;
		   int mode = NONE;

		   // Remember some things for zooming
		   PointF start = new PointF();
		   PointF mid = new PointF();
		   float oldDist = 1f;
		   float px;
		   float py;
		   float zoomRadius = 75;
		   
		   private Bitmap mBitmap = null, overlay = null;
		   private final Matrix mMatrix = new Matrix();
		   
		   ZoomState z[] = new ZoomState[100];
		   int zoomstate = 0;
		   
		   public SampleView(Context context) {
	            super(context);
	            setFocusable(true);
	            z[0] = new ZoomState();
	            mBitmap = BitmapFactory.decodeResource(getResources(),
	                                                     R.drawable.test1);
	            z[0].overlay = mBitmap;
	       		z[0].x = mBitmap.getWidth()/2;
	       		z[0].y = mBitmap.getHeight()/2;
	            
	            	            
	        }
		   
		   @Override protected void onDraw(Canvas canvas) {
		          
			   canvas.concat(mMatrix);
	           Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
	           mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
	           // mPaint.setColor(Color.TRANSPARENT);
	           // mPaint.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));

	            //mPaint
	           long sLoop = System.currentTimeMillis();
	           //canvas.drawBitmap(mBitmap, mMatrix, null);
	           // Canvas c2 = new Canvas(overlay);
	            //c2.drawCircle(75, 75, 80, mPaint);
	            
	           
	          // if(overlay != null && z!=null){
	        	   
	        	  // for(int i = 0;i<=zoomstate;i++){
	                // canvas.drawBitmap(z[zoomstate].overlay, px, py,mPaint);
	        		  // canvas.drawBitmap(z[zoomstate].overlay, z[zoomstate].x-zoomRadius, z[zoomstate].y-zoomRadius,mPaint);
	           canvas.drawBitmap(z[zoomstate].overlay,mMatrix,mPaint);
	        		   
	        	   //}
	           //}
	           long eLoop = System.currentTimeMillis();
	           Log.e(TAG, "draw bitmap "+(eLoop - sLoop)+"ms");
	        }
		   
		   @Override public boolean onTouchEvent(MotionEvent event) {
			      
			      // Handle touch events here...
			      switch (event.getAction() & MotionEvent.ACTION_MASK) {
			      case MotionEvent.ACTION_DOWN:
			    	  Log.e(TAG, "draw bitmap bm3");
			         savedMatrix.set(matrix);
			         start.set(event.getX(), event.getY());
			         Log.e(TAG, "mode=DRAG");
			         mode = DRAG;
			         break;
			      case MotionEvent.ACTION_POINTER_DOWN:
			         oldDist = spacing(event);
			         Log.d(TAG, "oldDist=" + oldDist);
			         if (oldDist > 10f) {
			            savedMatrix.set(matrix);
			            midPoint(mid, event);
			            mode = ZOOM;
			            Log.d(TAG, "mode=ZOOM");
			         }
			         break;
			      case MotionEvent.ACTION_UP:
			      case MotionEvent.ACTION_POINTER_UP:
			         mode = NONE;
			         Log.d(TAG, "mode=NONE");
			         break;
			      case MotionEvent.ACTION_MOVE:
			         if (mode == DRAG) {
			        	
			        	 /*px = event.getX()- start.x;
			        	 py = event.getY()- start.y;
			        	 invalidate();*/
			        			            
			         }
			         else if (mode == ZOOM) {
			            float newDist = spacing(event);
			            Log.d(TAG, "newDist=" + newDist);
			            			            
			            float scale = newDist / oldDist;
			            
			            if(scale>1){
			            
			            	zoomstate++;
			            	zoomRadius = oldDist/2;
			            	Log.e(TAG, "draw bitmap bm3");
			            	
			            	if((mid.x+zoomRadius)>mBitmap.getWidth()){
			                	mid.x = mBitmap.getWidth()- zoomRadius;     	
			                }
			                if((mid.x-zoomRadius)< 0){
			                	mid.x = zoomRadius;
			                }
			                if((mid.y+zoomRadius)> mBitmap.getHeight()){
			                	mid.y = mBitmap.getHeight()- zoomRadius;
			                }
			                if((mid.y-zoomRadius)< 0){
			                	mid.y = zoomRadius;
			                }
			            	overlay = obj.barrel(mBitmap, (float) 0.0002,mid.x,mid.y,zoomRadius);
			       		
			            	if(overlay != null){
			            		mBitmap = combineOverlay(mBitmap,overlay,mid.x,mid.y,zoomRadius);
			            	}
			            	z[zoomstate] = new ZoomState();
			            	z[zoomstate].overlay = mBitmap;
			            	z[zoomstate].x = mid.x;
			            	z[zoomstate].y = mid.y;
			       		
			            	px = mid.x;
			            	py = mid.y; 
			            	invalidate();
			            	
			            }else {
			            	if(zoomstate > 0)
			            	zoomstate--;
			            	mBitmap = z[zoomstate].overlay;
			            	invalidate();
			            }
			            	
			         }
			         
			         break;
			      }
			      //invalidate();
			      return true; // indicate event was handled
			   }
		   
		   		public static Bitmap combineOverlay(Bitmap bmp1, Bitmap bmp2,float x,float y, float zoomRadius) {
		   			
		   			Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
		   			Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		   			Canvas c = new Canvas(bmOverlay);
		   			c.drawBitmap(bmp1, new Matrix(), null);
		   			c.drawBitmap(bmp2, x-zoomRadius, y-zoomRadius, mPaint);
		   			return bmOverlay;
		   		}

			   /* Determine the space between the first two fingers */
			   private float spacing(MotionEvent event) {
			      float x = event.getX(0) - event.getX(1);
			      float y = event.getY(0) - event.getY(1);
			      return (FloatMath.sqrt(x * x + y * y));
			   }

			   /* Calculate the mid point of the first two fingers */
			   private void midPoint(PointF point, MotionEvent event) {
			      float x = event.getX(0) + event.getX(1);
			      float y = event.getY(0) + event.getY(1);
			      point.set(x / 2, y / 2);
			   }
	   }
    
}
