package com.freader;

import java.io.File;
import java.util.List;

import com.dropbox.sync.android.DbxFileInfo;
import com.freader.utils.FileSystemUtils;

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

	public BookListAdapter(Context context, List<DbxFileInfo> books) {
		mBooks = books;
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
		String bookPath = FileSystemUtils.BOOKS_FOLDER + "/"
				+ mBooks.get(position).path.getName();
		File f = new File(bookPath);
		int resourceId = f.exists() ? R.drawable.phone
				: R.drawable.dropbox_logo;
		holder.itemImage.setImageResource(resourceId);
		return convertView;
	}

	static class ViewHolder {
		TextView txt_bookName;
		ImageView itemImage;
	}
}
