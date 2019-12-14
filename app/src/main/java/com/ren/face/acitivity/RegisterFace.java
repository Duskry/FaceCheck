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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ren.face.R;
import com.ren.face.bean.Student;
import com.ren.face.handler.Myhandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ren.face.constant.Constant.REQ_ALU_CODE;
import static com.ren.face.constant.Constant.REQ_PIC_CODE;
import static com.ren.face.service.Face.runUpload;
import static com.ren.face.utils.ImageUtil.getBitmapFormUri;

public class RegisterFace extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterFace";

    @BindView(R.id.addbyPic)
    Button addByPic;

    @BindView(R.id.addbyAul)
    Button addByAul;

    @BindView(R.id.addfaceimg)
    ImageView imageView;

    @BindView(R.id.upload)
    Button upload;


    @BindView(R.id.button4)
    Button button4;

    Uri imageUri;

    Bitmap bitMap;

    Student student;


    ProgressBar progressBar;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_face);
        ButterKnife.bind(this);
        student = (Student) getIntent().getSerializableExtra("student");
        handler= new Myhandler(this);
        addByAul.setOnClickListener(this);
        addByPic.setOnClickListener(this);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                    Log.d(TAG, "onClick: 不为空");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("确认？").setMessage("是否确认上传").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(RegisterFace.this,"正在上传",Toast.LENGTH_SHORT).show();
                            runUpload(student.getName(),student.getAccount(),bitMap,handler);
                        }
                    }).setNegativeButton("否",null).show();
                }else{
                    Toast.makeText(v.getContext(),"你还没有添加图片哦！",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode==REQ_PIC_CODE){
            try {
                // 源图 bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                // 需要进行图片压缩
                bitMap = getBitmapFormUri(imageUri,this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onActivityResult: 拍照图片压缩失败");
            }

            imageView.setImageBitmap(bitMap);


            //  拍照上传 图片
           //  runUpload(student.getName(),student.getAccount(),bitMap);


        }
        else if(requestCode==REQ_ALU_CODE){
            imageUri=data.getData();
            try {
                bitMap = getBitmapFormUri(imageUri,this);
                Log.d(TAG, "onActivityResult: 图片压缩成功！");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onActivityResult: 相册图片压缩失败");
            }


             // runUpload(student.getName(),student.getAccount(),bitMap);

            // 相册上传图片
            imageView.setImageBitmap(bitMap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 相册导入
            case R.id.addbyAul: {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_ALU_CODE);
                break;
            }
            // 拍照导入
            case R.id.addbyPic: {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();            //7.0拍照必加

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
        }
    }
}
