package com.example.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    private int size=0;
    private ArrayList<NewsItem> listViewItemList = new ArrayList<NewsItem>();

    public NewsListAdapter() {
        size= listViewItemList.size();
    }


    // listviewitem 항목개수
    @Override
    public int getCount() {
        return size;
    }

    // position 위치의 item 값을 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // position 위치의 item 의 row id값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position 위치의 item항목을 View 형식으로 얻어온다.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        final Context context = parent.getContext();

        // listview_item의 layout을 inflate하여 xml을 view로 만들고 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_listview_row,parent,false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tv_press = (TextView) convertView.findViewById(R.id.tv_press);
        TextView tv_headline= (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_content= (TextView) convertView.findViewById(R.id.tv_content);
        ImageView iv_newsImg =(ImageView) convertView.findViewById(R.id.iv_img);

        // Data Set (listViewItemList) 에서 position에 위치한 데이터참조 획득
        NewsItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tv_press.setText(listViewItem.getPressTxt());
        tv_headline.setText(listViewItem.getTitleTxt());
        tv_content.setText(listViewItem.getConTxt());

        //이미지 url을 설정 **이 부분 이미지 url에 값은 들어옴
        Glide.with(context).load(listViewItem.getImg_url()).apply(new RequestOptions().transforms(new CenterCrop(),
                new RoundedCorners(13))).into(iv_newsImg);



        return convertView;
    }

    //    // item 데이터 추가를 위한 함수
    public void addItem(String press, String title, String cont,String url,String img_url) {
        NewsItem item = new NewsItem();
        item.setPressTxt(press);
        item.setTitleTxt(title);
        item.setConTxt(cont);
        item.setUrl(url);
        item.setImg_url(img_url);

        listViewItemList.add(item);

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        size= listViewItemList.size();
    }

    //    // item 삭제
//    public void delItem() {
//        listViewItemList.clear();
//    }
    public void refreshAdapter(List<NewsItem> newsItems) {
        this.listViewItemList.clear();
        this.listViewItemList.addAll(newsItems);
        notifyDataSetChanged();
    }

    public void addItem(NewsItem listViewItem) {
        listViewItemList.add(listViewItem);
    }
}