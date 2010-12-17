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

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OAuthActivity extends Activity {

	public static final String REQUEST_TOKEN_ENDPOINT_URL = "request_token_endpoint_url";
	public static final String ACCESS_TOKEN_ENDPOINT_URL = "access_token_endpoint_url";
	public static final String AUTHORIZATION_WEBSITE_URL = "authorization_website_url";
	public static final String CONSUMER_KEY = "consumer_key";
	public static final String CONSUMER_SECRET = "consumer_secret";
	public static final String CALLBACK = "callback";
	public static final String TOKEN = "token";
	public static final String TOKEN_SECRET = "token_secret";
	private static final String OAUTH_VERIFIER = "oauth_verifier";

	private String mRequestTokenEndpointUrl;
	private String mAccessTokenEndpointUrl;
	private String mAuthorizationWebsiteUrl;
	private String mConsumerKey;
	private String mConsumerSecret;
	private String mCallback;
	private String mOAuthVerifier;
	private CommonsHttpOAuthProvider mOAuthProvider;
	private CommonsHttpOAuthConsumer mOAuthConsumer;

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		Intent intent = getIntent();
		mRequestTokenEndpointUrl = intent.getStringExtra(REQUEST_TOKEN_ENDPOINT_URL);
		mAccessTokenEndpointUrl = intent.getStringExtra(ACCESS_TOKEN_ENDPOINT_URL);
		mAuthorizationWebsiteUrl = intent.getStringExtra(AUTHORIZATION_WEBSITE_URL);
		mConsumerKey = intent.getStringExtra(CONSUMER_KEY);
		mConsumerSecret = intent.getStringExtra(CONSUMER_SECRET);
		mCallback = intent.getStringExtra(CALLBACK);
		if (((mRequestTokenEndpointUrl != null) && (mRequestTokenEndpointUrl.length() > 0)) && 
			 ((mAccessTokenEndpointUrl != null) && (mAccessTokenEndpointUrl.length() > 0)) &&
			 ((mAuthorizationWebsiteUrl != null) && (mAuthorizationWebsiteUrl.length() > 0)) &&
			 ((mConsumerKey != null) && (mConsumerKey.length() > 0)) &&
			 ((mConsumerSecret != null) && (mConsumerSecret.length() > 0)) &&
			 ((mCallback != null) && (mCallback.length() > 0))) {
			mWebView = new WebView(this);
			mWebView.clearCache(true);
			mWebView.setWebViewClient(mWebViewClient);
			mWebView.setWebChromeClient(mWebChromeClient);
			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setBuiltInZoomControls(true);
			setContentView(mWebView);
			new Thread(mStart).start();
		} else {
			finish();
		}
	}

	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if ((url != null) && (url.startsWith(mCallback))) {
				mWebView.stopLoading();
				mWebView.setVisibility(View.INVISIBLE);
				if (mOAuthVerifier == null) {
					Uri uri = Uri.parse(url);
					mOAuthVerifier = uri.getQueryParameter(OAUTH_VERIFIER);
					if (mOAuthVerifier != null) {
						new Thread(mEnd).start();
					} else {
						finish();
					}
				}
			} else {
				super.onPageStarted(view, url, favicon);
			}
		}
	};

	private WebChromeClient mWebChromeClient = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			OAuthActivity.this.setProgress(newProgress * 100);
		}
	};

	private Runnable mStart = new Runnable() {
		@Override
		public void run() {
			mOAuthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);
			mOAuthProvider = new CommonsHttpOAuthProvider(
					mRequestTokenEndpointUrl, mAccessTokenEndpointUrl, mAuthorizationWebsiteUrl);
			String url = null;
			try {
				url = mOAuthProvider.retrieveRequestToken(mOAuthConsumer, mCallback);
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			}
			if ((url != null) && (url.length() > 0)) {
				mWebView.loadUrl(url);
			} else {
				finish();
			}
		}
	};

	private Runnable mEnd = new Runnable() {
		@Override
		public void run() {
			setResult(RESULT_CANCELED);
			try {
				mOAuthProvider.retrieveAccessToken(mOAuthConsumer, mOAuthVerifier);
				String token = mOAuthConsumer.getToken();
				String tokenSecret = mOAuthConsumer.getTokenSecret();
				Intent intent = new Intent();
				if (((token != null) && (token.length() > 0)) &&
					 ((tokenSecret != null) && (tokenSecret.length() > 0))) {
					intent.putExtra(OAuthActivity.TOKEN, token);
					intent.putExtra(OAuthActivity.TOKEN_SECRET, tokenSecret);
					setResult(RESULT_OK, intent);
				}
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			}
			finish();
		}
	};

}
