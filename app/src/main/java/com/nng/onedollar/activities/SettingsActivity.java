package com.nng.onedollar.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nng.onedollar.R;
import com.nng.onedollar.utils.UiUtil;

/**
 * Created by Jin on 8/11/2015.
 */
public class SettingsActivity extends BaseAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isRightToolbarShown() {
        return false;
    }

    @Override
    protected String getTitleToolbar() {
        return getResources().getString(R.string.settings);
    }
    public void onSettingClick(View v){
        switch (v.getId()){
            case R.id.tv_setting_about_the_app:
                UiUtil.showShortToast("tv_setting_about_the_app");
                break;
            case R.id.tv_setting_faq:
                UiUtil.showShortToast("tv_setting_faq");
                break;
            case R.id.tv_setting_term:
                UiUtil.showShortToast("tv_setting_term");
                break;
            case R.id.tv_setting_legal:
                UiUtil.showShortToast("tv_setting_legal");
                break;
        }
    }

}
