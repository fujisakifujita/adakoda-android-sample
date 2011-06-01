package com.adakoda.android.twitter4joauthsample;

import com.adakoda.android.twitter4j.OAuthActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Twitter4JOAuthSampleActivity extends Activity {
	
	private static final String TAG = "Twitter4JOAuthSampleActivity";
	private static final int REQUEST_OAUTH = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // OAuth画面呼び出し
        Intent intent = new Intent(this, OAuthActivity.class);
		intent.putExtra(OAuthActivity.CALLBACK, "http://www.adakoda.com/tokigamieru/callback");
		intent.putExtra(OAuthActivity.CONSUMER_KEY, "");
		intent.putExtra(OAuthActivity.CONSUMER_SECRET, "");
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