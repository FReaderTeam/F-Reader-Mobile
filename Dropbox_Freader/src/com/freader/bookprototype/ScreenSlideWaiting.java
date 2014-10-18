package com.freader.bookprototype;

import java.util.ArrayList;
import android.text.Layout;

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
    //private CharSequence text = "In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort. It had a perfectly round door like a porthole, painted green, with a shiny yellow brass knob in the exact middle. The door opened on to a tube-shaped hall like a tunnel: a very comfortable tunnel without smoke, with panelled walls, and floors tiled and carpeted, provided with polished chairs, and lots and lots of pegs for hats and coatsthe hobbit was fond of visitors. The tunnel wound on and on, going fairly but not quite straight into the side of the hillThe Hill, as all the people for many miles round called itand many little round doors opened out of it, first on one side and then on another. No going upstairs for the hobbit: bedrooms, bathrooms, cellars, pantries (lots of these), wardrobes (he had whole rooms devoted to clothes), kitchens, dining-rooms, all were on the same floor, and indeed on the same passage. The best rooms were all on the left-hand side (going in), for these were the only ones to have windows, deep-set round windows looking over his garden and meadows beyond, sloping down to the river. This hobbit was a very well-to-do hobbit, and his name was Baggins. In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort. It had a perfectly round door like a porthole, painted green, with a shiny yellow brass knob in the exact middle. The door opened on to a tube-shaped hall like a tunnel: a very comfortable tunnel without smoke, with panelled walls, and floors tiled and carpeted, provided with polished chairs, and lots and lots of pegs for hats and coatsthe hobbit was fond of visitors. The tunnel wound on and on, going fairly but not quite straight into the side of the hillThe Hill, as all the people for many miles round called itand many little round doors opened out of it, first on one side and then on another. No going upstairs for the hobbit: bedrooms, bathrooms, cellars, pantries (lots of these), wardrobes (he had whole rooms devoted to clothes), kitchens, dining-rooms, all were on the same floor, and indeed on the same passage. The best rooms were all on the left-hand side (going in), for these were the only ones to have windows, deep-set round windows looking over his garden and meadows beyond, sloping down to the river";
    private ViewTreeObserver observer;
    private int size;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_screen_slide);
		title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("name");
        ArrayList<String> att = (ArrayList<String>) getIntent().getSerializableExtra("book");
        this.parsedBook = new ParsedBook(title, author, att);
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
                Log.w("MY APPP TEXT ON TEXT VIEW", (String)textViewForGetSize.getText());
        		Log.w("MY APPPP SIZE 1", String.valueOf(size));        		
        		parsedBook.initPages(size, textViewForGetSize);
            }
            
        });
		
	}

	@Override
	public void callback(PagedBook pb) {
		Intent intent = new Intent(this, ScreenSlideActivity.class);
		intent.putExtra("title", pb.getTitle());
		intent.putExtra("name", pb.getAuthor());
		intent.putExtra("book", pb.getPages());
		intent.putExtra("pagesNumber", pb.getPagesNum());
		intent.putExtra("pHashMap", pb.getHashMap());
		Log.w("MY APPPP PAGE 1", pb.getPage(1));     
		Log.w("MY APPPP PAGE 2", pb.getPage(2));
		startActivity(intent);
		
	}

}