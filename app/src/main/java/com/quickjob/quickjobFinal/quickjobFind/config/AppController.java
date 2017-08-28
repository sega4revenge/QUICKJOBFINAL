package com.quickjob.quickjobFinal.quickjobFind.config;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.quickjob.quickjobFinal.quickjobFind.pref.ConnectivityReceiver;


public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();
    public static Context context;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
    public AppController(){

    }
	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
        context=getApplicationContext();
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	private AppController(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
			private final LruCache<String, Bitmap> cache = new LruCache<>(20);

			@Override
			public Bitmap getBitmap(String url) {
				return cache.get(url);
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				cache.put(url, bitmap);
			}
		});
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
//	@Override
//	protected void attachBaseContext(Context base) {super.attachBaseContext(base);}
//	public ImageLoader getImageLoader() {
//		getRequestQueue();
//		if (mImageLoader == null) {
//			mImageLoader = new ImageLoader(this.mRequestQueue,
//					new LruBitmapCache());
//		}
//		return this.mImageLoader;
//	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;
	}
}