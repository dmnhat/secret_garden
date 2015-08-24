package com.nng.onedollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.nng.onedollar.webservices.WebServicesManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Jin on 8/11/2015.
 */
public class ViewProductUserProfileActivity extends BaseAppCompatActivity {


    private class ProfileUserHolder {
        ImageView imv_profile_image, imv_profile_verified_facebook, imv_profile_verified_email;
        TextView tv_profile_name, tv_profile_address, tv_profile_date_join, tv_profile_item_listings, tv_selling, tv_sold;
    }


    ProfileUserHolder profileUserHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view_product);

        profileUserHolder = new ProfileUserHolder();

//        profileUserHolder.tv_selling.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleViewClick(v);
//            }
//        });

    }


    public void handleViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selling:
                break;
            default:
                break;
        }
    }


    private void getListCommentOnProduct() {

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
    protected int getBackgroundToolbar() {
        return R.color.transparent;
    }

    @Override
    protected float getAlphaToolbar() {
        return 1;
    }

    @Override
    protected boolean isRightToolbarShown() {
        return false;
    }

    @Override
    protected int getLeftToolbarImage() {
        return R.mipmap.ic_back_white;
    }
}
