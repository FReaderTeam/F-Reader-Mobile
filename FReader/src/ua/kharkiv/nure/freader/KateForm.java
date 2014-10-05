package ua.kharkiv.nure.freader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class KateForm extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.kate_form);
	    Button button = (Button)findViewById(R.id.mButtonLogin);
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onClickOutter(v);
			}
		});
	}
	
	public void onClickOutter(View v) {
	    EditText edUserName = (EditText) findViewById(R.id.login);
	    EditText edDescription = (EditText) findViewById(R.id.password);

		Intent intent = new Intent(KateForm.this, MainActivity.class);
		intent.putExtra("login", edUserName.getText().toString()); 
		intent.putExtra("password", edDescription.getText().toString());
		startActivity(intent);
	}
	
	

}
