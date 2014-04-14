package com.bionx.res.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bionx.res.DashMainActivity;
import com.bionx.res.R;

public class SplashActivity extends Activity {	
	
	private Thread thread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags
        (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
    	
        // Start animating the image
	    final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
	    splashImageView.setBackgroundResource(R.drawable.flag);
	    final AnimationDrawable frameAnimation = (AnimationDrawable)splashImageView.getBackground();
	    splashImageView.post(new Runnable(){
			@Override
			public void run() {
				frameAnimation.start();				
			}	    	
	    });
	        	
    	
    	final SplashActivity sPlashScreen = this;   
    	thread =  new Thread(){
    		@Override
    		public void run(){
    			try {
    				synchronized(this){
    					// Wait given period of time or exit on touch
    					wait(8000);
    				}
    			} 
    			catch(InterruptedException ex){    				
    			}
    			finish();
    			// Run next activity
    			Intent activity = new Intent();
    			activity.setClass(sPlashScreen, DashMainActivity.class);
    			startActivity(activity);    				
    		}
    	};
    	thread.start();
	}
	  
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
    	if(evt.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		synchronized(thread){
    			thread.notifyAll();
    		}
    	}
    	return true;
    }
}
