package com.nng.onedollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.fragments.CartSellingFragment;
import com.nng.onedollar.fragments.CartSoldFragment;
import com.nng.onedollar.models.UserModel;
import com.nng.onedollar.models.UserProduct;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.CommonUtil;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nng.onedollar.views.RoundedImageView;
import com.nng.onedollar.webservices.WebServicesManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Jin on 8/11/2015.
 */
public class UserProfileActivity extends BaseAppCompatActivity {


    private class ProfileUserHolder {
        ImageView  imv_profile_verified_facebook, imv_profile_verified_email;
        RoundedImageView imv_profile_image;
        TextView tv_profile_name, tv_profile_address, tv_profile_date_join, tv_profile_item_listings, tv_selling, tv_sold;
    }

    private class ProfileStatusSocial {
        TextView tv_profile_status_number_like, tv_profile_status_number_dislike, tv_profile_status_number_cup;
    }

    private int mSelectedTabId = 0;
    private ArrayList<TextView> mTabItems;

    ProfileUserHolder profileUserHolder;
    ProfileStatusSocial profileStatusSocial;
    UserModel userModel;
    private CartSellingFragment cartSellingFragment;
    private CartSoldFragment cartSoldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String jsonUserLogin = SharedPrefUtil.loadString(this, AppConstant.USER_LOGIN, null);
        if (jsonUserLogin == null) {
            finish();
        }
        userModel = (new Gson().fromJson(jsonUserLogin, UserModel.class));
        profileUserHolder = new ProfileUserHolder();
        profileUserHolder.imv_profile_image = (RoundedImageView) findViewById(R.id.layout_profile_info).findViewById(R.id.imv_profile_image);
        profileUserHolder.imv_profile_verified_facebook = (ImageView) findViewById(R.id.layout_profile_info).findViewById(R.id.imv_profile_verified_facebook);
        profileUserHolder.imv_profile_verified_email = (ImageView) findViewById(R.id.layout_profile_info).findViewById(R.id.imv_profile_verified_email);
        profileUserHolder.tv_profile_name = (TextView) findViewById(R.id.layout_profile_info).findViewById(R.id.tv_profile_name);
        profileUserHolder.tv_profile_address = (TextView) findViewById(R.id.layout_profile_info).findViewById(R.id.tv_profile_address);
        profileUserHolder.tv_profile_date_join = (TextView) findViewById(R.id.layout_profile_info).findViewById(R.id.tv_profile_date_join);
        profileUserHolder.tv_profile_item_listings = (TextView) findViewById(R.id.tv_profile_item_listings);
        profileUserHolder.tv_selling = (TextView) findViewById(R.id.root_reuse_selling_sold_layout).findViewById(R.id.tv_selling);
        profileUserHolder.tv_sold = (TextView) findViewById(R.id.root_reuse_selling_sold_layout).findViewById(R.id.tv_sold);

        mTabItems = new ArrayList<TextView>();
        mTabItems.add(profileUserHolder.tv_selling);
        mTabItems.add(profileUserHolder.tv_sold);
        profileUserHolder.tv_selling.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                handleViewClick(v);
            }
        });
        profileUserHolder.tv_sold.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                handleViewClick(v);
            }
        });

        profileStatusSocial = new ProfileStatusSocial();
        profileStatusSocial.tv_profile_status_number_like = (TextView) findViewById(R.id.layout_profile_status_social).findViewById(R.id.tv_profile_status_number_like);
        profileStatusSocial.tv_profile_status_number_dislike = (TextView) findViewById(R.id.layout_profile_status_social).findViewById(R.id.tv_profile_status_number_dislike);
        profileStatusSocial.tv_profile_status_number_cup = (TextView) findViewById(R.id.layout_profile_status_social).findViewById(R.id.tv_profile_status_number_cup);
        cartSellingFragment = new CartSellingFragment();
        cartSoldFragment = new CartSoldFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, cartSellingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, cartSoldFragment).commit();
        getListProductOfMine();
    }


    public void handleViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selling:
                mSelectedTabId = v.getId();
                showOrHideFragment("selling");
                break;

            case R.id.tv_sold:
                mSelectedTabId = v.getId();
                showOrHideFragment("sold");
                break;
            default:
                break;
        }
    }

    private void setTabItemSelected(TextView item) {
        for (TextView view : mTabItems) {
            view.setSelected(false);
        }
        item.setSelected(true);
    }


    private void showOrHideFragment(String fragment) {
        getSupportFragmentManager().beginTransaction().hide(cartSellingFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(cartSoldFragment).commit();

        if ("selling".equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(cartSellingFragment).commit();
            setTabItemSelected(profileUserHolder.tv_selling);
        } else if ("sold".equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(cartSoldFragment).commit();
            setTabItemSelected(profileUserHolder.tv_sold);
        }
    }

    private void getListProductOfMine() {
        if (ConnectionUtil.hasInternetConnection(this)) {

            WebServicesManager.getWithToken(true, AppConstant.URL_GET_MY_PRODUCT, null,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            handleGetListProductSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                              JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            handleGetListProductFail(statusCode, headers, errorResponse);
                        }
                    }, userModel.token);
        } else {
            ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
        }
    }


    private void handleGetListProductSuccess(int statusCode, Header[] headers, JSONObject response) {
        UserProduct userProduct = (new Gson().fromJson(response.toString(), UserProduct.class));
        if (userProduct != null) {
            profileUserHolder.tv_profile_item_listings.setText(getString(R.string.item_listings, userProduct.count));
        }
    }

    private void handleGetListProductFail(int statusCode, Header[] headers, JSONObject throwabl) {
        Log.e("nhatdo", "handleGetListProductFail login " + throwabl.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfoUser();
    }

    @Override
    protected void onRightToolbarClick() {
        Intent settingActivity = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingActivity);
    }

    @Override
    protected int getBackgroundToolbar() {
        return R.color.white;
    }

    @Override
    protected void onCenterToolbarClick() {
        Intent editUserProfile = new Intent(getApplicationContext(), EditUserProfileActivity.class);
        startActivity(editUserProfile);
    }

    private void showInfoUser() {
        String jsonUserLogin = SharedPrefUtil.loadString(this, AppConstant.USER_LOGIN, null);
        if (jsonUserLogin == null) {
            finish();
        }
        userModel = (new Gson().fromJson(jsonUserLogin, UserModel.class));
        Glide.with(this).load(userModel.avatar).into(profileUserHolder.imv_profile_image);

        profileUserHolder.tv_profile_name.setText(userModel.getFullName());
        profileUserHolder.tv_profile_address.setText(userModel.country_name);
        try {
            profileUserHolder.tv_profile_date_join.setText(getString(R.string.joined_on, CommonUtil.formatDate(userModel.creation_date)));
        } catch (ParseException e) {
            e.printStackTrace();
            profileUserHolder.tv_profile_address.setText("");
        }
        profileStatusSocial.tv_profile_status_number_like.setText(String.valueOf(userModel.likes_count));
        profileStatusSocial.tv_profile_status_number_dislike.setText(String.valueOf(userModel.dislikes_count));
        if(!userModel.is_email_verified){
            profileUserHolder.imv_profile_verified_email.setImageResource(R.mipmap.ic_email_enable);
        }else{
            profileUserHolder.imv_profile_verified_email.setImageResource(R.mipmap.ic_email_disable);
        }
    }

    @Override
    protected int getLeftToolbarImage() {
        return R.mipmap.ic_back_green;
    }
}
