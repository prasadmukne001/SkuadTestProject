package com.example.skuadtestproject.application;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.example.skuadtestproject.network.CustomHurlStack;
import com.example.skuadtestproject.network.LruBitmapCache;

/**
 * Created by Rahul on 15-02-2018.
 */

public class AppController extends Application {

    private static AppController mAppControllerInstance;
    private RequestQueue mRequestQueue;
    private LruBitmapCache mLruBitmapCache;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppControllerInstance = this;
    }


    public static AppController getInstance() {
        return mAppControllerInstance;
    }

    private static void addRequest(@NonNull final Request<?> request) {
        getInstance().getVolleyRequestQueue().add(request);
    }

    public static void addRequest(@NonNull final Request<?> request, @NonNull final String tag) {
        request.setTag(tag);
        addRequest(request);
    }

    public static void cancelAllRequests(@NonNull final String tag) {
        getInstance().getVolleyRequestQueue().cancelAll(tag);
    }

    public static Cache getCache() {
        return getInstance().getVolleyRequestQueue().getCache();
    }

    @NonNull
    public RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getCacheDir(), 20 * 1024 * 1024);
            Network network = new BasicNetwork(new CustomHurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    @NonNull
    public ImageLoader getVolleyImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader
                    (
                            getVolleyRequestQueue(),
                            AppController.getInstance().getVolleyImageCache()
                    );
        }

        return mImageLoader;
    }

    @NonNull
    private LruBitmapCache getVolleyImageCache() {
        if (mLruBitmapCache == null) {
            mLruBitmapCache = new LruBitmapCache(mAppControllerInstance);
        }

        return mLruBitmapCache;
    }


}
