package ua.kharkiv.khnure.bookprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.freader.*;

public class ScreenSlidePageFragment extends Fragment {

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
        mainTextTextView = (TextView)view.findViewById(R.id.textViewMainText);
        mainTextTextView.setText(text);
        return view;
    }
}