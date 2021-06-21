package com.example.skuadtestproject;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.skuadtestproject.adapter.PlacesListAdapter;
import com.example.skuadtestproject.application.AppController;
import com.example.skuadtestproject.network.ApiRequests;
import com.example.skuadtestproject.network.GsonRequest;
import com.example.skuadtestproject.network.response.NearByApiResponse;
import com.example.skuadtestproject.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_NEAR_BY_API_TAG = "REQUEST_NEAR_BY_API_TAG";
    private Unbinder unbinder;
    private PlacesListAdapter adapter;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.places_listing)
    RecyclerView mPlacesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initView();
        getNearByRestaurants();
    }

    private void initView(){
        adapter = new PlacesListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPlacesRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getNearByRestaurants() {
        if (Utility.isNetworkConnected(this)) {
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            String requestBody = getNearByPlacesApiRequestBody();

            final GsonRequest<NearByApiResponse> gsonRequest =
                    ApiRequests.getNearByApiResponse(
                            url,
                            requestBody,
                            new Response.Listener<NearByApiResponse>() {
                                @Override
                                public void onResponse(NearByApiResponse response) {
                                    setScratchCardClaimDataResponse(response);
                                }
                            }
                            ,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    if (isFinishing()) return;

                                    try {
                                        Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

            AppController.addRequest(gsonRequest, REQUEST_NEAR_BY_API_TAG);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    private String getNearByPlacesApiRequestBody() {
        String requestBody = "location=47.6204,-122.3491&radius=2500&type=restaurant&key=AIzaSyDkGIvqAXuuOE5TUoDedazelbPdKtQxb1E";
        return requestBody;
    }

    void setScratchCardClaimDataResponse(NearByApiResponse response) {
        if (isFinishing())
            return;

        if (response == null) {
            return;
        }

        if(response.status.equals("OK")){

        }

        if(response.results.size() == 0){
            Toast.makeText(this, getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }else{
            if (response.results.size() != 0) {
                adapter.setItems(response.results);
                mPlacesRecyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.cancelAllRequests(REQUEST_NEAR_BY_API_TAG);
        unbinder.unbind();
    }
}