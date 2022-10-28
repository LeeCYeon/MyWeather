package com.example.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekItemAdapter extends RecyclerView.Adapter<WeekItemAdapter.ViewHolder>{

    private List<WeekItem> itemList;

    public WeekItemAdapter(List<WeekItem> itemList) {
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_nested_item, parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeekItem item = itemList.get(position);

        holder.hourTxt.setText(item.getHour()+"h");
        holder.tmpTxt.setText(item.getTmp()+ "℃");


        holder.progressBar.setProgress(Integer.valueOf(item.getTmp())*5);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tmpTxt, hourTxt;
        ProgressBar progressBar;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        RecyclerView itemRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourTxt = itemView.findViewById(R.id.n_hour);
            tmpTxt = itemView.findViewById(R.id.head_temp);

            progressBar = itemView.findViewById(R.id.n_progressBar);

            itemRecyclerView = itemView.findViewById(R.id.child_rv);

            linearLayout= itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

        }

    }

}
