package ua.kharkiv.khnure.bookprototype;

import ua.kharkiv.knure.bookmodel.PagedBook;
import ua.kharkiv.knure.bookmodel.PagedBookListener;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenSlideActivity extends FragmentActivity implements PagedBookListener{

	// Info Book.class
	private static final int NUM_PAGES = 20;
	private String author = "J. R. R. Tolkien";
	private String title = "Hobbit";

	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private SeekBar seekBar;
	private TextView progressTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		mPager = (ViewPager) findViewById(R.id.pager);
		// mPager.setPageTransformer(true, new ZoomOutPageTransformer()); //
		// animation
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				seekBar.setProgress(position);
				progressTextView.setText(position + "/" + NUM_PAGES);

				final TextView tv = (TextView) findViewById(R.id.a10);
				{
					
					//SpannableString s = new SpannableString("aaa");
					//s.setSpan(new ForegroundColorSpan(0xFFFF0000), 0, 2, 0);
					//tv.setText(s);

				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		// go to page by number
		seekBar = (SeekBar) findViewById(R.id.seekBarProgress);
		seekBar.setMax(NUM_PAGES);
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
				progressTextView.setText(seekBar.getProgress() + "/"
						+ NUM_PAGES);
			}
		});

		progressTextView = (TextView) findViewById(R.id.textViewProgress);
		progressTextView.setText("0/" + NUM_PAGES);
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ScreenSlidePageFragment();
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return author + " " + title;
		}
	}

	@Override
	public void callback(PagedBook pb) {
		// TODO Auto-generated method stub
		
	}
}
