package com.nng.onedollar.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nng.onedollar.R;
import com.nng.onedollar.fragments.CartFragment;
import com.nng.onedollar.fragments.HomeFragment;
import com.nng.onedollar.fragments.InboxFragment;
import com.nng.onedollar.fragments.NotificationFragment;
import com.nng.onedollar.models.UserModel;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements OnClickListener {

    private DrawerLayout mDrawerLayout;
    private TextView mTvTitle;
    private ImageView mIvMenu, mIvSearch;
    private ImageView mIvHome, mIvCart, mIvInbox, mIvNotification,mIvAdd;
    private View mLineHome, mLineCart, mLineInbox, mLineNotification;
    private ArrayList<ImageView> mBottomBarItems;
    private ArrayList<View> mBottomBarLines;
    private TextView mTvUserName, mTvUserCity;
    private ImageView mIvAvatar;

    private HomeFragment mFragmentHome;
    private CartFragment mFragmentCart;
    private InboxFragment mFragmentInbox;
    private NotificationFragment mFragmentNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IntroductionActivity.START_ME)) {
            boolean isStartHome = intent.getExtras().getBoolean(IntroductionActivity.START_ME);
            if (isStartHome) {
                startActivity(new Intent(getApplicationContext(), IntroductionActivity.class));
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mIvMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        findViewById(R.id.rl_profile).setOnClickListener(this);
        findViewById(R.id.ll_claim_cash).setOnClickListener(this);
        findViewById(R.id.ll_get_extra_credit).setOnClickListener(this);
        findViewById(R.id.ll_help).setOnClickListener(this);
        findViewById(R.id.ll_sign_out).setOnClickListener(this);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this);

        mTvUserName = (TextView) findViewById(R.id.tv_name);

        mTvUserCity = (TextView) findViewById(R.id.tv_city);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);

        mIvHome = (ImageView) findViewById(R.id.iv_home);
        mIvHome.setOnClickListener(this);
        mIvCart = (ImageView) findViewById(R.id.iv_cart);
        mIvCart.setOnClickListener(this);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mIvAdd.setOnClickListener(this);
        mIvInbox = (ImageView) findViewById(R.id.iv_inbox);
        mIvInbox.setOnClickListener(this);
        mIvNotification = (ImageView) findViewById(R.id.iv_notification);
        mIvNotification.setOnClickListener(this);
        mBottomBarItems = new ArrayList<ImageView>();
        mBottomBarItems.add(mIvHome);
        mBottomBarItems.add(mIvCart);
        mBottomBarItems.add(mIvInbox);
        mBottomBarItems.add(mIvNotification);

        mLineHome = (View) findViewById(R.id.view_line_home);
        mLineCart = (View) findViewById(R.id.view_line_cart);
        mLineInbox = (View) findViewById(R.id.view_line_inbox);
        mLineNotification = (View) findViewById(R.id.view_line_notification);
        mBottomBarLines = new ArrayList<View>();
        mBottomBarLines.add(mLineHome);
        mBottomBarLines.add(mLineCart);
        mBottomBarLines.add(mLineInbox);
        mBottomBarLines.add(mLineNotification);

        mFragmentHome = new HomeFragment();
        mFragmentCart = new CartFragment();
        mFragmentInbox = new InboxFragment();
        mFragmentNotification = new NotificationFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentHome).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentCart).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentInbox).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentNotification).commit();

        // init bottom bar state
        showOrHideFragment(HomeFragment.class.getName());

        printHashKey();
    }
    private void printHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("nhatdo", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void setBottomBarLineSelected(View line) {
        for (View view : mBottomBarLines) {
            view.setVisibility(View.GONE);
        }
        line.setVisibility(View.VISIBLE);
    }

    private void setBottomBarItemSelected(ImageView item) {
        for (ImageView view : mBottomBarItems) {
            view.setSelected(false);
        }
        item.setSelected(true);
    }

    private void showOrHideFragment(String fragment) {
        getSupportFragmentManager().beginTransaction().hide(mFragmentHome).commit();
        getSupportFragmentManager().beginTransaction().hide(mFragmentCart).commit();
        getSupportFragmentManager().beginTransaction().hide(mFragmentInbox).commit();
        getSupportFragmentManager().beginTransaction().hide(mFragmentNotification).commit();

        mIvSearch.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);

        if (HomeFragment.class.getName().equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(mFragmentHome).commit();
            setBottomBarItemSelected(mIvHome);
            setBottomBarLineSelected(mLineHome);
            mIvSearch.setVisibility(View.VISIBLE);
        } else if (CartFragment.class.getName().equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(mFragmentCart).commit();
            setBottomBarItemSelected(mIvCart);
            setBottomBarLineSelected(mLineCart);
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(R.string.fragment_cart_title);
        } else if (InboxFragment.class.getName().equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(mFragmentInbox).commit();
            setBottomBarItemSelected(mIvInbox);
            setBottomBarLineSelected(mLineInbox);
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(R.string.fragment_inbox_title);
        } else if (NotificationFragment.class.getName().equals(fragment)) {
            getSupportFragmentManager().beginTransaction().show(mFragmentNotification).commit();
            setBottomBarItemSelected(mIvNotification);
            setBottomBarLineSelected(mLineNotification);
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(R.string.fragment_notification_title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInforUser();
    }

    private void showInforUser(){
        String jsonUserLogin = SharedPrefUtil.loadString(this, AppConstant.USER_LOGIN, null);
        if (jsonUserLogin == null) {
            return;
        }
        UserModel  userModel = (new Gson().fromJson(jsonUserLogin, UserModel.class));
        Glide.with(this).load(userModel.avatar).placeholder(R.drawable.ic_launcher).into(mIvAvatar);
        mTvUserCity.setText(userModel.country_name);
        mTvUserName.setText(userModel.getFullName());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                showOrHideFragment(HomeFragment.class.getName());
                break;

            case R.id.iv_cart:
                showOrHideFragment(CartFragment.class.getName());
                break;
            case R.id.iv_add:
                Intent sellItemActivity = new Intent(getApplicationContext(),SellItemActivity.class);
                startActivity(sellItemActivity);
                break;

            case R.id.iv_inbox:
                showOrHideFragment(InboxFragment.class.getName());
                break;

            case R.id.iv_notification:
                showOrHideFragment(NotificationFragment.class.getName());
                break;

            case R.id.rl_profile:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                break;

            case R.id.ll_claim_cash:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ClaimCashActivity.class));
                break;

            case R.id.ll_get_extra_credit:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.ll_help:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.ll_sign_out:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                SharedPrefUtil.saveBoolean(this, AppConstant.PREF_LOGIN_STATUS, false);
                closeAllActivities();
                startActivity(new Intent(this, StartActivity.class));
                break;
        }
    }

    @Override
    protected void deAlloc() {
        mDrawerLayout = null;
        mIvHome = null;
        mIvMenu = mIvSearch = mIvHome = mIvCart = mIvInbox = mIvNotification = mIvAvatar = null;
        mLineHome = mLineCart = mLineInbox = mLineNotification = null;
        if (mBottomBarItems != null) {
            for (ImageView img : mBottomBarItems) {
                img = null;
            }
        }
        mBottomBarItems = null;
        if (mBottomBarLines != null) {
            for (View img : mBottomBarLines) {
                img = null;
            }
        }
        mBottomBarLines = null;
        mTvUserName = mTvUserCity = null;
    }
}
