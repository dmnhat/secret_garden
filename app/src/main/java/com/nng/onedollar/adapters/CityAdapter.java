package com.nng.onedollar.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nng.onedollar.R;
import com.nng.onedollar.activities.ChooseCityActivity.Country;

public class CityAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Country> mData;
	private int mCheckedPosition = -1;

	public CityAdapter(Context context) {
		mContext = context;
	}

	public void setData(ArrayList<Country> data) {
		mData = data;
		notifyDataSetChanged();
	}

	public void setCheckedPosition(int position) {
		mCheckedPosition = position;
	}

	public int getCheckedPosition() {
		return mCheckedPosition;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_city, null);
		}

		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);

		tvName.setText(mData.get(position).name);
		ivCheck.setVisibility(View.INVISIBLE);
		if (mCheckedPosition == position) {
			ivCheck.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
