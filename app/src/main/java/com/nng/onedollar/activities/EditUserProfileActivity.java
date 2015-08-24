package com.nng.onedollar.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nng.onedollar.R;
import com.nng.onedollar.models.UserModel;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nng.onedollar.utils.UiUtil;
import com.nng.onedollar.views.RoundedImageView;
import com.nng.onedollar.webservices.WebServicesManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

/**
 * Created by Jin on 8/11/2015.
 */
public class EditUserProfileActivity extends BaseAppCompatActivity  implements View.OnClickListener{

    ImageButton imb_back_toolbar, imb_edit_toolbar, imb_setting_toolbar;
    UserModel userModel;
    EditText et_edit_profile_first_name, et_edit_profile_last_name, et_edit_profile_city, et_edit_profile_email,
            et_edit_profile_mobile, et_edit_profile_gender;
    Button btnBirthday;
    RoundedImageView imv_edit_profile_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        et_edit_profile_first_name = (EditText) findViewById(R.id.et_edit_profile_first_name);
        et_edit_profile_last_name = (EditText) findViewById(R.id.et_edit_profile_last_name);
        et_edit_profile_city = (EditText) findViewById(R.id.et_edit_profile_city);
        et_edit_profile_email = (EditText) findViewById(R.id.et_edit_profile_email);
        et_edit_profile_mobile = (EditText) findViewById(R.id.et_edit_profile_mobile);
        et_edit_profile_gender = (EditText) findViewById(R.id.et_edit_profile_gender);
        btnBirthday = (Button) findViewById(R.id.btnBirthday);
        imv_edit_profile_avatar = (RoundedImageView) findViewById(R.id.imv_edit_profile_avatar);
        imv_edit_profile_avatar.setOnClickListener(this);
        String jsonUserLogin = SharedPrefUtil.loadString(this, AppConstant.USER_LOGIN, null);
        if (jsonUserLogin == null) {
            finish();
        }
        userModel = (new Gson().fromJson(jsonUserLogin, UserModel.class));
        imageBitmapFromCamera = null;
        showInforUser();
    }


    @Override
    protected void onLeftToolbarClick() {
        super.onLeftToolbarClick();
    }

    @Override
    protected int getRightToolbarImage() {
        return R.mipmap.ic_tick_white;
    }

    @Override
    protected void onRightToolbarClick() {
        updateUser();
    }

    private void updateUser() {
        showMessageWaiting(false);
        if (ConnectionUtil.hasInternetConnection(this)) {


            String url = AppConstant.URL_EDIT_USER + userModel.id + "/";

            RequestParams requestParams = new RequestParams();
            requestParams.put("gender", et_edit_profile_gender.getText().toString().equals("Male") ? 1 : 0);
            requestParams.put("country", et_edit_profile_city.getText().toString());
            requestParams.put("credits", userModel.credits);
            requestParams.put("first_name", et_edit_profile_first_name.getText().toString());
            requestParams.put("last_name", et_edit_profile_last_name.getText().toString());
            requestParams.put("about", userModel.about);
            requestParams.put("dob", "2015-12-12");
            requestParams.put("email", et_edit_profile_email.getText().toString());

            if (imageBitmapFromCamera != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmapFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    requestParams.put("avatar", encodedImage);
            } else {
                File file = new File(URI.create(userModel.avatar));
                Bitmap bm = BitmapFactory.decodeFile(userModel.avatar);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                requestParams.remove("avatar");
                requestParams.put("avatar", encodedImage);
            }
            requestParams.setContentEncoding("UTF-8");
            Log.e("nhatdo", "requestEditUser " + requestParams.toString());
            WebServicesManager.patch(true, this, url, requestParams,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            dismissWaiting();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.e("nhatdo", "onSuccess EditUserProfileAct " + response.toString());
                            handleEditingUserSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                              JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Log.e("nhatdo", "onFailure EditUserProfileAct " + errorResponse.toString());
                            handleEditingUserFail(statusCode, headers, errorResponse);
                        }
                    }, userModel.token);
        } else {
            ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
        }
    }

    private void handleEditingUserSuccess(int statusCode, Header[] headers, JSONObject response) {
        if (response == null) {
            finish();
        }
        String token = userModel.token;
        userModel = (new Gson().fromJson(response.toString(), UserModel.class));
        userModel.token = token;
        SharedPrefUtil.saveString(this, AppConstant.USER_LOGIN, new Gson().toJson(userModel));
        UiUtil.showShortToast("Update profile successful !");
    }

    private void handleEditingUserFail(int statusCode, Header[] headers, JSONObject errorResponse) {
        UiUtil.showShortToast("Update profile fail ! \n "+errorResponse==null?"":errorResponse.toString());
    }

    private void showInforUser() {
        if (userModel != null) {
            Log.e("nhatdo", "token " + userModel.token);
            String firstName = userModel.first_name == null ? "" : userModel.first_name;
            String lastName = userModel.last_name == null ? "" : userModel.last_name;
            String city = userModel.country_name == null ? "" : userModel.country_name;
            String email = userModel.email == null ? "" : userModel.email;
            String mobile = ""; //TODO not defined
            String gender = userModel.gender == 0 ? "Male" : "Female";
            String dob = userModel.dob == null ? "" : userModel.dob;
            et_edit_profile_first_name.setText(firstName);
            et_edit_profile_last_name.setText(lastName);
            et_edit_profile_city.setText(city);
            et_edit_profile_email.setText(email);
            et_edit_profile_mobile.setText(mobile);
            et_edit_profile_gender.setText(gender);
            btnBirthday.setText(dob);
            Glide.with(this).load(userModel.avatar).into(imv_edit_profile_avatar);
        }
    }

    File bitmapFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Log.e("nhatdo", "mCurrentPhotoPath " + imageBitmapFromCamera);

            if(imageBitmapFromCamera!=null){
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                imageBitmapFromCamera.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, blob);
                byte[] bitmapdata = blob.toByteArray();
                Glide.with(this).fromBytes().load(bitmapdata).fitCenter().into(imv_edit_profile_avatar);
               // imv_edit_profile_avatar.setImageBitmap(imageBitmapFromCamera);
               // UiUtil.loadAvatarProfile(this, imv_edit_profile_avatar.get, imv_edit_profile_avatar);
            }
        } else if (requestCode == REQUEST_GALLERY) {

        }
    }

    @Override
    protected String getTitleToolbar() {
        return getResources().getString(R.string.edit_profile);
    }

    @Override
    protected int getLeftToolbarImage() {
        return R.mipmap.ic_back_white;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imv_edit_profile_avatar:
                showDialogChooseIntentImage();
                break;
        }
    }
}
