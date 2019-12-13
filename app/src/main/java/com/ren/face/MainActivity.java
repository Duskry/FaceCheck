package com.ren.face;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ren.face.acitivity.Menu;
import com.ren.face.acitivity.Register;
import com.ren.face.bean.Student;
import com.ren.face.dao.StudentDao;
import com.ren.face.database.FaceHelper;

import static com.ren.face.constant.Constant.REQ_CODE_RES;
import static com.ren.face.constant.Constant.RES_CODE_RES;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG="main";

    EditText account;

    EditText pwd;

    Button  login;

    Button regsiter;

    StudentDao studentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account =  findViewById(R.id.account);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        regsiter = findViewById(R.id.register);
        studentDao = new StudentDao(this);

        login.setOnClickListener(this);
        regsiter.setOnClickListener(this);
    }

    //  listener
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login :{
                login();
                break;
            }
            case R.id.register:{
                Log.d(TAG, "onClick: 注册界面");
                Intent intent = new Intent(this, Register.class);
                startActivityForResult(intent,REQ_CODE_RES);
                break;
            }

            default:break;
        }
    }
    // login
    public void login(){
        if(account.getText()!=null && pwd.getText()!=null){
            // 密码及权限校验

            Integer rscode=studentDao.isRight(account.getText().toString(),pwd.getText().toString());
            Intent intent = new Intent(this, Menu.class);
            Student student = studentDao.getStudent(account.getText().toString());
            student.setRole(rscode);
            intent.putExtra("student", student);
            switch (rscode){
                // 超级管理
                case 3:{

                }
                //  老师
                case 2:{

                }
                // 学生
                case 1:{
                    startActivity(intent);
                    break;
                }
                case 0:{
                    Toast.makeText(this,"你的密码错误",Toast.LENGTH_LONG).show();
                    break;
                }
                default:break;
            }

        }else {
            Toast.makeText(this,"账号或者密码为空",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE_RES && resultCode==RES_CODE_RES){

        }
    }
}
