package com.example.lenovo.lianxiren;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbq on 2017/4/23.
 */

public class ContactInfoService {
    private Context context;

    public ContactInfoService(Context context) {
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public List<Contact> getContractList() {

        List<Contact> list = new ArrayList<>();
        Contact contact;
        ContentResolver content=context.getContentResolver();
        // 首先,从raw_contacts中读取联系人的id("contact_id")

        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称

        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = content.query(uri, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                contact = new Contact();
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("display_name"));//获取联系人姓名
                String sortkey = cursor.getString(cursor.getColumnIndex("phonebook_label"));//这个字段保存了每个联系人首字的拼音的首字母
                contact.setName(name);
                contact.setSortKey(sortkey);
                Cursor dataCursor = content.query(dataUri, null, "raw_contact_id= ?", new String[]{id}, null);
                while (dataCursor.moveToNext()) {
                    String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                    if (type.equals("vnd.android.cursor.item/phone_v2")) {//如果得到的mimeType类型为手机号码类型才去接收
                        String phoneNum = dataCursor.getString(dataCursor.getColumnIndex("data1"));//获取手机号码
                        contact.setNumber(phoneNum);
                    }
                }
                dataCursor.close();
                if (contact.getName() != null && contact.getNumber() != null) {
                    list.add(contact);
                }
            }
            cursor.close();
        }
        return list;
    }
}

