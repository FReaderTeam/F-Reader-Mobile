package com.freader.di;

import static com.freader.dao.DropboxSettings.APP_KEY;
import static com.freader.dao.DropboxSettings.APP_SECRET;

import javax.inject.Singleton;

import android.app.Application;

import com.dropbox.sync.android.DbxAccountManager;
import com.freader.AuthorizationActivity;

import dagger.Module;
import dagger.Provides;

@Module(injects=AuthorizationActivity.class)
public class DropboxClientModule {

	DbxAccountManager dbxManager;

	public DropboxClientModule(Application context) {
		dbxManager = DbxAccountManager
				.getInstance(context, APP_KEY, APP_SECRET);
	}

	@Provides
	@Singleton
	public DbxAccountManager provideDbxManager() {
		return dbxManager;
	}
}
