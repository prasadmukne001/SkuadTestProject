package com.example.skuadtestproject.network;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.skuadtestproject.network.response.NearByApiResponse;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Rahul on 15-02-2018.
 */

public class ApiRequests {

    public static GsonRequest<NearByApiResponse> getNearByApiResponse(
            @NonNull final String url,
            @NonNull final String requestBody,
            @NonNull final Response.Listener<NearByApiResponse> listener,
            @NonNull final Response.ErrorListener errorListener) {

        return new GsonRequest<>(
                Request.Method.GET,
                url + requestBody,
                null,
                null,
                new TypeToken<NearByApiResponse>() {
                }.getType(),
                listener,
                errorListener
        );
    }
}
