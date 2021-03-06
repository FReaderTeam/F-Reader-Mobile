package com.freader.bookprototype;

import java.util.ArrayList;

import com.freader.R;
import com.freader.bookmodel.PagedBook;
import com.freader.bookmodel.ParsedBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.freader.bookmodel.*;

public class ScreenSlideWaiting extends FragmentActivity implements PagedBookListener{

	private TextView textViewForGetSize;
	private ParsedBook parsedBook;
    private String author;
    private String title;
    private CharSequence text;
    private int size;
    private String bookFullPath;
    private String dropboxPath;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("SSW","SSW");
        setContentView(R.layout.waiting_screen_slide);
		title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("name");
        dropboxPath = getIntent().getStringExtra("dropbox_path");
        ArrayList<String> paragraphs = PagesHolder.getInstance().getData();
        bookFullPath = getIntent().getStringExtra("path");
        this.parsedBook = new ParsedBook(title, author, paragraphs);
        text = parsedBook.getFirstPages();
        textViewForGetSize = (TextView)findViewById(R.id.textViewForGetSize);
        textViewForGetSize.setText(text);
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
	public void callback(PagedBook pb) {
		Intent intent = new Intent(this, ScreenSlideActivity.class);
		intent.putExtra("title", pb.getTitle());
		intent.putExtra("name", pb.getAuthor());
		PagesHolder.getInstance().setData(pb.getPages());
		intent.putExtra("pagesNumber", pb.getPagesNum());
		intent.putExtra("pHashMap", pb.getHashMap());
		intent.putExtra("path", bookFullPath);
		intent.putExtra("dropbox_path", dropboxPath);
		Log.w("Test", "Before activity call COCO");
		startActivity(intent);
		
	}

}