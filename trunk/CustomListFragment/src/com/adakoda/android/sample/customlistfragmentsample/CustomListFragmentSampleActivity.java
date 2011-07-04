package com.adakoda.android.sample.customlistfragmentsample;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomListFragmentSampleActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
			CustomListFragment fragment = new CustomListFragment();
			getFragmentManager().beginTransaction().add(android.R.id.content,
					fragment).commit();
		}
	}

	public static class CustomListFragment extends ListFragment {

		public static final String[] ITEMS = { "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "20" };

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// 1. 画面幅いっぱいにレイアウトされる
//			View view = inflater.inflate(R.layout.custom_listfragment, null);
			
			// 2. クラッシュする
//			View view = inflater.inflate(R.layout.custom_listfragment, container);
			
			// 3. 期待した幅でレイアウトされる
			View view = inflater.inflate(R.layout.custom_listfragment, container, false);
			
			// 4. クラッシュする
//			View view = inflater.inflate(R.layout.custom_listfragment, container, true); // 4->NG(Crash!)
			return view;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, ITEMS));
		}

	}

}