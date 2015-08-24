package com.nng.onedollar.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.nng.onedollar.R;

/**
 * Created by Jin on 8/11/2015.
 */
public class IntroductionActivity extends BaseActivity {

    public static final String START_ME = "START_ME?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
    public void backToHomeScreen(View v){
        this.finish();
    }
}
