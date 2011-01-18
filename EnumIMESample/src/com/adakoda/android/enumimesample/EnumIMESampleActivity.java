package com.adakoda.android.enumimesample;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public class EnumIMESampleActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		List<InputMethodInfo> inputMethodInfoList = imm
				.getEnabledInputMethodList();
		int inputMethodInfoListSize = inputMethodInfoList.size();
		for (int i = 0; i < inputMethodInfoListSize; ++i) {
			InputMethodInfo inputMethodInfo = inputMethodInfoList.get(i);
			CharSequence label = inputMethodInfo.loadLabel(getPackageManager());
			Log.v("label", String.valueOf(label));
		}
	}
}