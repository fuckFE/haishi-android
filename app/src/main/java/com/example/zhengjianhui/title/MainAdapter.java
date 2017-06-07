package com.example.zhengjianhui.title;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhengjianhui.title.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengjianhui on 17/6/4.
 */

public class MainAdapter extends BaseAdapter {

    private List<Map<Integer, String>> datas;
    private LayoutInflater mlayoutInflater;

    public MainAdapter(List<Map<Integer, String>> datas, Context context) {
        this.datas = datas;

        this.mlayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // mlayoutInflater.inflate 将一个xml 转换成view
//        View view = mlayoutInflater.inflate(R.layout.list_item, null);
//        TextView text = (TextView) view.findViewById(R.id.list_item);
//        text.setText(datas.get(position));

        MainListViewHolder holder;
        if (convertView == null) {
            convertView = mlayoutInflater.inflate(R.layout.list_item, null);
            TextView text = (TextView) convertView.findViewById(R.id.main_list_item);

//            text.setSingleLine();
            text.setMaxEms(25);
            text.setEllipsize(TextUtils.TruncateAt.END);

            holder = new MainListViewHolder();
            holder.setTextView(text);

            convertView.setTag(holder);
        } else {
            holder = (MainListViewHolder) convertView.getTag();
        }


        Map<Integer, String> data = datas.get(position);
        for (Map.Entry<Integer, String> e : data.entrySet()) {
            holder.getTextView().setText(e.getValue());
            holder.setId(e.getKey());
        }


        return convertView;
    }


    public void onDataChange(List<Map<Integer, String>> datas) {
        this.datas.addAll(datas);
        // 通知ListView 数据发生改变
        this.notifyDataSetChanged();
    }

    /**
     * 用于与  View convertView 建立关联 避免频繁的 findViewById
     * 这样能提高性能
     */
    protected class MainListViewHolder {

        private TextView textView;

        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
}