package com.example.zhengjianhui.title.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhengjianhui.title.MainAdapter;
import com.example.zhengjianhui.title.R;
import com.example.zhengjianhui.title.db.DataBaseHelper;
import com.example.zhengjianhui.title.search.SearchResultActivity;
import com.example.zhengjianhui.title.type.TypeListActivity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;

    private ListView mainList;
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDatabaseNotExistsWithInit();
        initToolBar();
        initMainList();




    }

    private void checkDatabaseNotExistsWithInit() {
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    private void initMainList() {
        SQLiteDatabase db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from type where isExist = 1", null);

        List<Map<Integer, String>> datas = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            Map<Integer, String> data = new HashMap(1);
            data.put(cursor.getInt(0), cursor.getString(1));
            datas.add(data);
        }
        cursor.close();
        db.close();


        this.mainList = (ListView) findViewById(R.id.main_list);
        this.mainAdapter = new MainAdapter(datas, this);
        mainList.setAdapter(mainAdapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                // getItemAtPosition 方法对应的是 自定义 adapter 中的 getItem方法
                Map<Integer, String> data = (HashMap<Integer, String>) lv.getItemAtPosition(position);

                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(MainActivity.this, TypeListActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                for (Map.Entry<Integer, String> e : data.entrySet()) {
                    bundle.putInt("key", e.getKey());
                    bundle.putString("name", e.getValue());
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("法规库");
//        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
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
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                // 传递name参数为tinyphp
                bundle.putString("key", query);
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
