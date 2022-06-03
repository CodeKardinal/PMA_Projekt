package com.example.pma_projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	Button btn, b11, b12, b13, b21, b22, b23, b31, b32, b33, b_reward;
	private static int[] BUTTONS;
	String xo = "O";
	String empty = "";
	int[][] Storage;
	boolean noWin = false;

	private AdView bAd_Bottom;
	private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
	private FrameLayout adContainerView;
	private AdView adView;

	private InterstitialAd interstitial;
	private RewardedAd mRewardedAd;
	int games = 1;

	private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
		// Checks network connection and changes the visibility of the reward button depending on it
		@Override
		public void onAvailable(@NonNull Network network) {
			super.onAvailable(network);
			try {
				b_reward.setVisibility(View.VISIBLE);
			}catch (Exception e){
			}
		}

		@Override
		public void onLost(@NonNull Network network) {
			super.onLost(network);
			try {
				b_reward.setVisibility(View.GONE);
			}catch (Exception e){
			}
		}

		@Override
		public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
			super.onCapabilitiesChanged(network, networkCapabilities);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		NetworkRequest networkRequest = new NetworkRequest.Builder()
				.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
				.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
				.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
				.build();

		ConnectivityManager connectivityManager =
				(ConnectivityManager) getSystemService(ConnectivityManager.class);
		connectivityManager.requestNetwork(networkRequest, networkCallback);

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		bAd_Bottom = findViewById(R.id.banner_bottom);

		BUTTONS = new int[]{
				R.id.button_11,
				R.id.button_12,
				R.id.button_13,
				R.id.button_21,
				R.id.button_22,
				R.id.button_23,
				R.id.button_31,
				R.id.button_32,
				R.id.button_33,
		};

		b11 = findViewById(BUTTONS[0]);
		b12 = findViewById(BUTTONS[1]);
		b13 = findViewById(BUTTONS[2]);
		b21 = findViewById(BUTTONS[3]);
		b22 = findViewById(BUTTONS[4]);
		b23 = findViewById(BUTTONS[5]);
		b31 = findViewById(BUTTONS[6]);
		b32 = findViewById(BUTTONS[7]);
		b33 = findViewById(BUTTONS[8]);

		b_reward = findViewById(R.id.button_reward);
		b_reward.setOnClickListener(v -> showRewardAd());
		adContainerView = findViewById(R.id.ad_view_container);

		b11.setOnClickListener(this);
		b12.setOnClickListener(this);
		b13.setOnClickListener(this);
		b21.setOnClickListener(this);
		b22.setOnClickListener(this);
		b23.setOnClickListener(this);
		b31.setOnClickListener(this);
		b32.setOnClickListener(this);
		b33.setOnClickListener(this);

		Storage = new int[3][3];

		if (games > 0) {
			b_reward.setVisibility(View.GONE);
		}

		try {
			Network currentNetwork = connectivityManager.getActiveNetwork();
			NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);

			if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
				b_reward.setVisibility(View.VISIBLE);
			} else {
				b_reward.setVisibility(View.GONE);
			}
		}
		catch(Exception e){
			b_reward.setVisibility(View.GONE);
		}
	}


	@Override
	protected void onStart() {
		super.onStart();

		// Waiting for adContainerView to be created to get the width
		adContainerView.post(new Runnable() {
			@Override
			public void run() {
				loadAdaptiveBannerAd();
			}
		});
		// Load and show BannerAd
		// Preload InterstitialAd and RewardAd
		loadBannerAdd(bAd_Bottom);
		loadInterAd();
		loadRewardAd();
	}


	private void loadRewardAd() {
		// Preload RewardAd
		FullScreenContentCallback fullScreenContentCallback =
				new FullScreenContentCallback() {
					@Override
					public void onAdDismissedFullScreenContent() {
						mRewardedAd = null;
					}
				};

		AdRequest adRequest = new AdRequest.Builder().build();

		RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
				adRequest, new RewardedAdLoadCallback() {
					@Override
					public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
						// mRewardedAd reference = null until ad is loaded
						mRewardedAd = rewardedAd;
					}

					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						mRewardedAd = null;
					}
				});
	}


	private void showRewardAd() {
		// Show preloaded RewardAd
		if (mRewardedAd != null) {
			Activity activityContext = MainActivity.this;
			mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
					// Handle Reward Item and set RewardButton invisible
					games = 3;
					b_reward.setVisibility(View.GONE);
				}
			});
		} else {
			b_reward.setVisibility(View.GONE);
			games = 1;
		}
		loadRewardAd();	//Preload new RewardAd after RewardAd was shown
	}


	private void loadInterAd() {
		// Preload InterstitialAd
		AdRequest adRequest = new AdRequest.Builder().build();

		InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
				new InterstitialAdLoadCallback() {
					@Override
					public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
						// mInterstitialAd reference = null until ad is loaded
						interstitial = interstitialAd;
					}

					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						interstitial = null;
					}
				});
	}


	private void showInterAd() {
		// Show Preloaded InterstitialAd
		if (interstitial != null) {
			interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {

				@Override
				public void onAdShowedFullScreenContent() {
					// Called when fullscreen content is shown
					// Reference set to null so Ad isn´t shown a second time
					interstitial = null;
				}
			});
			interstitial.show(MainActivity.this);
		}
	}


	private void loadAdaptiveBannerAd() {
		// Get ContainerView Size, set BannerAd size, show and load the BannerAd
		adView = new AdView(this);
		adView.setAdUnitId(AD_UNIT_ID);
		adContainerView.removeAllViews();
		adContainerView.addView(adView);

		AdSize adSize = getAdSize();
		adView.setAdSize(adSize);

		AdRequest adRequest = new AdRequest.Builder().build();


		adView.loadAd(adRequest);	// Loading ad in background
	}


	private AdSize getAdSize() {
		// Determine the screen width
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float density = outMetrics.density;

		float adWidthPixels = adContainerView.getWidth();

		// If the ad hasn't been laid out, default to the full screen width
		if (adWidthPixels == 0) {
			adWidthPixels = outMetrics.widthPixels;
		}

		int adWidth = (int) (adWidthPixels / density);
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}


	private void loadBannerAdd(AdView banner) {
		// Load and Show BannerAd
		AdRequest adRequest = new AdRequest.Builder().build();
		banner.loadAd(adRequest);

		banner.setAdListener(new AdListener() {
			@Override
			public void onAdFailedToLoad(LoadAdError adError) {
				// Code to be executed when an ad request fails.
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_11:
				if (input(1, 1)) {
					b11.setText(xo);
				}
				break;
			case R.id.button_12:
				if (input(1, 2)) {
					b12.setText(xo);
				}
				break;
			case R.id.button_13:
				if (input(1, 3)) {
					b13.setText(xo);
				}
				break;
			case R.id.button_21:
				if (input(2, 1)) {
					b21.setText(xo);
				}
				break;
			case R.id.button_22:
				if (input(2, 2)) {
					b22.setText(xo);
				}
				break;
			case R.id.button_23:
				if (input(2, 3)) {
					b23.setText(xo);
				}
				break;
			case R.id.button_31:
				if (input(3, 1)) {
					b31.setText(xo);
				}
				break;
			case R.id.button_32:
				if (input(3, 2)) {
					b32.setText(xo);
				}
				break;
			case R.id.button_33:
				if (input(3, 3)) {
					b33.setText(xo);
				}
				break;
		}
		if (checkEnd()) {
			gameFinish();
		}
	}


	private boolean input(int x, int y) {
		// Mapping the buttons into an array
		boolean fieldFree = false;
		x = x - 1;
		y = y - 1;

		if (Storage[x][y] == 0) {
			if (xo.equals("X")) {
				Storage[x][y] = 1;
				xo = "O";
			} else {
				Storage[x][y] = -1;
				xo = "X";
			}
			fieldFree = true;
		}
		return fieldFree;
	}


	private boolean checkEnd() {
		// Check if game has ended and return True or False
		int usedField = 0;
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				usedField = usedField + (Math.abs(Storage[x][y]));
			}
		}

		if (usedField == 9) {
			noWin = true;
		}

		return (Math.abs(Storage[0][0] + Storage[0][1] + Storage[0][2]) == 3
				|| Math.abs(Storage[1][0] + Storage[1][1] + Storage[1][2]) == 3
				|| Math.abs(Storage[2][0] + Storage[2][1] + Storage[2][2]) == 3
				|| Math.abs(Storage[0][0] + Storage[1][0] + Storage[2][0]) == 3
				|| Math.abs(Storage[0][1] + Storage[1][1] + Storage[2][1]) == 3
				|| Math.abs(Storage[0][2] + Storage[1][2] + Storage[2][2]) == 3
				|| Math.abs(Storage[0][0] + Storage[1][1] + Storage[2][2]) == 3
				|| Math.abs(Storage[0][2] + Storage[1][1] + Storage[2][0]) == 3)
				|| noWin;
	}


	private void gameFinish() {
		// Check who won and display Toast
		// Show InterstitialAd
		// Clear the game and restart
		if (noWin) {
			Toast.makeText(getApplicationContext().getApplicationContext(), "Unentschieden", Toast.LENGTH_LONG).show();
		} else if (xo.equals("X")) {
			Toast.makeText(getApplicationContext().getApplicationContext(), "X gewinnt", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext().getApplicationContext(), "O gewinnt", Toast.LENGTH_LONG).show();
		}
		if (games == 0) {
			showInterAd();
			loadInterAd();
			b_reward.setVisibility(View.VISIBLE);
		} else {
			games--;
		}
		clearField();
	}


	private void clearField(){
		for (int x = 0; x <= 2; x++) {
			for (int y = 0; y <= 2; y++) {
				Storage[x][y] = 0;
			}
		}
		for (int button : BUTTONS) {
			btn = findViewById(button);
			btn.setText(empty);
		}
	}
}