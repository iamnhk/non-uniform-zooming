package uh.interactive.fisheye;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.os.Bundle;
import android.view.*;
//import android.util.FloatMath;
import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
import android.util.Log;

public class FisheyeZoom {
	
	float xscale;
    float yscale;
    float xshift;
    float yshift;
    long getRadXStart = 0;
    long getRadXEnd = 0;
    long startSample = 0;
    float xr;
    float yr;
    float zoomRadius = 75;
  //  int [] s;
    
    private int [] s;
    private int [] scalar;
    private int [] s1;
    private int [] s2;
    private int [] s3;
    private int [] s4;
    
    float px;
	float py;
    
    private String TAG = "Filters";
    
	
		
	public Bitmap barrel (Bitmap input, float k,float cenx, float ceny, float rad){
        Log.e(TAG, "***********inside barrel method_2 ");
            
        s = new int[4];
        scalar = new int[4];
        s1 = new int[4];
        s2 = new int[4];
        s3 = new int[4];
        s4 = new int[4];
    
        int radius = (int)rad;
        float centerX = cenx;
        float centerY = ceny;
        int cHieght = (int)radius*2;

            
        
            
        Bitmap crop = Bitmap.createBitmap(input,Math.max((int)cenx-(int)radius,0),
        									Math.max((int)ceny-(int)radius,0),cHieght,cHieght);
        Log.e(TAG, "***********crop bitmap created ");
        int width = crop.getWidth(); //image bounds
        int height = crop.getHeight();
                          
        xshift = calc_shift(0,radius-1,radius,k);

        float newcenterX = width/2;
        float xshift_2 = calc_shift(0,newcenterX-1,newcenterX,k);

        yshift = calc_shift(0,radius-1,radius,k);

        float newcenterY = height/2;
        float yshift_2 = calc_shift(0,newcenterY-1,newcenterY,k);

        xscale = (width-xshift-xshift_2)/width;

        yscale = (height-yshift-yshift_2)/height;

            
        int origPixel = 0;
        int []arr = new int[(int)radius*(int)radius*4];
        int color = 0;

        int p = 0;
        int i = 0;
        int cW = crop.getWidth();
        int pixels[] = new int[cW];
        
        
        long startLoop = System.currentTimeMillis();
        Log.e(TAG, "***********about to loop through bm");
        
        //Bitmap dst2 = Bitmap.createBitmap((int)radius*2,(int)radius*2,crop.getConfig());
        for(int j=0;j<2*radius;j++){
        	//long sLoop = System.currentTimeMillis();  
        	crop.getPixels(pixels, 0, cW, 0, j, cW, 1);
           	for( i=0;i<2*radius;i++,p++){
           		//Log.e(TAG, "***********loop 2 through bm");
           		if(((i - radius)* (i - radius)+ (j - radius)*(j - radius))  <= radius*radius ){	  
              	             			
           			getRadialXY(j, i,radius, radius, k);
           			//Log.e(TAG, "radial xy");
           			sampleImage(crop, xr, yr);
                	color = ((s[0]&0x0ff)<<24)|((s[1]&0x0ff)<<16)|(((s[2]&0x0ff)<<8))|((s[3]&0x0ff));
                	//Log.e(TAG, "color "+ color +" color"+pixels[i]);
                	arr[p]= color;
           		}else{
                    arr[p]= pixels[i];
                }
            }
           //	long eLoop = System.currentTimeMillis();
           // Log.e(TAG, "1st loop "+(eLoop - sLoop)+"ms");
        }
        long endLoop = System.currentTimeMillis();
        long dur = endLoop - startLoop;
        Log.e(TAG, "loop took "+dur+"ms");

        Bitmap dst2 = Bitmap.createBitmap(arr,(int)radius*2,(int)radius*2,Bitmap.Config.ARGB_8888);        
        return dst2;
   }
	
	void sampleImage(Bitmap arr, float idx0, float idx1){
        
    	if(idx0<0 || idx1<0 || idx0>(arr.getHeight()-1) || idx1>(arr.getWidth()-1)){
          s[0]=0;
          s[1]=0;
          s[2]=0;
          s[3]=0;
          return;
     
    	}
    	
    	float idx0_fl=(float) Math.floor(idx0);
    	float idx0_cl=(float) Math.ceil(idx0);
    	float idx1_fl=(float) Math.floor(idx1);
    	float idx1_cl=(float) Math.ceil(idx1);

    	s1 = getARGB(arr,(int)idx0_fl,(int)idx1_fl);
    	s2 = getARGB(arr,(int)idx0_fl,(int)idx1_cl);
    	s3 = getARGB(arr,(int)idx0_cl,(int)idx1_cl);
    	s4 = getARGB(arr,(int)idx0_cl,(int)idx1_fl);

    	float x = idx0 - idx0_fl;
    	float y = idx1 - idx1_fl;

    	s[0]= (int) (s1[0]*(1-x)*(1-y) + s2[0]*(1-x)*y + s3[0]*x*y + s4[0]*x*(1-y));
    	s[1]= (int) (s1[1]*(1-x)*(1-y) + s2[1]*(1-x)*y + s3[1]*x*y + s4[1]*x*(1-y));
    	s[2]= (int) (s1[2]*(1-x)*(1-y) + s2[2]*(1-x)*y + s3[2]*x*y + s4[2]*x*(1-y));
    	s[3]= (int) (s1[3]*(1-x)*(1-y) + s2[3]*(1-x)*y + s3[3]*x*y + s4[3]*x*(1-y));
    	
    }
    
    float sqr(float x) {
    	return x * x;
    }
    float cube(float x) {
    	return x * x * x;
    }

    int [] getARGB(Bitmap buf,int x, int y){
    	int rgb = buf.getPixel(y, x); // Returns by default ARGB.
    	// int [] scalar = new int[4];
    	scalar[0] = (rgb >>> 24) & 0xFF;
    	scalar[1] = (rgb >>> 16) & 0xFF;
    	scalar[2] = (rgb >>> 8) & 0xFF;
    	scalar[3] = (rgb >>> 0) & 0xFF;
    	return scalar;
    }

    float getRadialX(float x,float y,float cx,float cy,float k){
    	x = (x*xscale+xshift);
    	y = (y*yscale+yshift);
    	float res = x+((x-cx)*k*((x-cx)*(x-cx)+(y-cy)*(y-cy)));
    	return res;
    }

    float getRadialY(float x,float y,float cx,float cy,float k){
    	x = (x*xscale+xshift);
    	y = (y*yscale+yshift);
    	float res = y+((y-cy)*k*((x-cx)*(x-cx)+(y-cy)*(y-cy)));
    	return res;
    }
    
    void getRadialXY(float x, float y, float cx, float cy, float k) {
    	//Log.e(TAG, "inside xy");
    	x = (x * xscale + xshift);
    	y = (y * yscale + yshift);
    	float f = k * (sqr(x - cx) + sqr(y - cy));
    	xr = x + ((x - cx) * f);
    	yr = y + ((y - cy) * f);
    }

    float thresh = 1;

    float calc_shift(float x1, float x2, float cx, float k) {

    	float x3 = x1 + (x2 - x1) * 0.5f;
		float res1 = x1 + cube(x1 - cx) * k ;

		if (-thresh < res1 && res1 < thresh) {
			return x1;
		}

		float res3 = x3 + cube(x3 - cx) * k;
		return (res3 < 0) 
            ? calc_shift(x3, x2, cx, k)
            : calc_shift(x1, x3, cx, k);
    }

}
