package com.nng.onedollar.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nng.onedollar.R;

public class BaseActivity extends FragmentActivity {

    private static ArrayList<BaseActivity> mActivities = new ArrayList<BaseActivity>();
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!mActivities.contains(this)) {
            mActivities.add(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mActivities.contains(this)) {
            mActivities.remove(this);
        }
        deAlloc();
    }

    protected  void deAlloc(){

    }

    public void closeAllActivities() {
        for (BaseActivity activity : mActivities) {
            activity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
    }



}
