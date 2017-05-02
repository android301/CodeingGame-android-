package com.example.lenovo.lianxiren;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zbq on 2017/4/30.
 */

public class ReadMessage extends AppCompatActivity {
    ListView smslist ;
    ArrayList<Map<String, Object>> mData = new ArrayList<>();
    List<String> title = new ArrayList<String>();//短信来源  
    List<String> text = new ArrayList<String>();//短信内容  

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smslist = (ListView)findViewById(android.R.id.list);
        getSmsInPhone();
        int lengh = title.size();
        for(int i=0;i<lengh;i++){
            Map<String,Object> item=new HashMap<>();
            item.put("title",title.get(i));
            item.put("text",text.get(i));
            mData.add(item);
            SimpleAdapter adapter=new SimpleAdapter(this,mData,android.R.layout.simple_list_item_1,
                    new String[]{"title","text"},new int[]{android.R.id.text1,android.R.id.text2});
            smslist.setAdapter(adapter);
        }
    }

    @SuppressLint("LongLogTag")
    private void getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";
         /*final String SMS_URI_INBOX = "content://sms/inbox";     
      final String SMS_URI_SEND  = "content://sms/sent";     
      final String SMS_URI_DRAFT = "content://sms/draft";*/
        try {
            ContentResolver cr = getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                String name;
                String phoneNumber;
                String smsbody;
                String date;
                String type;
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                do {
                    phoneNumber = cur.getString(phoneNumberColumn);
                    name = getPeopleNameFromPerson(phoneNumber);
                    smsbody = cur.getString(smsbodyColumn);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    date = dateFormat.format(d);
                    int typeId = cur.getInt(typeColumn);
                    if (typeId == 1) {
                        type = "接收";
                    } else if (typeId == 2) {
                        type = "发送";
                    } else {
                        type = "草稿";
                    }
                    title.add(type + " " + date + '\n' + phoneNumber);
                    text.add(name + '\n' + smsbody);
                    if (smsbody == null) smsbody = "";
                } while (cur.moveToNext());
            }
            cur.close();
            cur = null;
        } catch (SQLiteDatabaseCorruptException e) {
            Log.e("SQLiteException in getSmsInPhone", e.getMessage());
        }

    }
    private  String getPeopleNameFromPerson(String address) {
        if (address == null || address == "") {
            return null;
        }
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String strPerson;
        Uri uri_Person = Uri.withAppendedPath
             (ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,address);//address手机号过滤
        Cursor cursor = getContentResolver().query(uri_Person, projection, null, null, null);
        if(cursor.moveToFirst()){
             int index_PeopleName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
             String strPeopleName = cursor.getString(index_PeopleName);
             strPerson = strPeopleName;
             }
         else{
             strPerson = address; }
         cursor.close();
         return strPerson;

    }
}
