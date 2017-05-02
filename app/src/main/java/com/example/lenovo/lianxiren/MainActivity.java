package com.example.lenovo.lianxiren;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.phoneNumber;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<Contact> list;//所有联系人集合
    private ListView mContactListView;//联系人ListView
    private Map<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private TestCommonAdapter adapter;
    private Contact contact;
    static final String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};
    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;
    private SearchView mSearchView;
    private ListView listview;
    private Button button;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton ib1 = (ImageButton) findViewById(R.id.bohao);
        ImageButton imageButton = (ImageButton) findViewById(R.id.title_ib);
        listview=(ListView) findViewById(android.R.id.list) ;
        button=(Button)findViewById(R.id.title_bt);
        ImageButton imageButton1=(ImageButton)findViewById(R.id.geren) ;
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ReadMessage.class);
                startActivity(intent);
            }
        });

        mSearchView = (SearchView) findViewById(R.id.sv);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setBackgroundColor(0x22ff00ff);
        mCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, PROJECTION, null, null, null);
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, mCursor,
                new String[]{ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY}, new int[]{android.R.id.text1}, 0);
        /*listview.setAdapter(mAdapter);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mContactListView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                }

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
*/
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,sms.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT, Uri.parse
                        ("content://com.android.contacts/contacts/" + "1"));
                startActivity(intent);
            }
        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initData();
        initView();
        mContactListView = (ListView) findViewById(R.id.third_lv);
        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {android.Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }
        });
    }

    public void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086" ));///////////////////////////////
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        registerAllViewIds();
        registerAllViewAdapters();
    }

    private void registerAllViewIds() {
        mContactListView = (ListView) findViewById(R.id.third_lv);
    }

    private void registerAllViewAdapters() {
        adapter = new TestCommonAdapter(this, list, R.layout.fourth);
        mContactListView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initData() {
        ContactInfoService mContactInfoService = new ContactInfoService(this);
        list = mContactInfoService.getContractList();//返回手机联系人对象集合
        //按拼音首字母表排序
        Collections.sort(list, comparator);
    }

    Comparator<Contact> comparator = new Comparator<Contact>() {
        @Override
        public int compare(Contact t1, Contact t2) {
            String a = t1.getSortKey();
            String b = t2.getSortKey();
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);

            } else {
                return flag;

            }
        }
    };

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSearchView != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }
            mSearchView.clearFocus(); // 不获取焦点
        }
        return true;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String selection = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " LIKE '%" + newText + "%' " + " OR "
                + ContactsContract.RawContacts.SORT_KEY_PRIMARY + " LIKE '%" + newText + "%' ";
        mCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, PROJECTION, selection, null, null);
        mAdapter.swapCursor(mCursor); // 交换指针，展示新的数据
        return true;

    }

}





