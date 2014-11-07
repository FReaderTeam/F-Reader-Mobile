package com.freader.bookprototype;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.freader.*;
import com.freader.bookmodel.PagesHolder;
import com.freader.bookmodel.ParsedBook;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

public class ScreenSlidePageFragment extends Fragment {

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String PATH = "path";
	private static final String DB_PATH = "dbPath";
	
	private CharSequence text;
	private TextView mainTextTextView;
	private View view;
	private int selectedFontIndex;
	private static final String FRAGMENT_FONT_SIZE = "fragmentFontSize";
	private SharedPreferences sp;
	private ScreenSlideActivity screenSlideActivity;
	
	private static float mScaleFactor = 25.0f;
	StringBuilder sb = new StringBuilder();
	int upPI = 0;
	int downPI = 0;
	boolean pinch = false;
	String result = "";
	private static double deltaPointerDown, deltaPointerMove;
	private static float textSize;
	
	public ScreenSlidePageFragment(CharSequence text, 
			ScreenSlideActivity screenSlideActivity) {
		super();
		this.text = text;
		this.screenSlideActivity = screenSlideActivity;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_screen_slide_page, null);
		mainTextTextView = (TextView) view.findViewById(R.id.textViewMainText);
		mainTextTextView.setText(text);
		sp = getActivity().getSharedPreferences(FRAGMENT_FONT_SIZE, 
                Context.MODE_PRIVATE);
		if(sp.contains(FRAGMENT_FONT_SIZE)) {
			mainTextTextView.setTextSize(Integer.parseInt(
					sp.getString(FRAGMENT_FONT_SIZE, String.valueOf(
							(int)mainTextTextView.getTextSize()))));
		}

		mainTextTextView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Builder adb = new AlertDialog.Builder(getActivity());
				adb.setTitle(R.string.select_font_size);
				final List<String> data = Arrays.asList("8", "9", "10", 
						"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", 
						"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", 
						"31", "32", "33", "34", "35", "36", "37", "38", "39", "40");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(),
						android.R.layout.select_dialog_singlechoice, data);
				if(sp.contains(FRAGMENT_FONT_SIZE)) {
					selectedFontIndex = Integer.parseInt(
							sp.getString(FRAGMENT_FONT_SIZE, String.valueOf(
									(int)mainTextTextView.getTextSize())));
				}
				adb.setSingleChoiceItems(adapter, selectedFontIndex-8, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedFontIndex = Integer.parseInt(data.get(which));
						Editor editor = sp.edit();
						editor.putString(FRAGMENT_FONT_SIZE, String.valueOf(data.get(which)));
						editor.apply();
					}
				});
				
				adb.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(screenSlideActivity, ScreenSlideWaiting.class);
						intent.putExtra(NAME, screenSlideActivity.getScreenSlideAuthor());
						intent.putExtra(TITLE, screenSlideActivity.getScreenSlideTitle());
						intent.putExtra(PATH, screenSlideActivity.getScreenSlidePath());
						intent.putExtra(DB_PATH, screenSlideActivity.getScreenSlideDbPath());
						intent.putExtra("fontSize", selectedFontIndex + "");
						startActivity(intent);
					}
				});
				adb.create().show();
				return false;
			}
		});

		textSize = mainTextTextView.getTextSize();
		return view;
	}

	public static float getFontSize(Activity activity) {
		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		double curScale = (deltaPointerDown/deltaPointerMove);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		float valueWide = (float) (WIDE / curScale / (dMetrics.scaledDensity));
		return valueWide;
	}
}