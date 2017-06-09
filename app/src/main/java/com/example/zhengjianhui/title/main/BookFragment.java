package com.example.zhengjianhui.title.main;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhengjianhui.title.BookBean;
import com.example.zhengjianhui.title.MainAdapter;
import com.example.zhengjianhui.title.R;
import com.example.zhengjianhui.title.type.TypeListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookFragment extends Fragment {

    private ListView mainList;

    private MainAdapter mainAdapter;

    public static BookFragment newInstance(List<BookBean> datas) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();

        ArrayList list = new ArrayList(1);
        list.add(datas);

        args.putParcelableArrayList("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    public BookFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList list = getArguments().getParcelableArrayList("list");
        List<BookBean> datas = (List<BookBean>) list.get(0);
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        initMainList(view, datas);

        return view;
    }


    private void initMainList(View view, List<BookBean> datas) {
        this.mainList = (ListView) view.findViewById(R.id.main_list);
        this.mainAdapter = new MainAdapter(datas, getActivity());
        mainList.setAdapter(mainAdapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                // getItemAtPosition 方法对应的是 自定义 adapter 中的 getItem方法
                BookBean data = (BookBean) lv.getItemAtPosition(position);

                // getActivity() 获取包含这个 Fragment 的Activity
                Intent intent = new Intent(getActivity(), TypeListActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                bundle.putInt("key", data.getKey());
                bundle.putString("name", data.getName());

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


}