package com.nng.onedollar.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nng.onedollar.R;
import com.nng.onedollar.application.OneDollarApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import codetail.graphics.drawables.DrawableHotspotTouch;
import codetail.graphics.drawables.LollipopDrawable;
import codetail.graphics.drawables.LollipopDrawablesCompat;

public class UiUtil {

    private static Toast mToast;

    public static void hideKeyboard(Context context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(OneDollarApplication.getInstance(), message, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLongToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(OneDollarApplication.getInstance(), message, Toast.LENGTH_LONG);
        }
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showMessageDialog(Context context, String title,
                                         String message, String btnTitle) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnTitle, null);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void showSingleChoiceDialog(Context context, String title,
                                              String[] data, int checkedItem,
                                              DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setSingleChoiceItems(data, checkedItem, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void setDrawable(View view, Drawable drawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static void makeDrippleForView(View view) {
        UiUtil.setDrawable(view, getDrawable2(R.drawable.ripple_toolbar));
        view.setClickable(true);// if we don't set it true, ripple will not be played
        view.setOnTouchListener(
                new DrawableHotspotTouch((LollipopDrawable) view.getBackground()));
    }

    public static void makeDrippleForMultiViews(View... views) {
        for (View view : views) {
            makeDrippleForView(view);
        }
    }
    public static void makeDrippleForView(int drawableRipple, View view) {
        UiUtil.setDrawable(view, getDrawable2(drawableRipple));
        view.setClickable(true);// if we don't set it true, ripple will not be played
        view.setOnTouchListener(
                new DrawableHotspotTouch((LollipopDrawable) view.getBackground()));
    }
    public static void makeDrippleForMultiViews(int drawableRipple, View... views) {
        for (View view : views) {
            makeDrippleForView(drawableRipple, view);
        }
    }
    /**
     * {@link #getDrawable2(int)} is already taken by Android API
     * and method is final, so we need to give another name :(
     */
    public static Drawable getDrawable2(int id){
        return LollipopDrawablesCompat.getDrawable(OneDollarApplication.getInstance().getResources(), id);
    }
}
