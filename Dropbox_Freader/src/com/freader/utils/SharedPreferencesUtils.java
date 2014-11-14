package com.freader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {

	private static final int DEFAULT_POSITION = 0;
	private static final String LAST_OPENED_BOOK = "last_opened_book";

	public static void savePosition(SharedPreferences sharedPreferences,
			int position) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(LAST_OPENED_BOOK, position);
		editor.apply();
	}

	public static int getPosition(SharedPreferences sharedPreferences) {
		return sharedPreferences.getInt(LAST_OPENED_BOOK, DEFAULT_POSITION);
	}

	public static SharedPreferences getSharedPreferences(Activity activity) {
		return activity.getSharedPreferences(activity.getClass().getName(),
				Context.MODE_PRIVATE);
	}

}
