package com.adakoda.android.googleurlshortersample;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GoogleURLShorterSampleActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String apiUri = "https://www.googleapis.com/urlshortener/v1/url";
		// 以下の API Key を取得したものに置き換える（省略可）
		String apiKey = "";
		String postUrl = ""; // POST用URL文字列

		// 短縮元URL文字列
		String longUrl = "http://www.adakoda.com/";

		// パラメーターに日本語を含む場合は下記のようにエスケープしてください
		// Uri.Builder tmpUriBuilder = new Uri.Builder();
		// tmpUriBuilder("http://www.google.co.jp/search");
		// tmpUriBuilder.appendQueryParameter("q", Uri.encode("みっくみく"));
		// longUrl = Uri.decode(tmpUriBuilder.build().toString());

		// POST用URL文字列作成
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.path(apiUri);
		uriBuilder.appendQueryParameter("key", apiKey); // APIキー推奨
		postUrl = Uri.decode(uriBuilder.build().toString());

		try {
			// リクエスト作成
			HttpPost httpPost = new HttpPost(postUrl);
			httpPost.setHeader("Content-type", "application/json");
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("longUrl", longUrl);
			StringEntity stringEntity = new StringEntity(jsonRequest.toString());
			httpPost.setEntity(stringEntity);
			// リクエスト発行
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// 結果の取得
				String entity = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonEntity = new JSONObject(entity);
				if (jsonEntity != null) {
					// 短縮URL結果 （このサンプルの場合、「http://goo.gl/sGdK」）
					String shortUrl = jsonEntity.optString("id");
					Log.v("id", shortUrl);
					Toast.makeText(this, shortUrl, Toast.LENGTH_LONG).show();
				}
			}
		} catch (IOException e) {
		} catch (JSONException e) {
		}
	}
}