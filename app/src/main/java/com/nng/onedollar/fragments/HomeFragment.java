package com.nng.onedollar.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.adapters.ProductAdapter;
import com.nng.onedollar.models.ProductModel;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.views.TMKLoadingView;
import com.nng.onedollar.webservices.WebServicesManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class HomeFragment extends BaseFragment {

	private View mRootView;
	private GridView mGvProduct;
	private ProductAdapter mAdapter;
	private TMKLoadingView mLoadingView;
	private ArrayList<ProductModel> mProducts;

	private String mProductListUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_home, container, false);
			mGvProduct = (GridView) mRootView.findViewById(R.id.gv_product);
			mAdapter = new ProductAdapter(getActivity());
			mGvProduct.setAdapter(mAdapter);

			mLoadingView = (TMKLoadingView) mRootView.findViewById(R.id.view_loading);

			mProducts = new ArrayList<ProductModel>();

			mProductListUrl = AppConstant.URL_BASE + AppConstant.URL_PRODUCT_LIST;
			executeGetProducts();
		}

		return mRootView;
	}

	private void executeGetProducts() {
		if (ConnectionUtil.hasInternetConnection(getActivity())) {
			mLoadingView.show();

			WebServicesManager.get(false, mProductListUrl, null, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					handleGetProductsSuccess(statusCode, headers, response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					handleGetProductsFailure(statusCode, headers, throwable);
				}
			});
		} else {
			ErrorUtil.showError(getActivity(), getString(R.string.msg_err_no_internet));
		}
	}

	private void handleGetProductsSuccess(int statusCode, Header[] headers, JSONObject response) {
		ResponseResult result = (new Gson().fromJson(response.toString(), ResponseResult.class));
		if (result.results != null) {
			mProducts.addAll(result.results);
		}

		if (TextUtils.isEmpty(result.next)) {
			mLoadingView.dismiss();
			mAdapter.setData(mProducts);
		} else {
			mProductListUrl = result.next;
			executeGetProducts();
		}
	}

	private void handleGetProductsFailure(int statusCode, Header[] headers, Throwable throwable) {
		mLoadingView.dismiss();
		mAdapter.setData(mProducts);
		ErrorUtil.showError(getActivity(), getString(R.string.msg_err_load_product_fail));
	}

	public class ResponseResult {

		public int count;
		public String next;
		public String previous;
		public ArrayList<ProductModel> results;
	}

}
