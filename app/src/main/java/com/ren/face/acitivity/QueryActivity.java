package com.ren.face.acitivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ren.face.R;
import com.ren.face.adapter.CheckAdapter;
import com.ren.face.bean.Student;
import com.ren.face.dao.StudentDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 *   查询 Activity
 *
 * */
public class QueryActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    TextView datepick;

    TextView datetext;

    TextView res;

    TextView queryaccount;

    CheckAdapter checkAdapter;

    ListView listView;

    StudentDao studentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        datepick = findViewById(R.id.querybydate);
        datetext = findViewById(R.id.datetext);
        listView = findViewById(R.id.listview);
        res = findViewById(R.id.res);
        studentDao = new StudentDao(this);
        queryaccount = findViewById(R.id.queryaccount);
        // 先不展示数据
        checkAdapter = new CheckAdapter(this, null, 1);
        listView.setAdapter(checkAdapter);
    }

    public void chooseTime(View view) {
        Log.d(TAG, "chooseTime: " + "选择日期");
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);

                // 选择 后改变cursor
                checkAdapter.changeCursor( studentDao.getCheckTime(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), 0));

                datetext.setText("当前查询的日期为："+DateFormat.format("yyyy-MM-dd", c));
                // res.setText(studentDao.getCheckTime((String) DateFormat.format("yyy-MM-dd", c),0));

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void chooseAccount(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText1 = new EditText(this);
        editText1.setHint("账户");
        builder.setTitle("查询的账户")
                .setMessage("请输入要查询账户的账号")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: "+editText1.getText().toString());
                        checkAdapter.changeCursor(studentDao.getCheckTimeByAccunt(editText1.getText().toString()));
                        datetext.setText("当前查询的账户为："+editText1.getText().toString());
                    }
                })
                .setView(editText1).create().show();
    }

}
