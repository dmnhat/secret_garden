package com.nng.onedollar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nng.onedollar.utils.ImageUtils;


public class RoundedImageView extends ImageView {


    public RoundedImageView(Context ctx) {
        super(ctx);
    }

    public RoundedImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b =null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                b= bitmapDrawable.getBitmap();
            }
        }else{
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas1 = new Canvas(b);
            drawable.setBounds(0, 0, canvas1.getWidth(), canvas1.getHeight());
            drawable.draw(canvas1);
        }
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        Bitmap scaleBitmap = ImageUtils.scaleCenterCrop(bitmap, w, h);
        Bitmap roundBitmap = ImageUtils.getRoundedCornerBitmap(scaleBitmap, 0, getContext());
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

}