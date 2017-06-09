package com.example.zhengjianhui.title.search;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhengjianhui.title.BookBean;
import com.example.zhengjianhui.title.R;
import com.example.zhengjianhui.title.book.SearchBookActivity;
import com.example.zhengjianhui.title.MainAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengjianhui on 17/6/5.
 */

public class SearchResultActivity extends AppCompatActivity {

    private SearchView searchView;

    private ListView searchList;

    private MainAdapter searchAdapter;

    private View footer;

    private SQLiteDatabase db;

    private String[] params;

    private boolean isLast = false;

    private int totalCount = 0;

    private boolean isLoading = false;

    /**
     * 分页偏移量
     */
    private int offset = 0;

    /**
     * 每页查询数
     */
    private int limit = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        this.db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String query = bundle.getString("query");
        query = "印发 广东省";
        this.params = query.split(" ");

        initToolBar("查询结果");
        initSearchList(query);

    }


    private void initToolBar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        toolbar.setTitle(name);
//        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
    }


    private void initSearchList(String query) {
        List<BookBean> datas = query();

        this.searchList = (ListView) findViewById(R.id.search_result_list);

        // 添加底部loading 栏
        LayoutInflater inflater = LayoutInflater.from(searchList.getContext());
        this.footer = inflater.inflate(R.layout.search_result_footer, null);
        // 隐藏loading 条
        this.footer.findViewById(R.id.search_result_loading).setVisibility(View.GONE);
        this.searchList.addFooterView(footer);

        this.searchAdapter = new MainAdapter(datas, this);
        this.searchList.setAdapter(searchAdapter);

        final String finalQuery = query;
        this.searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                BookBean data = (BookBean) lv.getItemAtPosition(position);


                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(SearchResultActivity.this, SearchBookActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putInt("key", data.getKey());
                bundle.putString("name", data.getName());
                bundle.putString("highlighted", finalQuery);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        this.searchList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLast) {
                    return;
                }

                // 到达最后一项，并且停止滚动
                if (searchList.getLastVisiblePosition() + 1 == totalCount && scrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true;
                        // 显示出 loading 条
                        footer.findViewById(R.id.search_result_loading).setVisibility(View.VISIBLE);
                        // 加载数据
                        List<BookBean> datas = query();
                        if (!datas.isEmpty()) {
                            searchAdapter.onDataChange(datas);
                            loadcomplete();
                        } else {
                            loadcomplete();
                            LayoutInflater inflater = LayoutInflater.from(searchList.getContext());
                            footer = inflater.inflate(R.layout.search_result_footer2, null);
                            // 隐藏loading 条
                            footer.findViewById(R.id.search_result_loading2).setVisibility(View.VISIBLE);
                            searchList.addFooterView(footer);

                            isLast = true;

                        }

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                totalCount = totalItemCount;
            }
        });

    }

    private void loadcomplete() {
        // 放开加载限制
        isLoading = false;
        // 隐藏 loading 条
        footer.findViewById(R.id.search_result_loading).setVisibility(View.GONE);
    }

    private List<BookBean> query() {
        String condition1 = "laws_title like";
        String condition2 = "laws_content like";
        StringBuilder sql = new StringBuilder("select laws_id, laws_title from laws where 1 = 1 ");

        for (int i = 0; i < params.length; i++) {
            sql.append("and (").append(condition1).append(" '%").append(params[i]).append("%'")
                    .append(" or ").append(condition2).append(" '%").append(params[i]).append("%') ");
        }

        sql.append("order by laws_id ").append("limit ").append(limit).append(" offset ").append(offset == 0 ? 0 : offset);
        offset += limit;

        Cursor cursor = db.rawQuery(sql.toString(), null);

        List<BookBean> datas = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            BookBean data = new BookBean();
            data.setKey(cursor.getInt(0));
            data.setName(cursor.getString(1));
            datas.add(data);
        }

        return datas;
    }


    /**
     * 初始化菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initSearchView(menu);

        return true;
    }

    private void initSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                // 传递name参数为tinyphp
                bundle.putString("query", query);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    /**
     * 处理 toolbar 中显示 item 图标
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //hasToolbar 是用来判断该布局是否包含toolbar。
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    /**
     * 处理toolbar 中菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 17/6/5 zjh 监听toolbar

        switch (item.getItemId()) {
            case R.id.toolbar_add:
                System.out.println("add");
                System.out.println("add");
                System.out.println("add");
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();

    }
}
