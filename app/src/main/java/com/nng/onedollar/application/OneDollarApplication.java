package com.nng.onedollar.application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.graphics.Bitmap;

public class OneDollarApplication extends Application {

	private static OneDollarApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initImageLoader();
	}

	public static OneDollarApplication getInstance() {
		return mInstance;
	}

	//TODO : try using ion ' https://github.com/koush/ion '
	private void initImageLoader() {
		//TODO : replace ImageLoader by Picasso
		DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
		optionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		optionsBuilder.cacheInMemory(true);
		optionsBuilder.cacheOnDisk(true);
		optionsBuilder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		optionsBuilder.considerExifParams(true);
		DisplayImageOptions options = optionsBuilder.build();

		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext());
		configBuilder.defaultDisplayImageOptions(options);
		configBuilder.threadPriority(Thread.NORM_PRIORITY - 2);
		configBuilder.denyCacheImageMultipleSizesInMemory();
		configBuilder.tasksProcessingOrder(QueueProcessingType.LIFO);
		configBuilder.memoryCache(new WeakMemoryCache());
		ImageLoaderConfiguration config = configBuilder.build();

		ImageLoader.getInstance().init(config);
	}
	
}
