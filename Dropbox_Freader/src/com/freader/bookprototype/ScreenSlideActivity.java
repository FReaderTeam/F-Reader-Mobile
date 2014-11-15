package com.freader.bookprototype;

import java.util.ArrayList;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.dropbox.sync.android.DbxException;
import com.freader.*;

import com.freader.bookmodel.PagesHolder;
import com.freader.dao.PositionDao;
import com.freader.utils.DropboxUtils;

import java.util.HashMap;

public class ScreenSlideActivity extends FragmentActivity {

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String P_HASH_MAP = "pHashMap";
	private static final String PATH = "path";
	private static final String DB_PATH = "dbPath";
	private static final String PAGES_NUMBER = "pagesNumber";
	private int numberOfPages;
	private String author;
	private String title;
	private ArrayList<CharSequence> pages;
	private int numbersOfPageForProgressTextView;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private SeekBar seekBar;
	private TextView progressTextView;
	private TextView authorAndTitleTextView;
	private HashMap<Integer, Integer> paragraphsToPages;
	private String dbPath;
	private String path;
	private int firstPage;

	private ScreenSlideActivity screenSlideActivity;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		title = getIntent().getStringExtra(TITLE);
		author = getIntent().getStringExtra(NAME);
		paragraphsToPages = (HashMap<Integer, Integer>) getIntent()
				.getSerializableExtra(P_HASH_MAP);
		path = getIntent().getStringExtra(PATH);
		dbPath = getIntent().getStringExtra(DB_PATH);
		pages = PagesHolder.getInstance().getPages();
		numberOfPages = getIntent().getIntExtra(PAGES_NUMBER, 0);
		numbersOfPageForProgressTextView = numberOfPages;
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		seekBar = (SeekBar) findViewById(R.id.seekBarProgress);
		progressTextView = (TextView) findViewById(R.id.textViewProgress);
		authorAndTitleTextView = (TextView) findViewById(R.id.textViewAuthorAndTitle);

		seekBar.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		Log.w("freaderseekbar", " " + seekBar.getMeasuredHeight());
		progressTextView.measure(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED);
		Log.w("freaderprogress", " " + progressTextView.getHeight());

		authorAndTitleTextView.setText(author + " " + "\"" + title + "\"");
		try {
			int paragraphNumber = (int) PositionDao.getPosition(
					DropboxUtils.getDefaultDatastore(), dbPath);
			firstPage = paragraphsToPages.get(paragraphNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mPager.setCurrentItem(firstPage);
		seekBar.setProgress(firstPage);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				seekBar.setProgress(position);
				progressTextView.setText(position + 1 + "/"
						+ numbersOfPageForProgressTextView);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		seekBar.setMax(numbersOfPageForProgressTextView - 1);
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
				progressTextView.setText(seekBar.getProgress() + 1 + "/"
						+ numbersOfPageForProgressTextView);
			}
		});
		progressTextView.setText(firstPage + 1 + "/"
				+ numbersOfPageForProgressTextView);
		screenSlideActivity = this;
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			for (Map.Entry<Integer, Integer> entry : paragraphsToPages
					.entrySet()) {
				if (position == entry.getValue()) {
					try {
						PositionDao.savePosition(
								DropboxUtils.getDefaultDatastore(), dbPath,
								(long) entry.getKey());
					} catch (DbxException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			return new ScreenSlidePageFragment(pages.get(position),
					screenSlideActivity);
		}

		@Override
		public int getCount() {
			return numberOfPages;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, AuthorizationActivity.class);
		startActivity(intent);
	}

	public String getScreenSlideAuthor() {
		return author;
	}

	public String getScreenSlideTitle() {
		return title;
	}

	public String getScreenSlideDbPath() {
		return dbPath;
	}

	public String getScreenSlidePath() {
		return path;
	}

}
