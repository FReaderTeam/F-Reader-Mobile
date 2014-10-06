package ua.kharkiv.nure.freader;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends Activity {
	public static final String APP_PREFERENCES = "mysettings";
	public static final String APP_PREFERENCES_LOGIN = "Login";
	public static final String APP_PREFERENCES_PASSWORD = "Password";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		SharedPreferences sp = getSharedPreferences(APP_PREFERENCES,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (sp.contains(APP_PREFERENCES_LOGIN)
				&& sp.contains(APP_PREFERENCES_PASSWORD)) {
			Intent intentArtemActivity = new Intent(MainActivity.this,
					ListViewActivity.class);
			startActivity(intentArtemActivity);

			boolean isLogout = getIntent().getBooleanExtra("isLogout", true);
			if (isLogout) {
				e = sp.edit();
				e.clear();
				e.commit();
			}
		} else {
			Intent intentKateActivity = new Intent(MainActivity.this,
					KateForm.class);
			startActivity(intentKateActivity);

			String login = " ";
			String password = " ";
			login = getIntent().getStringExtra("login");
			password = getIntent().getStringExtra("password");

			e.putString(APP_PREFERENCES_LOGIN, login);
			e.putString(APP_PREFERENCES_PASSWORD, password);
			e.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
