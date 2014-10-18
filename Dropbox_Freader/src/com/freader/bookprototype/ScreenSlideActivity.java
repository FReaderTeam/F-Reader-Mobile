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

public class ScreenSlideActivity extends FragmentActivity {

	// Info Book.class
    private int numberOfPages;
    private String author;
    private String title;
    private CharSequence text;
    private ArrayList<String> att;
    
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private SeekBar seekBar;
    private TextView progressTextView;    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        //mPager.setPageTransformer(true, new ZoomOutPageTransformer()); // animation
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("name");
        att = (ArrayList<String>) getIntent().getSerializableExtra("book");
        text = att.get(0);
        numberOfPages = getIntent().getIntExtra("pagesNumber", 0);
        
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        
        
        
        seekBar = (SeekBar)findViewById(R.id.seekBarProgress);
        progressTextView = (TextView)findViewById(R.id.textViewProgress);
        
        
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            	
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            	seekBar.setProgress(position);
            	progressTextView.setText(position + "/" + numberOfPages);
            	text = att.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
          });
        
        // go to page by number
        
        seekBar.setMax(numberOfPages);
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
        		progressTextView.setText(seekBar.getProgress() + "/" + numberOfPages);
        		text = att.get(seekBar.getProgress());
        	}
        });
        progressTextView.setText("0/" + numberOfPages);
 
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
            return numberOfPages;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
        	return author + " " + title;
        }
    }
    
}
