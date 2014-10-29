package com.freader.bookprototype;

import java.util.ArrayList;
import java.util.HashMap;

import com.freader.R;
import com.freader.bookmodel.ParsedBook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.freader.bookmodel.*;

public class ScreenSlideWaiting extends FragmentActivity implements PagedBookListener{

	private static final String TITLE = "title";
	private static final String NAME = "name";
	private static final String P_HASH_MAP = "pHashMap";
	private static final String PATH = "path";
	private static final String DB_PATH = "dbPath";
	private static final String PAGES_NUMBER = "pagesNumber";
	private ParsedBook parsedBook;
	private TextView textViewForGetSize;
    private String author;
    private String title;
    private String bookFullPath;
    public String dbPath;
    private CharSequence text;
    private int size;
    
    private String fontSizeString;
    private SharedPreferences sp;
    private static final String FRAGMENT_FONT_SIZE = "fragmentFontSize";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_screen_slide);
		title = getIntent().getStringExtra(TITLE);
        author = getIntent().getStringExtra(NAME);
        ArrayList<String> paragraphs = PagesHolder.getInstance().getParagraphs();
        bookFullPath = getIntent().getStringExtra(PATH);
        dbPath = getIntent().getStringExtra(DB_PATH);
        
        sp = getSharedPreferences(FRAGMENT_FONT_SIZE, 
                Context.MODE_PRIVATE);
        String fontSizeString = getIntent().getStringExtra("fontSize");
        
        this.parsedBook = new ParsedBook(title, author, paragraphs);
        text = parsedBook.getFirstPages();
        textViewForGetSize = (TextView)findViewById(R.id.textViewForGetSize);
        textViewForGetSize.setText(text);
        
        Log.w("GHGHGHGHGH", fontSizeString + "");
        if(sp.contains(FRAGMENT_FONT_SIZE)) {
    		textViewForGetSize.setTextSize(Integer.parseInt(
					sp.getString(FRAGMENT_FONT_SIZE, String.valueOf(
							(int)textViewForGetSize.getTextSize()))));
		}
        if(fontSizeString != null){
        	textViewForGetSize.setTextSize(Integer.parseInt(fontSizeString));
        	Editor editor = sp.edit();
			editor.putString(FRAGMENT_FONT_SIZE, fontSizeString);
			editor.apply();
        }
        
        textViewForGetSize.setVisibility(View.INVISIBLE);
        parsedBook.setActivity(this);
        ViewTreeObserver vto = textViewForGetSize.getViewTreeObserver(); 
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @Override 
            public void onGlobalLayout() { 
                textViewForGetSize.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
                size = (int) Math.floor((textViewForGetSize.getHeight() / 
        				textViewForGetSize.getLineHeight())*0.86);                                        		
        		parsedBook.initPages(size, textViewForGetSize);
            }
        });
	}

	@Override
	public void callback(ArrayList<CharSequence> pages, HashMap<Integer, Integer> hm) {
		Intent intent = new Intent(this, ScreenSlideActivity.class);
		intent.putExtra(TITLE, title); 
		intent.putExtra(NAME, author);
		PagesHolder.getInstance().setPages(pages);
		intent.putExtra(PAGES_NUMBER, pages.size());
		intent.putExtra(P_HASH_MAP, hm);
		intent.putExtra(PATH, bookFullPath);
		intent.putExtra(DB_PATH, dbPath);
		startActivity(intent);
	}

}