package com.freader.bookprototype;

import java.util.ArrayList;
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
import android.util.Log;

import com.freader.*;
import com.freader.bookmodel.PagedBook;
import com.freader.bookmodel.PagedBookListener;
import com.freader.bookmodel.ParsedBook;
import java.util.HashMap;

public class ScreenSlideActivity extends FragmentActivity {

    private int numberOfPages;
    private String author;
    private String title;
    private ArrayList<String> att;
    private int numbersOfPageForProgressTextView;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private SeekBar seekBar;
    private TextView progressTextView;
    private TextView authorAndTitleTextView;
    private HashMap<Integer, Integer> paragraphs_to_pages;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("name");
        paragraphs_to_pages = (HashMap<Integer, Integer>) getIntent().getSerializableExtra("pHashMap");
        att = (ArrayList<String>) getIntent().getSerializableExtra("book");
        numberOfPages = getIntent().getIntExtra("pagesNumber", 0);
        numbersOfPageForProgressTextView = numberOfPages - 1;
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        seekBar = (SeekBar)findViewById(R.id.seekBarProgress);
        progressTextView = (TextView)findViewById(R.id.textViewProgress);
        authorAndTitleTextView = (TextView)findViewById(R.id.textViewAuthorAndTitle);
        authorAndTitleTextView.setText(author + " " + title);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            	
            	seekBar.setProgress(position);
            	progressTextView.setText(position + "/" + numbersOfPageForProgressTextView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
          });
        
        seekBar.setMax(numbersOfPageForProgressTextView);
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
        		progressTextView.setText(seekBar.getProgress() + "/" + numbersOfPageForProgressTextView);
        	}
        });
        progressTextView.setText("0/" + numbersOfPageForProgressTextView);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	return new ScreenSlidePageFragment(att.get(position));
        }

        @Override
        public int getCount() {
            return numberOfPages;
        }
    }
    
}
