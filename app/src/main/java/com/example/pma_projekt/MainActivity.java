package com.example.pma_projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
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

	Button b11, b12, b13, b21, b22, b23, b31, b32, b33, b_reward;
	String xo = "O";
	int[][] Storage;
	boolean noWin = false;

	private AdView bAd_Bottom;
	private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
	private FrameLayout adContainerView;
	private AdView adView;

	private InterstitialAd interstitial;
	private RewardedAd mRewardedAd;
	int games;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		games = intent.getIntExtra("games", 0);


		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});
		bAd_Bottom = findViewById(R.id.banner_bottom);

		b11 = findViewById(R.id.button_11);
		b12 = findViewById(R.id.button_12);
		b13 = findViewById(R.id.button_13);
		b21 = findViewById(R.id.button_21);
		b22 = findViewById(R.id.button_22);
		b23 = findViewById(R.id.button_23);
		b31 = findViewById(R.id.button_31);
		b32 = findViewById(R.id.button_32);
		b33 = findViewById(R.id.button_33);

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

		// Since we're loading the banner based on the adContainerView size, we need to wait until this
		// view is laid out before we can get the width.
		adContainerView.post(new Runnable() {
			@Override
			public void run() {
				loadAdaptiveBannerAd();
			}
		});
		loadBannerAdd(bAd_Bottom);
		loadInterAd();
		loadRewardAd();


	}


	private void loadRewardAd() {
		FullScreenContentCallback fullScreenContentCallback =
				new FullScreenContentCallback() {
					@Override
					public void onAdShowedFullScreenContent() {
						// Code to be invoked when the ad showed full screen content.
					}

					@Override
					public void onAdDismissedFullScreenContent() {
						mRewardedAd = null;
						// Code to be invoked when the ad dismissed full screen content.
					}
				};

		AdRequest adRequest = new AdRequest.Builder().build();

		RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
				adRequest, new RewardedAdLoadCallback() {
					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						// Handle the error.
						mRewardedAd = null;
					}

					@Override
					public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
						mRewardedAd = rewardedAd;
					}
				});

	}

	private void showRewardAd() {
		if (mRewardedAd != null) {
			Activity activityContext = MainActivity.this;
			mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
					// Handle the reward.
					//int rewardAmount = rewardItem.getAmount();
					//String rewardType = rewardItem.getType();
					games = 3;
					b_reward.setVisibility(View.GONE);
				}
			});
		} else {
			b_reward.setVisibility(View.GONE);
			games = 1;
		}
	}


	private void loadInterAd() {
		AdRequest adRequest = new AdRequest.Builder().build();

		InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
				new InterstitialAdLoadCallback() {
					@Override
					public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
						// The mInterstitialAd reference will be null until
						// an ad is loaded.
						interstitial = interstitialAd;
					}

					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						// Handle the error
						interstitial = null;
					}
				});
	}

	private void showInterAd() {
		if (interstitial != null) {
			interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
				@Override
				public void onAdDismissedFullScreenContent() {
					// Called when fullscreen content is dismissed.
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}

				@Override
				public void onAdFailedToShowFullScreenContent(AdError adError) {
					// Called when fullscreen content failed to show.
				}

				@Override
				public void onAdShowedFullScreenContent() {
					// Called when fullscreen content is shown.
					// Make sure to set your reference to null so you don't
					// show it a second time.
					interstitial = null;
				}
			});
			interstitial.show(MainActivity.this);
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	private void loadAdaptiveBannerAd() {
		// Create an ad request.
		adView = new AdView(this);
		adView.setAdUnitId(AD_UNIT_ID);
		adContainerView.removeAllViews();
		adContainerView.addView(adView);

		AdSize adSize = getAdSize();
		adView.setAdSize(adSize);

		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}

	private AdSize getAdSize() {
		// Determine the screen width (less decorations) to use for the ad width.
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float density = outMetrics.density;

		float adWidthPixels = adContainerView.getWidth();

		// If the ad hasn't been laid out, default to the full screen width.
		if (adWidthPixels == 0) {
			adWidthPixels = outMetrics.widthPixels;
		}

		int adWidth = (int) (adWidthPixels / density);
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}


	private void loadBannerAdd(AdView banner) {
		AdRequest adRequest = new AdRequest.Builder().build();
		banner.loadAd(adRequest);

		banner.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(LoadAdError adError) {
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdClicked() {
				// Code to be executed when the user clicks on an ad.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the user is about to return
				// to the app after tapping on an ad.
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
		if (noWin) {
			Toast.makeText(getApplicationContext().getApplicationContext(), "Unentschieden", Toast.LENGTH_LONG).show();
		} else if (xo.equals("X")) {
			Toast.makeText(getApplicationContext().getApplicationContext(), "X gewinnt", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext().getApplicationContext(), "O gewinnt", Toast.LENGTH_LONG).show();
		}
		if (games == 0) {
			showInterAd();
		} else {
			games--;
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("games", games);
			startActivity(intent);
		}
		this.finish();
	}
}