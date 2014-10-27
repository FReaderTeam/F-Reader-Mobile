package com.freader.bookprototype;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.freader.*;
import com.freader.bookmodel.ParsedBook;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

public class ScreenSlidePageFragment extends Fragment implements
		OnTouchListener {

	private CharSequence text;
	private TextView mainTextTextView;
	private View view;
	private static float mScaleFactor = 25.0f;

	public ScreenSlidePageFragment(CharSequence text) {
		super();
		this.text = text;
	}

	ScaleGestureDetector mScaleDetector;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_screen_slide_page, null);
		mainTextTextView = (TextView) view.findViewById(R.id.textViewMainText);
		mainTextTextView.setText(text);

		mainTextTextView.setOnTouchListener(this);

textSize = mainTextTextView.getTextSize();

		return view;
	}

	StringBuilder sb = new StringBuilder();
	int upPI = 0;
	int downPI = 0;
	boolean pinch = false;
	String result = "";
	private static double deltaPointerDown, deltaPointerMove;
	private static float textSize;
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// событие
		int actionMask = event.getActionMasked();
		// индекс касания
		int pointerIndex = event.getActionIndex();
		// число касаний
		int pointerCount = event.getPointerCount();

		float deltaX, deltaY;
		switch (actionMask) {
		case MotionEvent.ACTION_DOWN:
			pinch = false;
			break;// первое касание
		case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
			pinch = true;
			deltaX = event.getX(0) - event.getX(1);
			deltaY = event.getY(0) - event.getY(1);
			deltaPointerDown = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
			Log.d("PINCH", "ACTION_POINTER_DOWN");
			break;
		case MotionEvent.ACTION_UP: // прерывание последнего касания
			pinch = false;
			break;
		case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
			pinch = false;
			Log.d("PINCH", "ACTION_POINTER_UP");
			break;

		case MotionEvent.ACTION_MOVE: // движение
			Log.d("PINCH", "ACTION_POINTER_MOVE");
			if (pinch) {
				deltaX = event.getX(0) - event.getX(1);
				deltaY = event.getY(0) - event.getY(1);
				deltaPointerMove = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
				float tempTextSize = getFontSize(getActivity())/textSize;
				if(Math.abs(tempTextSize/textSize)>=0.7||Math.abs(tempTextSize/textSize)<=0.2){
					mainTextTextView.setTextSize(tempTextSize);
					textSize = tempTextSize;
				}
			}
			break;
		}
		return true;
	}

	public static float getFontSize(Activity activity) {

		
		DisplayMetrics dMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);

		// lets try to get them back a font size realtive to the pixel width of
		// the screen
		double curScale = (deltaPointerDown/deltaPointerMove);
		final float WIDE = activity.getResources().getDisplayMetrics().widthPixels;
		float valueWide = (float) (WIDE / curScale / (dMetrics.scaledDensity));
		return valueWide;
	}
}