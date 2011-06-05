package com.adakoda.android.twitter4joauthsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.adakoda.android.twitter4j.OAuthActivity;

public class Twitter4JOAuthSampleActivity extends Activity {

	private static final String TAG = "Twitter4JOAuthSampleActivity";

	// CALLBACK、CONSUMER_KEY、CONSUMER_SECRET は書き変えてください
	// CALLBACKは WebViewでフックするためにhttp://で始める必要があります
	private static final String CALLBACK = "http://www.adakoda.com/hogehoge/callback";
	private static final String CONSUMER_KEY = "XXXXXXXXXX";
	private static final String CONSUMER_SECRET = "XXXXXXXXXX";

	private static final int REQUEST_OAUTH = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// OAuth画面呼び出し
		Intent intent = new Intent(this, OAuthActivity.class);
		intent.putExtra(OAuthActivity.CALLBACK, CALLBACK);
		intent.putExtra(OAuthActivity.CONSUMER_KEY, CONSUMER_KEY);
		intent.putExtra(OAuthActivity.CONSUMER_SECRET, CONSUMER_SECRET);
		startActivityForResult(intent, REQUEST_OAUTH);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_OAUTH:
			if (resultCode == RESULT_OK) {
				// OAuth結果取得
				long userId = data.getLongExtra(OAuthActivity.USER_ID, 0);
				String screenName = data.getStringExtra(OAuthActivity.SCREEN_NAME);
				String token = data.getStringExtra(OAuthActivity.TOKEN);
				String tokenSecret = data.getStringExtra(OAuthActivity.TOKEN_SECRET);
				// サンプルログ出力
				Log.v(TAG, String.valueOf(userId));
				Log.v(TAG, screenName);
				Log.v(TAG, token);
				Log.v(TAG, tokenSecret);
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}