package com.girish.aphotograph.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.girish.aphotograph.MyApplication;
import com.girish.aphotograph.R;
import com.girish.aphotograph.activity.ViewActivity;
import com.girish.aphotograph.adapter.RecyclerAdapter;
import com.girish.aphotograph.adapter.RecyclerClickListener;
import com.girish.aphotograph.adapter.TouchListener;
import com.girish.aphotograph.extra.CheckConnection;
import com.girish.aphotograph.extra.EndPoints;
import com.girish.aphotograph.extra.QueryParamsUtil;
import com.girish.aphotograph.util.ParseDataModel;
import com.girish.aphotograph.util.RetrofitUtil;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Popular extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ParseDataModel dataModel;
    RetrofitUtil util;
    Call<ParseDataModel> dataModelCall;
    Button retryFetch;
    LinearLayout connectionError;

    public Popular() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recyclerView = view.findViewById(R.id.popular_recycle_view);
        progressBar = view.findViewById(R.id.popular_progress);
        connectionError = view.findViewById(R.id.no_network);
        retryFetch = view.findViewById(R.id.retry_connection);

        retryFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(EndPoints.BY_CREAED_AT);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        Retrofit retrofit = MyApplication.getRetrofitApiClient();

        util = retrofit.create(RetrofitUtil.class);

        if (savedInstanceState != null) {
            dataModel = Parcels.unwrap(savedInstanceState.getParcelable("com.girish.aphotograph.editors"));
            if (dataModel != null) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new RecyclerAdapter(getContext(), dataModel));
                recyclerView.scheduleLayoutAnimation();
            } else
                sendRequest(EndPoints.BY_CREAED_AT);
        } else {
            sendRequest(EndPoints.BY_CREAED_AT);
        }

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), recyclerView, new TouchListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast.makeText(getContext(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                intent.putExtra("com.girish.aphotograph.URL", dataModel.details.get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void sendRequest(String sortBy) {
        if (CheckConnection.isInternetAvailable(getContext())) {

            switch (sortBy) {
                case EndPoints.BY_CREAED_AT:
                    dataModelCall = util.listGetData(QueryParamsUtil.getQuery(EndPoints.POPULAR,
                            EndPoints.BY_CREAED_AT));
                    break;
                case EndPoints.BY_RATING:
                    dataModelCall = util.listGetData(QueryParamsUtil.getQuery(EndPoints.POPULAR,
                            EndPoints.BY_RATING));
                    break;
                case EndPoints.BY_TIMES_VIEWED:
                    dataModelCall = util.listGetData(QueryParamsUtil.getQuery(EndPoints.POPULAR,
                            EndPoints.BY_TIMES_VIEWED));
                    break;
            }

            if (dataModelCall != null) {
                dataModelCall.enqueue(new Callback<ParseDataModel>() {
                    @Override
                    public void onResponse(Call<ParseDataModel> call, Response<ParseDataModel> response) {
                        dataModel = response.body();
                        progressBar.setVisibility(View.GONE);
                        connectionError.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new RecyclerAdapter(getContext(), dataModel));
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onFailure(Call<ParseDataModel> call, Throwable t) {
                        Log.i("Error", t.getMessage());
                    }
                });
            } else {
                connectionError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            connectionError.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void sortData(String sortBy) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        sendRequest(sortBy);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("com.girish.aphotograph.editors", Parcels.wrap(dataModel));
    }

}
