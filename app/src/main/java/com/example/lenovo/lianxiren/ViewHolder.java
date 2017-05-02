package com.example.lenovo.lianxiren;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by zbq on 2017/4/23.
 */
public class ViewHolder {
    //把用户自定义的 ViewHolder 和BaseAdapter 的声明等进行了封装，调用时，需要 CommonAdapter 和ViewHoler 这两个类。
        private SparseArray<View> mViews;
        private Context mcontext;
        private View mConvertView;
        private int mposition;

        public View getmConvertView() {
            return mConvertView;
        }

        public int getMposition() {
            return mposition;
        }

        public ViewHolder(Context context, ViewGroup parent, int layoutId,
                          int position) {
            this.mposition = position;
            this.mViews = new SparseArray<>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            mConvertView.setTag(this);//将view缓存起来
        }

        public static ViewHolder get(Context context, View convertView,
                                     ViewGroup parent, int position, int layoutId) {
            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId, position);
            } else {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.mposition = position;//修改位置变化
                return holder;
            }
        }

        public  <T extends View> T getView(int viewid) {
            View view = mViews.get(viewid);
            if (view == null) {
                view = mConvertView.findViewById(viewid);
                mViews.put(viewid, view);
            }
            return (T) view;
        }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


}
