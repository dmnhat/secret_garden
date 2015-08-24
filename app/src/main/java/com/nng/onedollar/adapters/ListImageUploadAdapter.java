package com.nng.onedollar.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nng.onedollar.R;
import com.nng.onedollar.application.OneDollarApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jin on 8/22/2015.
 */
public class ListImageUploadAdapter extends RecyclerView.Adapter<ListImageUploadAdapter.ViewHolder> {

    ArrayList<Bitmap> listImageUpload;
    Context context;
    View.OnClickListener onClickListener;

    public ListImageUploadAdapter(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void updateListImageUpload(ArrayList<Bitmap> _listImageUpload) {
        listImageUpload = null;
        this.listImageUpload = _listImageUpload;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.row_image_upload, null);
        return new ViewHolder(convertView, onClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            Picasso.with(context).load(R.mipmap.ic_camera).into(holder.imageView);
        } else {
      //      String urlBitmap = listImageUpload.get(position - 1);
      //      Picasso.with(context).load(urlBitmap).into(holder.imageView);
        }
        holder.imageView.setTag(position);

    }

    @Override
    public int getItemCount() {
        if (listImageUpload == null)
            return 1;
        else
            return 1 + listImageUpload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        View.OnClickListener onClickListener;

        public ViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setOnClickListener(onClickListener);
        }
    }
}
