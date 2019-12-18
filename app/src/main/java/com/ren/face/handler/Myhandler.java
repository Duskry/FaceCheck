package com.ren.face.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;


/**
 *   上传人脸的回调类
 */
public class Myhandler extends Handler {
    private Context context;
    public Myhandler(Context context){
        this.context=context;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:{
                Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
                break;
            }
            // 百度的人脸识别错误码
            case 223105:{
                Toast.makeText(context,"已经有你的图片了",Toast.LENGTH_SHORT).show();
                break;
            }
            default:{
                Toast.makeText(context,"上传失败请重新上传错误代码为"+msg.what,Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }
}
