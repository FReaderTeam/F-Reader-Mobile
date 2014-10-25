package com.freader.bookprototype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

import com.dropbox.sync.android.DbxException;
import com.freader.*;
import com.freader.bookmodel.PagedBook;
import com.freader.bookmodel.PagedBookListener;
import com.freader.bookmodel.ParsedBook;
import com.freader.dao.PositionDao;

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
    private HashMap<Integer, Integer> paragraphsToPages;
    
    // save and load page
    private PositionDao positionDao;
    private String bookFullPath;
    private int firstPage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("name");
        paragraphsToPages = (HashMap<Integer, Integer>) getIntent().getSerializableExtra("pHashMap");
        bookFullPath = getIntent().getStringExtra("path");
        
        //att = (ArrayList<String>) getIntent().getSerializableExtra("book");
        att = AuthorizationActivity.arr;
        numberOfPages = getIntent().getIntExtra("pagesNumber", 0);
        numbersOfPageForProgressTextView = numberOfPages - 1;
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        seekBar = (SeekBar)findViewById(R.id.seekBarProgress);
        progressTextView = (TextView)findViewById(R.id.textViewProgress);
        authorAndTitleTextView = (TextView)findViewById(R.id.textViewAuthorAndTitle);
        authorAndTitleTextView.setText(author + " " + title);
        
        // save and load page
        
        try {
			positionDao = new PositionDao(AuthorizationActivity.dbxDatastoreManager);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			firstPage = paragraphsToPages.get(positionDao.getPosition(bookFullPath));
		} catch (DbxException e) {
			// TODO Auto-generated catch block
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
        progressTextView.setText(firstPage + "/" + numbersOfPageForProgressTextView);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	for (Map.Entry<Integer, Integer> entry : paragraphsToPages.entrySet())
        	{
        		if(position == entry.getValue()){
        			try {
						positionDao.savePosition(bookFullPath, entry.getKey());
					} catch (DbxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Long - Int??
        		}
        	    System.out.println(entry.getKey() + "/" + entry.getValue());
        	}
        	
        	return new ScreenSlidePageFragment(att.get(position));
        }

        @Override
        public int getCount() {
            return numberOfPages;
        }
    }
    
}
