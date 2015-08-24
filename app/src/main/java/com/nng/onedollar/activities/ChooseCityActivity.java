package com.nng.onedollar.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.adapters.CityAdapter;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.views.TMKLoadingView;
import com.nng.onedollar.webservices.WebServicesManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseCityActivity extends BaseActivity {

	private ListView mLvCity;
	private CityAdapter mAdapter;
	private TMKLoadingView mLoadingView;
	private TextView mTvDone;
	private EditText mEtSearch;

	private String mCityCode;
	private ArrayList<Country> mData = new ArrayList<Country>();
	private ArrayList<Country> mFilteredData = new ArrayList<Country>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);
		overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

		mCityCode = getIntent().getStringExtra("code");

		mLoadingView = (TMKLoadingView) findViewById(R.id.view_loading);

		mEtSearch = (EditText) findViewById(R.id.et_search);
		mEtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String keyword = mEtSearch.getText().toString().trim();
				if (!TextUtils.isEmpty(keyword)) {
					filter(keyword);
				} else {
					mAdapter.setCheckedPosition(-1);
					mAdapter.setData(mData);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
			}
		});

		mTvDone = (TextView) findViewById(R.id.tv_done);
		mTvDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int checkedPosition = mAdapter.getCheckedPosition();
				Country checkedCountry;
				if (!mFilteredData.isEmpty()) {
					checkedCountry = mFilteredData.get(checkedPosition);
				} else {
					checkedCountry = mData.get(checkedPosition);
				}

				Intent data = new Intent();
				data.putExtra("name", checkedCountry.name);
				data.putExtra("code", checkedCountry.code);
				setResult(RESULT_OK, data);

				finish();
				overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
			}
		});

		mLvCity = (ListView) findViewById(R.id.lv_city);
		mLvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View row, int position, long index) {
				mTvDone.setEnabled(true);
				mAdapter.setCheckedPosition(position);
				mAdapter.notifyDataSetChanged();
			}
		});

		mAdapter = new CityAdapter(this);
		mLvCity.setAdapter(mAdapter);

		executeGetCountries();
	}

	private void filter(String keyword) {
		mFilteredData.clear();
		for (Country country : mData) {
			if (country.name.toLowerCase().contains(keyword.toLowerCase())) {
				mFilteredData.add(country);
			}
		}

		mAdapter.setCheckedPosition(-1);
		mAdapter.setData(mFilteredData);
	}

	private void executeGetCountries() {
		if (ConnectionUtil.hasInternetConnection(this)) {
			mLoadingView.show();

			WebServicesManager.get(true, AppConstant.URL_COUNTRIES, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
					super.onSuccess(statusCode, headers, response);
					handleGetCountriesSuccessful(statusCode, headers, response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					handleGetCountriesFailure(statusCode, headers, throwable);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					handleGetCountriesFailure(statusCode, headers, throwable);
				}
			});
		} else {
			ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
		}
	}

	private void handleGetCountriesSuccessful(int statusCode, Header[] headers, JSONArray response) {
		mLoadingView.dismiss();

		if (response != null) {
			try {
				Country country;
				for (int i = 0; i < response.length(); i++) {
					country = new Country(response.getJSONObject(i));
					mData.add(country);
					if (country.code.equals(mCityCode)) {
						mAdapter.setCheckedPosition(i);
					}
				}
				mAdapter.setData(mData);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else {
			mData.clear();
			mAdapter.setData(mData);
		}
	}

	private void handleGetCountriesFailure(int statusCode, Header[] headers, Throwable throwable) {
		mLoadingView.dismiss();
		ErrorUtil.showError(this, getString(R.string.msg_err_get_countries_fail));
	}

	public static class Country {

		public Country(JSONObject json) throws JSONException {
			code = json.getString("code");
			name = json.getString("name");
		}

		public String code;
		public String name;
	}

}
