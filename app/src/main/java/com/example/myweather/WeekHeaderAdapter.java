package com.example.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeekHeaderAdapter extends RecyclerView.Adapter<WeekHeaderAdapter.HeaderViewHolder> {

    private List<WeekHeaderItem> hList;
    private List<WeekItem> items = new ArrayList<>();

    public WeekHeaderAdapter(List<WeekHeaderItem> hList) {
        this.hList = hList;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //최상위 카테고리의 xml레이아웃을 연결, 반환
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item_row, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {

        // 파라미터 배열 각 요소의 위치를 얻어옴
        WeekHeaderItem headerItem = hList.get(position);

        holder.tv_day.setText(headerItem.getDay());
        holder.tv_date.setText(headerItem.getDate());
//        holder.tv_max.setText(headerItem.getMax().substring(0,2).replace(".","")+"℃");
//        holder.tv_min.setText(headerItem.getMin().substring(0,2).replace(".","")+"℃  / ");
        holder.tv_max.setText(headerItem.getMax().replace(".0","")+"℃");
        holder.tv_min.setText(headerItem.getMin().replace(".0","")+"℃  / ");

        //날씨 아이콘 설정 (sky만)
        switch (headerItem.getSky()){
            case "맑음":
                holder.iv_icon.setImageResource(R.drawable.sky_1_small);
                break;
            case "구름많음":
                holder.iv_icon.setImageResource(R.drawable.sky_3_small);
                break;
            case "흐림":
                holder.iv_icon.setImageResource(R.drawable.sky_4_small);
                break;
        }


        // 접었다 펼치기
        boolean isExpandable = headerItem.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable? View.VISIBLE: View.GONE);

        if(isExpandable){
            holder.Arrow.setImageResource(R.drawable.up);

        }else {
            holder.Arrow.setImageResource(R.drawable.down);
        }

        // 하위 아이템을 상위 아이템에 연결
        // ( 중첩 recyclerView )
        WeekItemAdapter itemAdapter= new WeekItemAdapter(items);
        holder.itemRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setAdapter(itemAdapter);

        holder.Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerItem.setExpandable(!headerItem.isExpandable());
                items = headerItem.getChildItemList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hList.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
//        private LinearLayout linearLayout;
        private RelativeLayout expandableLayout;
        private TextView tv_date,tv_day, tv_min, tv_max;
        private ImageView Arrow, iv_icon; // 화살표
        private RecyclerView itemRecyclerView; // 하위 아이템이 있는 RecyclerView
        private ProgressBar progressBar;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
//            linearLayout= itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout); //접었다 펼칠 레이아웃
            tv_date = itemView.findViewById(R.id.date);
            tv_day = itemView.findViewById(R.id.day);
            tv_min= itemView.findViewById(R.id.n_temp_min);
            tv_max= itemView.findViewById(R.id.n_temp_max);
            Arrow = itemView.findViewById(R.id.toggle_expand);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            itemRecyclerView = itemView.findViewById(R.id.child_rv); //recyclerview 내부에 중첩된 recyclerview
            progressBar= itemView.findViewById(R.id.n_progressBar);

        }


    }
}
