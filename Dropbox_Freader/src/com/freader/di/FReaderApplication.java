package com.freader.di;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import android.app.Application;

public class FReaderApplication extends Application {

	private ObjectGraph graph;

	@Override
	public void onCreate() {
		super.onCreate();
		graph = ObjectGraph.create(getModules().toArray());
	}

	protected List<Object> getModules() {
		return Arrays.<Object> asList(new AndroidModule(this),
				new DropboxClientModule(this));
	}

	public void inject(Object object) {
		graph.inject(object);
	}

}
