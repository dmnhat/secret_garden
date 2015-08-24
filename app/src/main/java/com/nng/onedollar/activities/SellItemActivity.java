package com.nng.onedollar.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nng.onedollar.R;
import com.nng.onedollar.adapters.ListImageUploadAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jin on 8/21/2015.
 */
public class SellItemActivity extends BaseAppCompatActivity implements View.OnClickListener {


    //--------------------------------------------------
    //  LIFE CYCLE
    //--------------------------------------------------

    RecyclerView listImageUploadRecyclerView;
    ListImageUploadAdapter listImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        listImageAdapter = new ListImageUploadAdapter(getApplicationContext(), this);
        listImageUploadRecyclerView = (RecyclerView) findViewById(R.id.listImageUpload);
        listImageUploadRecyclerView.setAdapter(listImageAdapter);
        listImageUploadRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imv_image_upload && v.getTag() == 0) {
            showDialogChooseIntentImage();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            ArrayList<Bitmap> listBitmap = new ArrayList<>();
            listBitmap.add(imageBitmapFromCamera);
            listImageAdapter.updateListImageUpload(listBitmap);
        } else if (requestCode == REQUEST_GALLERY) {

        }
    }



    //--------------------------------------------------
    //  STYLE FOR TOOLBAR
    //--------------------------------------------------
    @Override
    protected int getLeftToolbarImage() {
        return R.mipmap.ic_cancel_white;
    }

    @Override
    protected int getRightToolbarImage() {
        return R.mipmap.ic_tick_white;
    }

    @Override
    protected String getTitleToolbar() {
        return getResources().getString(R.string.sell_item);
    }

    @Override
    protected int getBackgroundToolbar() {
        return R.color.green;
    }


}
