package com.example.zhengjianhui.title.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ListView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zhengjianhui.title.BookBean;
import com.example.zhengjianhui.title.MainAdapter;
import com.example.zhengjianhui.title.R;
import com.example.zhengjianhui.title.db.DataBaseHelper;
import com.example.zhengjianhui.title.search.SearchResultActivity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private SearchView searchView;

    private ListView mainList;

    private MainAdapter mainAdapter;

    private BookFragment bookFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDatabaseNotExistsWithInit();
        initToolBar();
        initNavigationBar();
    }

    public List<BookBean> getMainListDatas() {
        SQLiteDatabase db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from type where isExist = 1", null);

        List<BookBean> datas = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()) {
            BookBean data = new BookBean();
            data.setKey(cursor.getInt(0));
            data.setName(cursor.getString(1));
            datas.add(data);
        }
        cursor.close();
        db.close();

        return datas;
    }

    void initNavigationBar() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.tab_settings_normal, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.tab_settings_normal, "Books"))
                .addItem(new BottomNavigationItem(R.drawable.tab_settings_normal, "Music"))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }


    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        bookFragment = bookFragment.newInstance(getMainListDatas());

        transaction.replace(R.id.tb, bookFragment);
        transaction.commit();
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

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (bookFragment == null) {
                    bookFragment = BookFragment.newInstance(getMainListDatas());
                }
                transaction.replace(R.id.tb, bookFragment);
                break;
            case 1:
                if (bookFragment == null) {

                    List<BookBean> datas = new ArrayList<>(5);
                    BookBean data = new BookBean();
                    data.setName("222222");
                    bookFragment = BookFragment.newInstance(datas);
                }
                transaction.replace(R.id.tb, bookFragment);
                break;
            case 2:
                if (bookFragment == null) {
                    List<BookBean> datas = new ArrayList<>(5);
                    BookBean data = new BookBean();
                    data.setName("33333");
                    bookFragment = BookFragment.newInstance(datas);
                }
                transaction.replace(R.id.tb, bookFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
