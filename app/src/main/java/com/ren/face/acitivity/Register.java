package com.ren.face.acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ren.face.R;
import com.ren.face.dao.StudentDao;

import static com.ren.face.constant.Constant.RES_CODE_RES;

public class Register extends AppCompatActivity {


    EditText account;

    EditText pwd;

    EditText name;

    EditText role;

    Button re;

    StudentDao studentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = findViewById(R.id.r_account);
        pwd = findViewById(R.id.r_pwd);
        name = findViewById(R.id.r_name);
        role = findViewById(R.id.r_role);
        re= findViewById(R.id.reg);
        studentDao = new StudentDao(this);

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentDao.insertAccount(account.getText().toString(),name.getText().toString(),pwd.getText().toString(),Integer.parseInt(role.getText().toString()));
                Toast.makeText(v.getContext(),"注册成功",Toast.LENGTH_LONG).show();
              /*  Intent intent = new Intent(v.getContext(),Menu.class);
                intent.putExtra("role",Integer.parseInt(role.getText().toString()));
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);*/
                finish();
            }
        });

    }
}
