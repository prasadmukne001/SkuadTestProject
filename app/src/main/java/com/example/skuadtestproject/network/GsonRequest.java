package com.example.skuadtestproject.network;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Prasad on 21-06-2021.
 */

public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson gson;
    private final Type type;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    private boolean isCacheChanged = false;
    private int cacheHit;
    private int cacheExpiry;

    /**
     * @param method        - GET/POST/PUT/DELETE
     * @param url           - server URL
     * @param headers       - request header
     * @param requestBody   - post parameters
     * @param type          - class represents server response
     * @param listener      - listener for success case
     * @param errorListener - listener for failure case
     */
    public GsonRequest(int method,
                       @NonNull final String url,
                       @NonNull final Map<String, String> headers,
                       final String requestBody,
                       @NonNull final Type type,
                       @NonNull final Response.Listener<T> listener,
                       @NonNull final Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        GsonBuilder gsonBuilder = new GsonBuilder();

        this.gson = gsonBuilder.create();
        this.type = type;
        this.headers = headers;
        this.listener = listener;

    }


    /**
     * @param method        - GET/POST/PUT/DELETE
     * @param url           - server URL
     * @param headers       - request header
     * @param requestBody   - post parameters
     * @param clazz         - class represents server response
     * @param listener      - listener for success case
     * @param errorListener - listener for failure case
     * @param cacheHit      - time milliseconds, after this time cache will be hit, but also refreshed on background_login.
     *                      0 for CacheHit = CacheExpiry case
     * @param cacheExpiry   - time milliseconds, after this time cache entry expires completely
     */
    public GsonRequest(int method,
                       @NonNull final String url,
                       @NonNull final Map<String, String> headers,
                       final String requestBody,
                       @NonNull final Class<T> clazz,
                       @NonNull final Response.Listener<T> listener,
                       @NonNull final Response.ErrorListener errorListener,
                       int cacheHit, int cacheExpiry) {
        this(method, url, headers, requestBody, clazz, listener, errorListener);
        this.isCacheChanged = true;
        this.cacheHit = cacheHit;
        this.cacheExpiry = cacheExpiry;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return super.getRetryPolicy();
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(20 * 1000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success
                    (
                            gson.fromJson(json, type),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
