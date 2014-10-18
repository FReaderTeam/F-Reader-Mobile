package com.freader.bookprototype;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.freader.*;
import com.freader.bookmodel.ParsedBook;


import android.util.DisplayMetrics;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class ScreenSlidePageFragment extends Fragment {

	private CharSequence text;
	private TextView mainTextTextView;
	private View view;

	public ScreenSlidePageFragment(CharSequence text) {
		super();
		this.text = text;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_screen_slide_page, null);
		mainTextTextView = (TextView) view.findViewById(R.id.textViewMainText);
		mainTextTextView.setText(text);

		return view;
	}
}