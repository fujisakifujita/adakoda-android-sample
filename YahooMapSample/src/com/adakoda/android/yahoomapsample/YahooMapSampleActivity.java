package com.adakoda.android.yahoomapsample;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import android.os.Bundle;

// 3. 「Yahoo!地図」を表示したいアクティビティを（jp.co.yahoo.android.maps.）MapActivity を継承して作成する
public class YahooMapSampleActivity extends MapActivity {
	private static final String APP_ID = "取得したアプリIDに書き変えてください！";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 5. MapView を（アプリケーションID渡しで）生成し、
		// getMapController() で取得した MapController で、表示位置などの設定をする
		MapView mapView = new MapView(this, APP_ID);
		MapController c = mapView.getMapController();
		c.setCenter(new GeoPoint(35632385, 139881695)); // 初期表示の地図を指定
		c.setZoom(3); // 初期表示の縮尺を指定
		mapView.setMapType(mapView.MapTypeSatellite); // 航空写真表示（オプショナル）
		// 6. MapActivity に MapView を設定し、表示させる
		setContentView(mapView);
	}
	// 4. MapActivity.isRouteDisplayed をオーバーライドする
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}