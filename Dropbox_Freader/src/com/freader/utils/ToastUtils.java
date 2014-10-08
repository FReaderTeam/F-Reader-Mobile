package com.freader.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void showToast(Context context, String msg, int toastLength){
		 Toast.makeText(context, msg, toastLength).show();
	}
	
	public static void showLongToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showShortToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
