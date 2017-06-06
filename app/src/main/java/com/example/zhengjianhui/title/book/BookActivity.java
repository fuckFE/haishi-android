package com.example.zhengjianhui.title.book;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.zhengjianhui.title.R;
import com.example.zhengjianhui.title.search.SearchResultActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhengjianhui on 17/6/4.
 */

public class BookActivity extends AppCompatActivity {

    private SearchView searchView;

    private RecyclerView bookView;

    private Button onARecord;

    private Button underARecord;

    private LinearLayoutManager linearLayoutManager;

    private List<Integer> recordPositions = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        int key = bundle.getInt("key");
        String name = bundle.getString("name");
        String highlighted = bundle.getString("highlighted");

        initToolBar(name);

        List<Spanned> datas = initData(key, highlighted);
        ininView(datas, highlighted);
    }

    private void initToolBar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle(name);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
    }

    private List<Spanned> initData(int key, String highlighted) {
        SQLiteDatabase db = openOrCreateDatabase("laws.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select laws_content from laws where laws_id = ?", new String[]{String.valueOf(key)});

        String data = null;
        while (cursor.moveToNext()) {
            data = cursor.getString(0);
        }
        cursor.close();
        db.close();

        if (data == null) {
            return Collections.EMPTY_LIST;
        }

        String[] highlighteds = null;
        if (highlighted != null) {
            highlighteds = highlighted.split(" ");
        }

        List<Spanned> lines = new ArrayList<>();
        String[] datas = data.split("</p>");
        for (int i = 0; i < datas.length; i++) {
            String record = datas[i] + "</p>";
            lines.add(dealHtmlString(record, highlighteds));

            if(regex(record, highlighteds)) {
                recordPositions.add(i);
            }

        }

        return lines;
    }

    private Boolean regex(String record, String[] highlighteds) {
        if(highlighteds == null) {
            return Boolean.FALSE;
        }

        for (String regex : highlighteds) {
//            if() {
                return record.matches(regex) == true ? Boolean.TRUE : Boolean.FALSE;
//            }
        }


        return Boolean.FALSE;
    }

    private Spanned dealHtmlString(String html, String[] highlighteds) {
        if (highlighteds != null) {
            for (String s : highlighteds) {
                html = html.replace(s, "<font color='#e87400'>" + s + "</font>");
            }

        }

        return Html.fromHtml(html);
    }


    private void ininView(List<Spanned> datas, String highlighted) {
        bookView = (RecyclerView) findViewById(R.id.bookView);
        this.linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookView.setLayoutManager(linearLayoutManager);

        BookAdapter bookAdapter = new BookAdapter(this, datas, highlighted);
        bookView.setAdapter(bookAdapter);

        bookView.setItemAnimator(new DefaultItemAnimator());
        bookView.setHasFixedSize(true);
    }


    /**
     * 移动页面到 position 指定位置
     *
     * @param manager
     * @param position
     */
    public void MoveToPosition(LinearLayoutManager manager, int position) {
        bookView.smoothScrollToPosition(position);
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
                Intent intent = new Intent(BookActivity.this, SearchResultActivity.class);
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
