package com.nng.onedollar.adapters;

import java.util.ArrayList;

import com.nng.onedollar.R;
import com.nng.onedollar.models.ProductModel;
import com.nng.onedollar.views.SquareImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ProductModel> mData;
	private ViewHolder mHolder;
	private ProductModel mProduct;

	public ProductAdapter(Context context) {
		mContext = context;
	}

	public void setData(ArrayList<ProductModel> data) {
		mData = data;
		notifyDataSetChanged();
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
		return mData.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mProduct = mData.get(position);

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.item_product, null);

			mHolder = new ViewHolder();
			mHolder.ivAvatar = (SquareImageView) convertView.findViewById(R.id.iv_avatar);
			mHolder.ivStatus = (ImageView) convertView.findViewById(R.id.iv_status);
			mHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.ivStatus.setImageResource(R.drawable.ic_second_hand);
		if (mProduct.is_new) {
			mHolder.ivStatus.setImageResource(R.drawable.ic_new);
		}

		String img = null;
		if (mProduct.photos != null && !mProduct.photos.isEmpty()) {
			img = mProduct.photos.get(0).image;
		}
		mHolder.ivAvatar.setImageResource(android.R.color.transparent);
		if (!TextUtils.isEmpty(img)) {
			ImageLoader.getInstance().displayImage(img, mHolder.ivAvatar);
		}

		mHolder.tvPrice.setText(mContext.getText(R.string.currency) + mProduct.price);

		return convertView;
	}

	public class ViewHolder {

		public SquareImageView ivAvatar;
		public ImageView ivStatus;
		public TextView tvPrice;
	}

}
