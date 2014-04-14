package com.bionx.res;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bionx.res.about.InformationDrawer;
import com.bionx.res.activity.BionxWebs;
import com.bionx.res.activity.Preferences;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sbstrm.appirater.Appirater;

public class DashMainActivity extends FragmentActivity{
	
	private int mSelectedFragment;
	private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";
	private static String TAG= "DashMain";
	
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment);
    }
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_new);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		initCards();
        Appirater.appLaunched(this);
        
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "SegoeWP-Semilight.ttf");
		
		TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setTypeface(tf);
        TextView tv2 = (TextView) findViewById(R.id.textView4);
        tv2.setTypeface(tf);
        TextView tv3 = (TextView) findViewById(R.id.textView5);
        tv3.setTypeface(tf);
        TextView tv4 = (TextView) findViewById(R.id.textView6);
        tv4.setTypeface(tf);
        TextView tv5 = (TextView) findViewById(R.id.textView7);
        tv5.setTypeface(tf);
        TextView tv6 = (TextView) findViewById(R.id.textView8);
        tv6.setTypeface(tf);
        TextView tv7 = (TextView) findViewById(R.id.ads);
        tv7.setTypeface(tf);
        
		
		SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slide_menu);
	}
	{
        Log.d(TAG, "Bionx Initializing");
	}
	
	
	public void initCards(){
		init_nx_card();
		init_phan_card();
		init_xda_card();
		init_f_card();
		init_t_card();
		init_g_card();
		init_google_card();
		init_atf_card();
		init_resource_url1_card();
		init_resource_url2_card();
		init_about_card();
		init_ad_card1();
		init_ad_card2();
		init_ad_card3();
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
	 * Nx Industries Card
	 */
	private void init_nx_card() {
		Card card = new Card(getApplicationContext());
        CardHeader header = new CardHeader(getApplicationContext());
        CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setDrawableResource(R.drawable.ic_launcher2);
        header.setTitle("Nx Industries");
        header.setOtherButtonVisible(true);
        header.setOtherButtonDrawable(R.drawable.btn_exit);
        header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener() {
            @Override
			public void onButtonItemClick(Card card, View view) {
            	Toast.makeText(getApplicationContext(),
            	          "Bionx FTW!",
            	          Toast.LENGTH_SHORT).show();
            	          DashMainActivity.this.finish();
            }
        });
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
            	Intent webview = new Intent(getBaseContext(),
						BionxWebs.class);
				startActivity(webview);
            }
        });
        card.addCardHeader(header);
        card.addCardThumbnail(thumb);
        card.setTitle("www.bionx.webs");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card1);
        cardView.setCard(card);
	}
	
	/**
	 * Phandroid Card
	 */
	private void init_phan_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("Phandroid");
        CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setDrawableResource(R.drawable.phandroid);
		card.addCardThumbnail(thumb);
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent phan = new Intent("android.intent.action.VIEW", Uri
						.parse("http://www.phandroid.com/"));
				startActivity(phan);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Android Enthusiast");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card2);
        cardView.setCard(card);
    }
	
	/**
	 * XDA Card
	 */
	private void init_xda_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("XDA");
        CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setDrawableResource(R.drawable.xda);
		card.addCardThumbnail(thumb);
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent xda = new Intent("android.intent.action.VIEW", Uri
						.parse("http://www.xda-developers.com"));
				startActivity(xda);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Developers and Users");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card3);
        cardView.setCard(card);
    }
	/**
	 * ATF Card
	 */
	private void init_atf_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("ATF");
        CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setDrawableResource(R.drawable.atf);
		card.addCardThumbnail(thumb);
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent atf = new Intent("android.intent.action.VIEW", Uri
						.parse("http://www.androidtablets.net/"));
				startActivity(atf);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Android Tablet Forums, we speak tablets");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card5);
        cardView.setCard(card);
    }
	/**
	 * Facebook Card
	 */
	private void init_f_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("Facebook");
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent phan = new Intent("android.intent.action.VIEW", Uri
						.parse("https://www.facebook.com/pages/Bionx/133692896729322"));
				startActivity(phan);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Nx Industries on Facebook");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card11);
        cardView.setCard(card);
    }
	/**
	 * Twitter Card
	 */
	private void init_t_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("Twitter");
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent phan = new Intent("android.intent.action.VIEW", Uri
						.parse("https://twitter.com/Rgocal"));
				startActivity(phan);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Follow Biotic on Twitter");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card12);
        cardView.setCard(card);
    }
	/**
	 * Google+ Card
	 */
	private void init_g_card() {
		Card card = new Card(getApplicationContext());
        //Create a CardHeader
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("Google+");
		card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
				Intent phan = new Intent("android.intent.action.VIEW", Uri
						.parse("https://plus.google.com/u/0/100201209156317802250"));
				startActivity(phan);
            }
        });
        card.addCardHeader(header);
        card.setTitle("Join Biotic's circle on G+");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.ic_placeholder);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card13);
        cardView.setCard(card);
    }
	/**
	 * Google Card
	 */
	private void init_google_card() {
		Card card = new Card(getApplicationContext());
        CardHeader header = new CardHeader(getApplicationContext());
        header.setTitle("Only On Google Play");
        header.setOtherButtonVisible(true);
        header.setOtherButtonDrawable(R.drawable.proceed_button);
        header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener() {
            @Override
			public void onButtonItemClick(Card card, View view) {
				Intent store = new Intent("android.intent.action.VIEW", Uri
						.parse("https://play.google.com/store/apps/developer?id=Biotic"));
				startActivity(store);
            }
        });
        card.addCardHeader(header);
        card.setTitle("More apps developed by Biotic");
        card.setShadow(true);
        card.setBackgroundResourceId(R.drawable.google_play);
        //Set card in the cardView
        CardView cardView = (CardView) findViewById(R.id.card4);
        cardView.setCard(card);
    }
	/**
	 * Content View Card
	 */
	private void init_resource_url1_card() {
		Card card = new Card(getApplicationContext());
		CardHeader header = new CardHeader(getApplicationContext());
		card.setTitle("Update Coming Soon!");
		card.addCardHeader(header);
		CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setUrlResource("https://raw.github.com/rgocal/BionxDashboard4_Package/master/stream/notifications/icon_1.png");
		thumb.setErrorResource(R.drawable.warning);
		card.addCardThumbnail(thumb);
		card.setShadow(true);
		card.setBackgroundResourceId(R.drawable.ic_placeholder);
		CardView cardView = (CardView) findViewById(R.id.card6);
		cardView.setCard(card);
	}
	/**
	 * Content View Card
	 */
	private void init_resource_url2_card() {
		Card card = new Card(getApplicationContext());
		CardHeader header = new CardHeader(getApplicationContext());
		card.setTitle("New App Coming Soon!");
		card.addCardHeader(header);
		CardThumbnail thumb = new CardThumbnail(getApplicationContext());
		thumb.setUrlResource("https://raw.github.com/rgocal/BionxDashboard4_Package/master/stream/notifications/icon_2.png");
		thumb.setErrorResource(R.drawable.warning);
		card.addCardThumbnail(thumb);
		card.setShadow(true);
		card.setBackgroundResourceId(R.drawable.ic_placeholder);
		CardView cardView = (CardView) findViewById(R.id.card7);
		cardView.setCard(card);
	}

	/**
	 * About Card
	 */
	private void init_about_card() {
		Card card = new Card(getApplicationContext());
		CardHeader header = new CardHeader(getApplicationContext());
		header.setTitle("Information Center");
		card.setSwipeable(true);
		card.addCardHeader(header);
		card.setTitle("Swipe to view misc information about the dashboard");
		card.setShadow(true);
		card.setBackgroundResourceId(R.drawable.ic_placeholder);
		card.setOnSwipeListener(new Card.OnSwipeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onSwipe(Card arg0) {
				showDialog(11);
			}
        });
		CardView cardView = (CardView) findViewById(R.id.card10);
		cardView.setCard(card);
	}
	
	//Menu
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.dash_menu, menu);
      return super.onCreateOptionsMenu(menu);
    } 
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.settings:
    	  @SuppressWarnings("unused")
		final Context context = this;
    	  Intent settings_menu = new Intent(Settings.ACTION_SETTINGS);
			startActivity(settings_menu);
			return true;
      case R.id.info:
    	  new AlertDialog.Builder(this)
			.setTitle(R.string.info)
			.setMessage(Html.fromHtml(getString(R.string.info_msg)))
			.show();
			return true;
      case R.id.preference:
		@SuppressWarnings("unused")
		final Context context1 = this;
    	  Intent preference = new Intent(getBaseContext(), Preferences.class);
			startActivity(preference);
			return true;
      case R.id.exit:
          Toast.makeText(getApplicationContext(),
          "Bionx FTW!",
          Toast.LENGTH_SHORT).show();
          DashMainActivity.this.finish();
      }
      return false;
    }
    
@SuppressWarnings("deprecation")
@Override
protected Dialog onCreateDialog(int id) {
    switch (id) {    
        case 11:
        // Create our About Dialog
        TextView aboutMsg  = new TextView(this);
        aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
        aboutMsg.setPadding(30, 30, 30, 30);
        aboutMsg.setText(Html.fromHtml("To view more information about the development of the Bionx Dashboard, Continue into the Information Center, If you don't care much for license and authors, click 'Proceed'."));

        Builder builder = new AlertDialog.Builder(this);
            builder.setView(aboutMsg)
            .setTitle(Html.fromHtml("Dashboard <b><font color='" + getResources().getColor(R.color.holo_blue) + "'>Info</font></b>"))
            .setIcon(R.drawable.ic_launcher)
            .setCancelable(true)
            .setPositiveButton("Information Drawer",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                            int which) {
	                            Intent dialog_intent = new Intent(getBaseContext(), InformationDrawer.class);
	                            startActivity(dialog_intent);
                        }
                    })
            .setNegativeButton("Proceed",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                int which) {
                                Toast.makeText(getApplicationContext(),
                                "Bionx Dashboard",
                                Toast.LENGTH_LONG).show();
                        }
                    });
        return builder.create();
    }
    return super.onCreateDialog(id);
}
}