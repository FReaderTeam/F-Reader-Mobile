package com.freader.di;

import android.app.Activity;
import android.os.Bundle;

public class InjectingActivity extends Activity {

	@Override protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    // Perform injection so that when this call returns 
	    // all dependencies will be available for use.
	    ((FReaderApplication) getApplication()).inject(this);
	  }
	
}
