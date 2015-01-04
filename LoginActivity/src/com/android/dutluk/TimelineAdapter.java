package com.android.dutluk;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimelineAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private static String type= "";

	public TimelineAdapter(Activity a, ArrayList<HashMap<String, String>> d, String t) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		type = t;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null){
			if(type.equals("comment")) {
				vi = inflater.inflate(R.layout.list_row_comment, null);
			}
			else {
				vi = inflater.inflate(R.layout.list_row, null);
			}
			
		}
			
		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView info = (TextView) vi.findViewById(R.id.info);
		HashMap<String, String> song = new HashMap<String, String>();
		song = data.get(position);
		title.setText(song.get(TimelineActivity.KEY_TITLE));
		info.setText(song.get(TimelineActivity.KEY_INFO));
		return vi;

	}
}
