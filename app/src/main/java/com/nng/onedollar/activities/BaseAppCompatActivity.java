package com.nng.onedollar.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nng.onedollar.R;
import com.nng.onedollar.utils.UiUtil;

/**
 * Created by Jin on 8/16/2015.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    ImageButton imb_back_toolbar, imb_edit_toolbar, imb_setting_toolbar;
    TextView tv_title_toolbar;
    RelativeLayout rl_toolbar_background;
    IViewClickListener iViewClickListener;
    MaterialDialog materialDialog;
    public static final int REQUEST_GALLERY = 2001;
    public static final int REQUEST_CAMERA = 2002;
    public Bitmap imageBitmapFromCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iViewClickListener = new IViewClickListener();
    }

    public void showMessageWaiting(boolean autoDismiss) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.plaese_wait)
                .content(R.string.updating).cancelable(autoDismiss)
                .progress(true, 0).show();

    }

    public void dismissWaiting() {
        materialDialog.dismiss();
        materialDialog = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (findViewById(R.id.layout_toolbar) != null) {
            setSupportActionBar((Toolbar) findViewById(R.id.layout_toolbar));
        }
        initActionBar();
    }

    private class IViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imb_back_toolbar:
                    onLeftToolbarClick();
                    break;
                case R.id.imb_setting_toolbar:
                    onRightToolbarClick();
                    break;
                case R.id.imb_edit_toolbar:
                    onCenterToolbarClick();
                    break;
                default:
                    break;
            }
        }
    }

    protected void initActionBar() {
        imb_back_toolbar = (ImageButton) findViewById(R.id.imb_back_toolbar);
        imb_setting_toolbar = (ImageButton) findViewById(R.id.imb_setting_toolbar);
        imb_edit_toolbar = (ImageButton) findViewById(R.id.imb_edit_toolbar);
        tv_title_toolbar = (TextView) findViewById(R.id.tv_title_toolbar);
        rl_toolbar_background = (RelativeLayout) findViewById(R.id.rl_toolbar_background);
        if (rl_toolbar_background != null) {
            rl_toolbar_background.setBackgroundColor(getBackgroundToolbar());
            rl_toolbar_background.setAlpha(getAlphaToolbar());
        }
        if (imb_back_toolbar != null) {
            imb_back_toolbar.setOnClickListener(iViewClickListener);
            imb_back_toolbar.setImageResource(getLeftToolbarImage());
        }
        if (imb_setting_toolbar != null) {
            imb_setting_toolbar.setImageResource(getRightToolbarImage());
            if (isRightToolbarShown()) {
                imb_setting_toolbar.setVisibility(View.VISIBLE);
            } else {
                imb_setting_toolbar.setVisibility(View.GONE);
            }
            imb_setting_toolbar.setOnClickListener(iViewClickListener);
        }
        if (imb_edit_toolbar != null) {
            imb_edit_toolbar.setOnClickListener(iViewClickListener);
        }
        if (tv_title_toolbar != null) {
            tv_title_toolbar.setText(getTitleToolbar());
        }
    }

    protected void onLeftToolbarClick() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
    }

    protected void onRightToolbarClick() {
        UiUtil.showShortToast("onRightToolbarClick");
    }

    protected void onCenterToolbarClick() {
    }

    public void showDialogChooseIntentImage() {
        new MaterialDialog.Builder(this)
                .title("Upload image by using")
                .content(null)
                .positiveText("From Gallery")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        importImageFromGallery();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        importImageFromCamera();
                    }
                })
                .negativeText("From Camera")
                .show();
    }

    private void importImageFromGallery() {

    }


    private void importImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmapFromCamera = (Bitmap) extras.get("data");

        } else if (requestCode == REQUEST_GALLERY) {

        }
    }
    protected void deAlloc() {
        imb_back_toolbar = imb_edit_toolbar = imb_setting_toolbar = null;
        tv_title_toolbar = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deAlloc();
    }


    protected String getTitleToolbar() {
        return getResources().getString(R.string.app_name);
    }

    protected boolean isRightToolbarShown() {
        return true;
    }

    protected int getBackgroundToolbar() {
        return R.color.white;
    }

    protected float getAlphaToolbar() {
        return 1.0f;
    }

    protected int getLeftToolbarImage() {
        return R.drawable.ic_menu;
    }

    protected int getRightToolbarImage() {
        return R.mipmap.ic_profile_setting_green;
    }
}
