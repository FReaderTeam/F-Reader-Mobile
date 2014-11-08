package com.freader;

import java.io.File;
import java.util.List;

import com.dropbox.sync.android.DbxFileInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private static List<DbxFileInfo> mBooks;
	private LayoutInflater mLayoutInflater;
	private String mAppPath;

	public BookListAdapter(Context context, List<DbxFileInfo> books,
			String appPath) {
		mBooks = books;
		mAppPath = appPath;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mBooks.size();
	}

	@Override
	public Object getItem(int position) {
		return mBooks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.book_list_item,
					parent, false);
			holder = new ViewHolder();
			holder.txt_bookName = (TextView) convertView
					.findViewById(R.id.book_list_name);
			holder.itemImage = (ImageView) convertView
					.findViewById(R.id.book_list_image);
			convertView.findViewById(R.id.book_list_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_bookName.setText(mBooks.get(position).path.getName());
		File f = new File(mAppPath + "/" + mBooks.get(position).path.getName());
		if (f.exists())
			holder.itemImage.setImageResource(R.drawable.phone);
		else
			holder.itemImage.setImageResource(R.drawable.dropbox_logo);
		return convertView;
	}

	static class ViewHolder {
		TextView txt_bookName;
		ImageView itemImage;
	}
}
