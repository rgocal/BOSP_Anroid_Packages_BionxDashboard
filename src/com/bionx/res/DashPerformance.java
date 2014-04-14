package com.bionx.res;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import com.bionx.res.activity.AndroidIDToolActivity;
import com.bionx.res.activity.BatteryCalibrationActivity;
import com.bionx.res.activity.BuildPropEditor;
import com.bionx.res.activity.PowerMenu;
import com.bionx.res.performance.PerformanceActivity;
import com.bionx.res.performance.SEED_Module;
import com.bionx.res.performance.VM_Module;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Utils;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DashPerformance extends FragmentActivity {
	
	private int mSelectedFragment;
	private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";
	private static String TAG= "PerformanceDashboard";
	private TextView mCharge;
	
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment);
}

public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_performance);
	
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);
	initCards();
	
	mCharge = (TextView) findViewById(R.id.charge_info);
    
    Typeface tf = Typeface.createFromAsset(getAssets(),
            "SegoeWP-Semilight.ttf");
    
    TextView tv1 = (TextView) findViewById(R.id.textView1);
    tv1.setTypeface(tf);
    TextView tv2 = (TextView) findViewById(R.id.TextView02);
    tv2.setTypeface(tf);
    TextView tv3 = (TextView) findViewById(R.id.textView2);
    tv3.setTypeface(tf);
    TextView tv4 = (TextView) findViewById(R.id.TextView03);
    tv4.setTypeface(tf);
    TextView tv5 = (TextView) findViewById(R.id.ads);
    tv5.setTypeface(tf);
	
	SlidingMenu menu = new SlidingMenu(this);
    menu.setMode(SlidingMenu.LEFT);
    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    menu.setShadowWidthRes(R.dimen.shadow_width);
    menu.setShadowDrawable(R.drawable.shadow);
    menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    menu.setFadeDegree(0.35f);
    menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
    menu.setMenu(R.layout.slide_menu);
}
{
    Log.d(TAG, "Modules Initializing");
}
private void initCards() {
	/**
	 * Performance Modules
	 */
	init_oc();
	init_vmm();
	init_seed();
	/**
	 * Utility Modules
	 */
	init_batt();
	init_adb();
	init_prop();
	init_power();
	/**
	 * Advertisments
	 */
	init_ad_card1();
	init_ad_card2();
	init_ad_card3();
	
}

@Override
public void onResume() {
	super.onResume();

	IntentFilter filter = new IntentFilter();
	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
	registerReceiver(mBatInfoReceiver, filter);
}

@Override
public void onPause() {
	super.onPause();
	unregisterReceiver(mBatInfoReceiver);
}

/**
 * Performance Modules
 */
private void init_oc() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("CPU Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
					PerformanceActivity.class);
			startActivity(oc);
        }
    });
    card.addCardHeader(header);
    card.setTitle("Central Processing Unit Overclocking");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.oc);
    cardView.setCard(card);
}

private void init_vmm() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("VMM Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
					VM_Module.class);
			startActivity(oc);
        }
    });
    card.addCardHeader(header);
    card.setTitle("Virtual Memory Settings");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.vmm);
    cardView.setCard(card);
}

private void init_seed() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("Seeder Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
					SEED_Module.class);
			startActivity(oc);
        }
    });
    card.addCardHeader(header);
    card.setTitle("Manage RGN Memory Usage");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.seed);
    cardView.setCard(card);
}

/**
 * Utility Modules
 */
private void init_batt() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("Battery Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
        			BatteryCalibrationActivity.class);
			startActivity(oc);
        }
    });
    card.addCardHeader(header);
    card.setTitle("Legacy Battery Stats Calibrator");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.batt);
    cardView.setCard(card);
}

private void init_adb() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("ADB Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
        			AndroidIDToolActivity.class);
			startActivity(oc);
        }
    });
	card.addCardHeader(header);
    card.setTitle("Android Debug Bridge Settings");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.adb);
    cardView.setCard(card);
}

private void init_prop() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("Build Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
        			BuildPropEditor.class);
			startActivity(oc);
        }
    });
	card.addCardHeader(header);
    card.setTitle("Build.prop Editor");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.prop);
    cardView.setCard(card);
}

private void init_power() {
	Card card = new Card(getApplicationContext());
    //Create a CardHeader
    CardHeader header = new CardHeader(getApplicationContext());
    header.setTitle("Power Module");
	card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
        	Intent oc = new Intent(getBaseContext(),
        			PowerMenu.class);
			startActivity(oc);
        }
    });
	card.addCardHeader(header);
    card.setTitle("Bionx Power Menu");
    card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    //Set card in the cardView
    CardView cardView = (CardView) findViewById(R.id.power);
    cardView.setCard(card);
}

/**
 * Admob Card1
 */
private void init_ad_card1() {
	Card card = new Card(getApplicationContext(),R.layout.admob_card_one);
	CardHeader header = new CardHeader(getApplicationContext());
	card.setSwipeable(true);
	card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    card.setOnSwipeListener(new Card.OnSwipeListener() {
		public void onSwipe(Card arg0) {
		}
    });
	CardView cardView = (CardView) findViewById(R.id.adCardone);
    cardView.setCard(card);
}

/**
 * Admob Card2
 */
private void init_ad_card2() {
	Card card = new Card(getApplicationContext(),R.layout.admob_card_two);
	CardHeader header = new CardHeader(getApplicationContext());
	card.setSwipeable(true);
	card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    card.setOnSwipeListener(new Card.OnSwipeListener() {
		public void onSwipe(Card arg0) {
		}
    });
	CardView cardView = (CardView) findViewById(R.id.adCardtwo);
    cardView.setCard(card);
}

/**
 * Admob Card3
 */
private void init_ad_card3() {
	Card card = new Card(getApplicationContext(),R.layout.admob_card_three);
	CardHeader header = new CardHeader(getApplicationContext());
	card.setSwipeable(true);
	card.setShadow(true);
    card.setBackgroundResourceId(R.drawable.ic_placeholder);
    card.setOnSwipeListener(new Card.OnSwipeListener() {
		public void onSwipe(Card arg0) {
		}
    });
	CardView cardView = (CardView) findViewById(R.id.adCardthree);
    cardView.setCard(card);
}


/**
 * Misc Services
 */

private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {
		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
				BatteryManager.BATTERY_STATUS_UNKNOWN);
		int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

		mCharge.setText("" + level + "% @ " + voltage + "mV");
	}
};
}
