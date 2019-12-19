package com.ren.face.acitivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ren.face.R;
import com.ren.face.bean.Student;
import com.ren.face.handler.Deletehandler;
import com.ren.face.handler.Gethandler;
import com.ren.face.handler.SearchHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static com.ren.face.constant.Constant.REQ_ALU_CODE;
import static com.ren.face.constant.Constant.REQ_PIC_CODE;
import static com.ren.face.constant.Constant.ROLE_ADMIN;
import static com.ren.face.constant.Constant.ROLE_STUDENT;
import static com.ren.face.constant.Constant.ROLE_TEACHER;
import static com.ren.face.service.Face.runDelete;
import static com.ren.face.service.Face.runGet;
import static com.ren.face.service.Face.runSerach;
import static com.ren.face.utils.ImageUtil.getBitmapFormUri;

/**
 *
 *   菜单
 *
 */
public class Menu extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Menu:";

    //@BindView(R.id.registerface)
    TextView regface;

    //@BindView(R.id.query)
    TextView query;

    TextView delete;


    //@BindView(R.id.check_in_pic)
    TextView inByPic;

    //@BindView(R.id.check_in_byal)
    TextView inByAlbum;


    Student student;


    Handler handler;


    Handler  getHandler;

    Uri imageUri;

   // @BindView(R.id.image)
    ImageView imageView;
    // 图片
    Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        //ButterKnife.bind(this);

        delete = findViewById(R.id.deleteface);

        regface = findViewById(R.id.registerface);

        query = findViewById(R.id.query);

        inByPic = findViewById(R.id.check_in_pic);

        inByAlbum = findViewById(R.id.check_in_byal);

        imageView = findViewById(R.id.image);

        Intent now = getIntent();
        if (now != null) {
            student = (Student) now.getSerializableExtra("student");
            handler = new SearchHandler(this, student);
        }
        getHandler = new Gethandler(this,student);
        Log.i(TAG, "onCreate: 权限是" + student.getRole());
        // 隐藏不该有的控件
        shouldVisble(student.getRole());

        Student student1 = student;


        runGet(getHandler,student1);
        /*

        监听器
         */
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QueryActivity.class);
                startActivity(intent);
            }
        });
        inByAlbum.setOnClickListener(this);

        inByPic.setOnClickListener(this);

        regface.setOnClickListener(this);

        delete.setOnClickListener(this);


    }

    /*
      权限 是否可视
     */
    private void shouldVisble(Integer role) {
        switch (role) {
            case ROLE_ADMIN:
            case ROLE_TEACHER:
                break;
            // 学生
            case ROLE_STUDENT: {
                // 简单做了一下  这里需要修改一下 有些还没隐藏
                delete.setVisibility(View.INVISIBLE);
                regface.setVisibility(View.INVISIBLE);
                query.setVisibility(View.INVISIBLE);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 相册导入
            case R.id.check_in_byal: {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_ALU_CODE);
                break;
            }
            // 拍照导入
            case R.id.check_in_pic: {
                // 动态申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


                /*StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();            //7.0拍照必加*/

                Intent openFileIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                openFileIntent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.TITLE, imageFileName);
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                openFileIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openFileIntent, REQ_PIC_CODE);
                break;
            }
            case R.id.registerface: {

                Intent intent = new Intent(Menu.this, RegisterFace.class);
                intent.putExtra("student", student);
                startActivity(intent);
                break;
            }
            case  R.id.deleteface:{
                final EditText editText = new EditText(this);
                editText.setHint("账户");
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this).setTitle("删除用户").setView(editText)
                        .setMessage("请注意，删除会删除用户的全部人脸！")
                        .setPositiveButton("删除人脸", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                runDelete(new Deletehandler(Menu.this), editText.getText().toString());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQ_PIC_CODE) {
            Log.d(TAG, "onActivityResult: 拍照已经进入");
            Log.d(TAG, "onActivityResult: " + imageUri);

            try {
                // 原图 bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                // 需要进行图片压缩
                bitMap = getBitmapFormUri(imageUri, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onActivityResult: 拍照图片压缩失败");
            }

            // 检验图片
            runSerach(bitMap, student.getAccount(), handler);

            // imageView.setImageBitmap(bitMap);
        } else if (requestCode == REQ_ALU_CODE) {
            imageUri = data.getData();
            try {
                bitMap = getBitmapFormUri(imageUri, this);
                Log.d(TAG, "onActivityResult: 图片压缩成功！");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onActivityResult: 相册图片压缩失败");
            }
            runSerach(bitMap, student.getAccount(), handler);

        }
    }


}
