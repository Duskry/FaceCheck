package com.ren.face.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ren.face.R;



/**
 *
 *  简单起见不用bean类 接受cursor 直接用适配器展示进视图 建议换种实现 如换成recycleview
 *
 */
public class CheckAdapter extends CursorAdapter {

    //  显示两种颜色  随便实现的 有点问题
    private static int flag=1;

    private final String TAG = getClass().getSimpleName();
    public CheckAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout linearLayoutCompat = (LinearLayout) layoutInflater.inflate(R.layout.itemlayout,parent,false);
        if(flag==1){
            linearLayoutCompat.setBackgroundColor(Color.rgb(207,207,207));
            flag=-flag;
        }else{
            linearLayoutCompat.setBackgroundColor(Color.rgb(255,255,255));
            flag=-flag;
        }
        return linearLayoutCompat;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String account = cursor.getString(1);
        String name  = cursor.getString(2);
        String date = cursor.getString(3);
        Log.d(TAG, "bindView: "+date);

        TextView accountview = view.findViewById(R.id.checkaccount);
        accountview.setText(account);
        TextView nameview = view.findViewById(R.id.checkname);
        nameview.setText(name);
        TextView timeview = view.findViewById(R.id.checktime);
        timeview.setText(date);
    }
}
