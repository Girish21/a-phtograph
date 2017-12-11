package com.girish.aphotograph.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.girish.aphotograph.R;
import com.girish.aphotograph.util.ParseDataModel;

import java.util.List;

/**
 * Created by Girish on 11-Dec-17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ParseDataModel dataModelList;
    private Context context;

    public RecyclerAdapter(Context context, ParseDataModel dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            Glide.with(context)
                    .load(dataModelList.details.get(position).getUrl())
                    .into(holder.imageView);
            holder.titleText.setText(dataModelList.details.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dataModelList.details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleText;

        MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.card_image);
            titleText = itemView.findViewById(R.id.card_title);
        }
    }

}
