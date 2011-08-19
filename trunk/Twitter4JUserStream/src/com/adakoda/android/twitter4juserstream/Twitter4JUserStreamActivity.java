package com.adakoda.android.twitter4juserstream;

import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Twitter4JUserStreamActivity extends Activity {

	private MyUserStreamAdapter mMyUserStreamAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mMyUserStreamAdapter = new MyUserStreamAdapter();

		// ここは別途OAuth認証して情報を取得する。。。
		String oAuthConsumerKey = "あなたのTwitterクライアントで取得したものに置き換えてください";
		String oAuthConsumerSecret = "あなたのTwitterクライアントで取得したものに置き換えてください";
		String oAuthAccessToken = "あなたのTwitterクライアントで認証したユーザーの情報に置き換えてください";
		String oAuthAccessTokenSecret = "あなたのTwitterクライアントで認証したユーザーの情報に置き換えてください";

		// Twitter4Jに対してOAuth情報を設定
		ConfigurationBuilder builder = new ConfigurationBuilder();
		{
			// アプリ固有の情報
			builder.setOAuthConsumerKey(oAuthConsumerKey);
			builder.setOAuthConsumerSecret(oAuthConsumerSecret);
			// アプリ＋ユーザー固有の情報
			builder.setOAuthAccessToken(oAuthAccessToken);
			builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		}

		// 1. TwitterStreamFactory をインスタンス化する
		Configuration conf = builder.build();
		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
		// 2. TwitterStream をインスタンス化する
		TwitterStream twitterStream = twitterStreamFactory.getInstance();
		
		// ユーザーストリーム操作
		{
			// 4. TwitterStream に UserStreamListener を実装したインスタンスを設定する
			twitterStream.addListener(mMyUserStreamAdapter);
			// 5. TwitterStream#user() を呼び出し、ユーザーストリームを開始する
			twitterStream.user();
		}
	}
	
	// 3. UserStream 受信時に応答する（UserStreamListener）リスナーを実装する
	class MyUserStreamAdapter extends UserStreamAdapter {

		// 新しいツイート（ステータス）を取得する度に呼び出される
		@Override
		public void onStatus(Status status) {
			super.onStatus(status);
			// 6. UserStream 受信時、3 で実装したメソッドが呼び出されるので必要な処理をする
			// サンプルログ出力
			Log.v("Twitter4JUserStreamActivity", status.getText());
			// ここではサンプルとして通知発行メソッドを呼び出している
			Twitter4JUserStreamActivity.notify(Twitter4JUserStreamActivity.this,
					status.getId(), status.getText(),
					status.getUser().getId(), status.getUser().getScreenName());
		}
	}

	// おまけ：ツイート内容から通知を発行する
	private static void notify(Context context, long statusId,
			String statusText, long userId, String userScreenName) {
		// NotificationManager取得
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification構築
		Notification notification = new Notification(R.drawable.icon,
				statusText, System.currentTimeMillis());
		// 通知をタップした時に起動するペンディングインテント
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				// ウェブのURLを処理するアプリを起動する
				new Intent(Intent.ACTION_VIEW,
						// 通知で表示されているツイートのURL
						Uri.parse("http://twitter.com/#!/" + userId + "/status/" + statusId)),
						Intent.FLAG_ACTIVITY_NEW_TASK);
		// 通知に表示する内容を設定
		notification.setLatestEventInfo(context, statusText, userScreenName, contentIntent);
		// 通知を発行
		nm.notify(0, notification);
	}

}
