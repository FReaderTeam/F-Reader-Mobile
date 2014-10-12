package ua.kharkiv.khnure.bookprototype;

import ua.kharkiv.knure.bookmodel.PagedBook;
import ua.kharkiv.knure.bookmodel.PagedBookListener;
import ua.kharkiv.knure.bookmodel.ParsedBook;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.freader.*;

public class ScreenSlideActivity extends FragmentActivity implements PagedBookListener{

	private CharSequence text;
	private int size;
	private int pagesNum;
	
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private SeekBar seekBar;
	private TextView progressTextView;
	private TextView mainTextTextView;
	private ParsedBook parsedBook;
	private PagedBook pagedBook;
	private ProgressBar progressBarLoading;
	
	public ScreenSlideActivity(ParsedBook parsedBook) {
		super();
		this.parsedBook = parsedBook;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		mPager = (ViewPager) findViewById(R.id.pager);
		// mPager.setPageTransformer(true, new ZoomOutPageTransformer()); //
		// animation
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		
		seekBar = (SeekBar)findViewById(R.id.seekBarProgress);
        progressTextView = (TextView)findViewById(R.id.textViewProgress);
        progressTextView.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        
        mainTextTextView = (TextView)findViewById(R.id.textViewMainText);
        size = mainTextTextView.getHeight() / mainTextTextView.getLineHeight();
        
        progressBarLoading = (ProgressBar)findViewById(R.id.progressBarLoading);
		
		parsedBook.setActivity(this);
		parsedBook.setTextView(mainTextTextView);
		parsedBook.initPages(size, mainTextTextView);
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ScreenSlidePageFragment(text);
		}

		@Override
		public int getCount() {
			return pagesNum;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return parsedBook.getAuthor() + " " + parsedBook.getTitle();
		}
	}

	@Override
	public void callback(PagedBook pb) {
		pagedBook = pb;
		pagesNum = pagedBook.getPagesNum();
		text = pagedBook.getCurrentPage();
		progressBarLoading.setVisibility(View.INVISIBLE);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            	seekBar.setProgress(position);
            	progressTextView.setText(position + "/" + pagedBook.getPagesNum());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
          });
        
        // go to page by number
        
        seekBar.setMax(pagedBook.getPagesNum());
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress,
        			boolean fromUser) {
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {        		
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		mPager.setCurrentItem(seekBar.getProgress());
        		progressTextView.setText(seekBar.getProgress() + "/" + pagedBook.getPagesNum());
        	}
        });
        progressTextView.setText("0/" + pagedBook.getPagesNum());
        seekBar.setVisibility(View.VISIBLE);
	}
}
