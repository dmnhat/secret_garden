package com.nng.onedollar.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nng.onedollar.R;

public class NotificationAdapter extends BaseAdapter {

	private Context mContext;
	private ViewHolder mHolder;

	public NotificationAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return 20;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_notification, null);

			mHolder = new ViewHolder();
			mHolder.tvMessage = (TextView) convertView
					.findViewById(R.id.tv_message);
			mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		String userName = "<strong>" + "Username666" + "</strong>";
		String message = "has won your item.";
		String time = "posted 3 hours ago";
		mHolder.tvMessage.setText(Html.fromHtml(userName + " " + message));
		mHolder.tvTime.setText(Html.fromHtml(time));
		
		return convertView;
	}

	private class ViewHolder {

		public TextView tvMessage, tvTime;
	}

}
