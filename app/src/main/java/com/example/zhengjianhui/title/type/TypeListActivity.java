package com.example.zhengjianhui.title.type;

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
import com.example.zhengjianhui.title.book.BookActivity;
import com.example.zhengjianhui.title.MainAdapter;
import com.example.zhengjianhui.title.search.SearchResultActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengjianhui on 17/6/5.
 */

public class TypeListActivity extends AppCompatActivity {


    private SearchView searchView;

    private ListView typeList;
    private MainAdapter typeAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        int key = bundle.getInt("key");
        String name = bundle.getString("name");

        initToolBar(name);
        initTypeList(key);


    }

    private void initToolBar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(name);
//        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
    }

    private void initTypeList(int key) {
        SQLiteDatabase db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select laws_id, laws_title from laws where type_id = ?", new String[]{String.valueOf(key)});

        List<Map<Integer, String>> datas = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            Map<Integer, String> data = new HashMap(1);
            data.put(cursor.getInt(0), cursor.getString(1));
            datas.add(data);
        }
        cursor.close();
        db.close();

        this.typeList = (ListView) findViewById(R.id.type_list);
        this.typeAdapter = new MainAdapter(datas, this);
        typeList.setAdapter(typeAdapter);
        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                Map<Integer, String> data = (HashMap<Integer, String>) lv.getItemAtPosition(position);

                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(TypeListActivity.this, BookActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                for (Map.Entry<Integer, String> e : data.entrySet()) {
                    bundle.putInt("key", e.getKey());
                    bundle.putString("name", e.getValue());
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(TypeListActivity.this, SearchResultActivity.class);
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
