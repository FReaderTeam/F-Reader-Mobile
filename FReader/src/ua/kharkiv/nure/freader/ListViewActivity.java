package ua.kharkiv.nure.freader;


import java.util.ArrayList;

import ua.kharkiv.nure.freader.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ArrayList<Book> image_details = GetSearchResults();        
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new ItemListBaseAdapter(this, image_details));        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object o = lv1.getItemAtPosition(position);
            	Book obj_itemDetails = (Book)o;
        		Toast.makeText(ListViewActivity.this, "You have chosen : " + obj_itemDetails.getName(), Toast.LENGTH_SHORT).show();
        	}  
        });
    }
	
    //Method for searching book collection of user
	private ArrayList<Book> GetSearchResults(){
		// add 3 books for an example
    	ArrayList<Book> results = new ArrayList<Book>(3);
    	results.add(new Book("Hobbit","J.R.R. Tolkin"));
    	results.add(new Book("Harry Potter","J.K.Rowling"));
    	results.add(new Book("Metro 2033","D. Glukhovsky"));
    	return results;
    }
}