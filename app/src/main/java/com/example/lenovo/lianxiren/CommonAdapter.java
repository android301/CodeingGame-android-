package com.example.lenovo.lianxiren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Lenovo on 2017/4/13.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mcontext;
    protected List mDatas;
    protected LayoutInflater mInlater;
    private int layoutId;
    public CommonAdapter(Context context ,List datas,int layoutId) {
        this.mcontext = context;
        this.mDatas= datas;
        this.layoutId = layoutId;
        mInlater = LayoutInflater.from(context);
    }


    public Context getMcontext() {
        return mcontext;
    }


    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }


    public List getmDatas() {
        return mDatas;
    }


    public void setmDatas(List mDatas) {
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int arg0) {
        return (T)mDatas.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public  View getView(int postion, View convertView, ViewGroup parent){
        ViewHolder holder = ViewHolder.get(mcontext, convertView, parent, postion, layoutId);
        convert(holder, getItem(postion));
        return holder.getmConvertView();
    }
    public abstract void convert(ViewHolder holder,T t);
}


