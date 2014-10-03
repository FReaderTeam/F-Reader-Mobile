package ua.kharkiv.nure.freader;

import java.util.ArrayList;

import ua.kharkiv.nure.freader.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<Book> booksArrayList;
		
	private LayoutInflater l_Inflater;

	public ItemListBaseAdapter(Context context, ArrayList<Book> results) {
		booksArrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return booksArrayList.size();
	}

	public Object getItem(int position) {
		return booksArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_view, null);
			holder = new ViewHolder();
			holder.txt_bookName = (TextView) convertView.findViewById(R.id.name);
			holder.txt_bookAuthor = (TextView) convertView.findViewById(R.id.author);
			//holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		holder.txt_bookName.setText(booksArrayList.get(position).getName());
		holder.txt_bookAuthor.setText(booksArrayList.get(position).getAuthor());
		//holder.itemImage.setImageResource(R.drawable.icon);
		return convertView;
	}

	static class ViewHolder {
		TextView txt_bookName;
		TextView txt_bookAuthor;
		ImageView itemImage;
	}
}
