/*
 * Copyright (C) 2010 adakoda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adakoda.android.hatenaoauthsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class HatenaOAuthSampleActivity extends Activity {

private static final int REQUEST_OAUTH = 1;

private static final String REQUEST_TOKEN_ENDPOINT_URL = "https://www.hatena.com/oauth/initiate";
private static final String ACCESS_TOKEN_ENDPOINT_URL = "https://www.hatena.com/oauth/token";
private static final String AUTHORIZATION_WEBSITE_URL = "https://www.hatena.ne.jp/touch/oauth/authorize";
private static final String CONSUMER_KEY = "";
private static final String CONSUMER_SECRET = "";
private static final String CALLBACK = "http://www.adakoda.com/android/hatenaoauthsample/callback";

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	Intent intent = new Intent(this, OAuthActivity.class);
	intent.putExtra(OAuthActivity.REQUEST_TOKEN_ENDPOINT_URL, REQUEST_TOKEN_ENDPOINT_URL);
	intent.putExtra(OAuthActivity.ACCESS_TOKEN_ENDPOINT_URL, ACCESS_TOKEN_ENDPOINT_URL);
	intent.putExtra(OAuthActivity.AUTHORIZATION_WEBSITE_URL, AUTHORIZATION_WEBSITE_URL);
	intent.putExtra(OAuthActivity.CONSUMER_KEY, CONSUMER_KEY);
	intent.putExtra(OAuthActivity.CONSUMER_SECRET, CONSUMER_SECRET);
	intent.putExtra(OAuthActivity.CALLBACK, CALLBACK);
	startActivityForResult(intent, REQUEST_OAUTH);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	switch (requestCode) {
	case REQUEST_OAUTH:
		if (resultCode == Activity.RESULT_OK) {
			String token = data.getStringExtra(OAuthActivity.TOKEN);
			String tokenSecret = data.getStringExtra(OAuthActivity.TOKEN_SECRET);
			new AlertDialog.Builder(this)
				.setMessage("[TOKEN]\n" + token + "\n" +
						"\n[TOKEN SECRET]\n" + tokenSecret)
				.create()
				.show();
		}
		break;
	}
	super.onActivityResult(requestCode, resultCode, data);
}
}