package com.example.zhengjianhui.title.book;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhengjianhui.title.R;

import java.util.List;

/**
 * Created by zhengjianhui on 17/5/26.
 */

public class BookAdapter extends RecyclerView.Adapter<MyViewHolder> {


    private List<Spanned> datas;

    private LayoutInflater mlayoutInflater;

    private Context mContext;


    public BookAdapter(Context mContext, List<Spanned> datas) {
        this.datas = datas;
        this.mContext = mContext;

        mlayoutInflater = LayoutInflater.from(mContext);

    }

    /**
     * 创建一个 ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mlayoutInflater.inflate(R.layout.item_textview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public MyViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }
}
