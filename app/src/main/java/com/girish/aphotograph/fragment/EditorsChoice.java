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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
public class EditorsChoice extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Retrofit retrofit;
    ParseDataModel dataModel;
    Call<ParseDataModel> dataModelCall;
    Button retryFetch;
    LinearLayout connectionError;
    RetrofitUtil util;

    public EditorsChoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editors_choice, container, false);

        setRetainInstance(true);

        recyclerView = view.findViewById(R.id.editors_recycle_view);
        progressBar = view.findViewById(R.id.editors_progress);
        connectionError = view.findViewById(R.id.no_network);
        retryFetch = view.findViewById(R.id.retry_connection);

        retryFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(EndPoints.BY_CREATED_AT);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.grid_animate_from_bottom);
        recyclerView.setLayoutAnimation(controller);

        retrofit = MyApplication.getRetrofitApiClient();
        util = retrofit.create(RetrofitUtil.class);

        if (savedInstanceState != null) {
            dataModel = Parcels.unwrap(savedInstanceState.getParcelable("com.girish.aphotograph.editors"));
            if (dataModel != null) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new RecyclerAdapter(getContext(), dataModel));
                recyclerView.scheduleLayoutAnimation();
            } else {
                sendRequest(EndPoints.BY_CREATED_AT);
            }
        } else {
            sendRequest(EndPoints.BY_CREATED_AT);
        }

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), recyclerView, new TouchListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast.makeText(getContext(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                intent.putExtra("com.girish.aphotograph.URL", dataModel.details.get(position).getUrl());
                intent.putExtra("com.girish.aphotograph.ID", dataModel.details.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void sendRequest(String sortBy) {
        if (CheckConnection.isInternetAvailable(getActivity())) {
            switch (sortBy) {
                case EndPoints.BY_CREATED_AT:
                    dataModelCall = util.listGetData(QueryParamsUtil.getGeneralQuery(EndPoints.EDITORS,
                            EndPoints.BY_CREATED_AT));
                    break;
                case EndPoints.BY_RATING:
                    dataModelCall = util.listGetData(QueryParamsUtil.getGeneralQuery(EndPoints.EDITORS,
                            EndPoints.BY_RATING));
                    break;
                case EndPoints.BY_TIMES_VIEWED:
                    dataModelCall = util.listGetData(QueryParamsUtil.getGeneralQuery(EndPoints.EDITORS,
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
        try {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            sendRequest(sortBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("com.girish.aphotograph.editors", Parcels.wrap(dataModel));
    }
}
