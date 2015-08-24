package com.nng.onedollar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nng.onedollar.R;
import com.nng.onedollar.manager.BaseResponse;
import com.nng.onedollar.manager.Request.SignUpRequest;
import com.nng.onedollar.manager.Response.SignUpResponse;
import com.nng.onedollar.manager.SignUpResponseListener;
import com.nng.onedollar.manager.User.UserManager;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.CommonUtil;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.PhotoUtils;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nng.onedollar.utils.UiUtil;
import com.nng.onedollar.views.TMKProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class SignUpDetailActivity extends BaseActivity implements SignUpResponseListener {

    private Button mBtnCity;
    private String mCityName, mCityCode, mEmail, mPassword, mUserName, mFBToken, mAvatarPath, mAvatarPathTemp;
    private boolean mIsFB;
    private EditText mEtEmail, mEtUserName;
    private TMKProgressDialog mPDialog;
    private ImageView mIvAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);
        overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

        mEmail = getIntent().getStringExtra("email");
        mUserName = getIntent().getStringExtra("username");
        mPassword = getIntent().getStringExtra("password");
        mFBToken = getIntent().getStringExtra("fbToken");
        mIsFB = getIntent().getBooleanExtra("isFB", false);
        mAvatarPath = getIntent().getStringExtra("avatarUrl");

        mPDialog = new TMKProgressDialog(this);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mIvAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!CommonUtil.isUrlValid(mAvatarPath)) {
                    String[] arr;
                    if (TextUtils.isEmpty(mAvatarPath)) {
                        arr = getResources().getStringArray(R.array.camera1);
                    } else {
                        arr = getResources().getStringArray(R.array.camera2);
                    }
                    UiUtil.showSingleChoiceDialog(SignUpDetailActivity.this, "", arr, -1,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        PhotoUtils.choosePhoto(SignUpDetailActivity.this, AppConstant.RC_CHOOSE_PHOTO);
                                    } else if (which == 1) {
                                        mAvatarPathTemp = PhotoUtils.capturePhoto(SignUpDetailActivity.this,
                                                AppConstant.RC_CAPTURE_PHOTO);
                                    } else if (which == 2) {
                                        mAvatarPath = null;
                                        displayAvatar();
                                    }
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

        if (!TextUtils.isEmpty(mEmail)) {
            mEtEmail.setText(mEmail);
            mEtEmail.setEnabled(false);
        }

        if (!TextUtils.isEmpty(mUserName)) {
            mEtUserName.setText(mUserName);
        }

        displayAvatar();

        findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
            }
        });

        findViewById(R.id.tv_done).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
                executeSignUp();
            }
        });

        mBtnCity = (Button) findViewById(R.id.btn_city);
        mBtnCity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
                Intent intent = new Intent(getApplicationContext(), ChooseCityActivity.class);
                intent.putExtra("code", mCityCode);
                startActivityForResult(intent, AppConstant.RC_CHOOSE_COUNTRY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstant.RC_CHOOSE_COUNTRY:
                    mCityName = data.getStringExtra("name");
                    mCityCode = data.getStringExtra("code");
                    mBtnCity.setText(mCityName);
                    break;

                case AppConstant.RC_CAPTURE_PHOTO:
                    mAvatarPath = mAvatarPathTemp;
                    PhotoUtils.addPhotoToGallery(this, mAvatarPath);
                    displayAvatar();
                    break;

                case AppConstant.RC_CHOOSE_PHOTO:
                    mAvatarPath = PhotoUtils.getPhotoPath(this, data);
                    displayAvatar();
                    break;
            }
        }
    }

    private void displayAvatar() {
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, mIvAvatar);
        if (!TextUtils.isEmpty(mAvatarPath)) {
            DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
            optionsBuilder.cacheInMemory(true);
            optionsBuilder.cacheOnDisk(true);
            optionsBuilder.resetViewBeforeLoading(true);
            optionsBuilder.considerExifParams(true);
            optionsBuilder.displayer(new RoundedBitmapDisplayer(200));
            DisplayImageOptions options = optionsBuilder.build();

            if (CommonUtil.isUrlValid(mAvatarPath)) {
                ImageLoader.getInstance().displayImage(mAvatarPath, mIvAvatar, options);
            } else {
                ImageLoader.getInstance().displayImage("file://" + mAvatarPath, mIvAvatar, options);
            }
        }
    }

    private boolean validate() {
        mEmail = mEtEmail.getText().toString().trim();
        mUserName = mEtUserName.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mCityCode)) {
            ErrorUtil.showError(this, getString(R.string.msg_err_not_enough_info));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            ErrorUtil.showError(this, getString(R.string.msg_invalid_email));
            return false;
        }

        return true;
    }

    private void executeSignUp() {
        if (validate()) {
            if (ConnectionUtil.hasInternetConnection(this)) {
                mPDialog.show();
                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.username = mUserName;
                signUpRequest.country = mCityCode;
                signUpRequest.email = mEmail;

                if (mIsFB) {
                    signUpRequest.fb_token = mFBToken;
                    signUpRequest.password = "";
                } else {
                    signUpRequest.fb_token = "";
                    signUpRequest.password = mPassword;
                }

                if (!TextUtils.isEmpty(mAvatarPath) && !CommonUtil.isUrlValid(mAvatarPath)) {
                    signUpRequest.avatar = PhotoUtils.getBase64FromBitmap(mAvatarPath);
                } else {
                    signUpRequest.avatar = "";
                }

                UserManager.shareInstance().signUpWithEmail(signUpRequest, "signUp", this);
            } else {
                ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
            }
        }
    }

    @Override
    public void didSignUpResponse(BaseResponse baseResponse) {
        if (mPDialog != null && mPDialog.isShowing()) {
            mPDialog.dismiss();
        }
        if (baseResponse == null) {
            ErrorUtil.showError(this, getString(R.string.msg_err_sign_up_fail));
        } else {
            if (baseResponse instanceof SignUpResponse) {
                Log.e("nhatdo", "didSygnUp " + ((SignUpResponse) baseResponse).getError());
                if (((SignUpResponse) baseResponse).getError() == null) {
                    SharedPrefUtil.saveBoolean(this, AppConstant.PREF_LOGIN_STATUS, true);
                    SharedPrefUtil.saveInt(this, AppConstant.PREF_ID, ((SignUpResponse) baseResponse).getId());
                    SharedPrefUtil.saveString(this, AppConstant.PREF_TOKEN, ((SignUpResponse) baseResponse).getToken());
                    SharedPrefUtil.saveString(this, AppConstant.PREF_USERNAME, ((SignUpResponse) baseResponse).getFirstName() + " " + ((SignUpResponse) baseResponse).getLastName());
                    SharedPrefUtil.saveString(this, AppConstant.PREF_CITY, ((SignUpResponse) baseResponse).getCountryName());
                    SharedPrefUtil.saveString(this, AppConstant.PREF_AVATAR, ((SignUpResponse) baseResponse).getAvatar() == null ? null : ((SignUpResponse) baseResponse).getAvatar().toString());
                    closeAllActivities();
                    UiUtil.showShortToast("Welcome to " + getString(R.string.app_name));
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(IntroductionActivity.START_ME, true);
                    Intent homeIntent = new Intent(this, HomeActivity.class);
                    homeIntent.putExtras(bundle);
                    startActivity(homeIntent);
                } else {
                    // error sign up
                    // ErrorUtil.showError(this, getString(R.string.msg_err_sign_up_fail));
                    String errorMessage = ((SignUpResponse) baseResponse).getError();
                    try {
                        JSONObject obj = new JSONObject(errorMessage);
                        String key;
                        if (obj != null) {
                            errorMessage = "";
                            Iterator i = obj.keys();
                            while (i.hasNext()) {
                                key = (String) i.next();
                                errorMessage += key + " " + obj.getString(key) + "\n";

                            }
                        }
                        ErrorUtil.showError(this, errorMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }
}
