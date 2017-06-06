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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String query = bundle.getString("query");

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
        query = "港口 海事局";

        List<Map<Integer, String>> datas = query(query);
        if(datas.isEmpty()) {
            Map<Integer, String> data = new HashMap<>(1);
            data.put(1, "未能查询到结果");
            datas.add(data);
        }

        this.searchList = (ListView) findViewById(R.id.type_list4);
        this.searchAdapter = new MainAdapter(datas, this);
        searchList.setAdapter(searchAdapter);

        final String finalQuery = query;
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                Map<Integer, String> data = (HashMap<Integer, String>) lv.getItemAtPosition(position);


                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(SearchResultActivity.this, SearchBookActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                for (Map.Entry<Integer, String> e : data.entrySet()) {
                    bundle.putInt("key", e.getKey());
                    bundle.putString("name", e.getValue());
                    bundle.putString("highlighted", finalQuery);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private List<Map<Integer, String>> query(String query) {
        String[] params = query.split(" ");

        String sql1 = "select laws_id, laws_title from laws where 1 = 1";
        String condition1 = "laws_title like";
        String sql2 = "select laws_id, laws_title from laws where 1 = 1";
        String condition2 = "laws_content like";
        StringBuilder var1 = new StringBuilder();
        StringBuilder var2 = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            String data = params[i];
            var1.append(" and ").append(condition1).append(" '%" + data + "%'");
            var2.append(" and ").append(condition2).append(" '%" + data + "%'");
        }

        StringBuilder sql = new StringBuilder(sql1);
        sql.append(var1.toString()).append(" UNION ").append(sql2).append(var2);

        SQLiteDatabase db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery(sql.toString(), null);

        List<Map<Integer, String>> datas = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            Map<Integer, String> data = new HashMap(1);
            data.put(cursor.getInt(0), cursor.getString(1));
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
}
