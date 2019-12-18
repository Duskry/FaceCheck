package com.ren.face.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ren.face.bean.Student;
import com.ren.face.dao.StudentDao;

/**
 *   人脸匹配的回调类
 */
public class SearchHandler extends Handler {
    private Context context;
    StudentDao studentDao;
    Student student;
    public SearchHandler(Context context,Student student){
        this.context = context;
        studentDao = new StudentDao(context);
        this.student = student;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 0:{
                Toast.makeText(context,"匹配成功,打卡成功",Toast.LENGTH_SHORT).show();
                //  打卡成功  应该写入 数据库
                studentDao.insertTime(student);
                //  账户  名字   时间
                break;
            }
            case -1:{
                Toast.makeText(context,"人脸和当前账号不匹配",Toast.LENGTH_SHORT).show();
                break;
            }
            default:{
                Toast.makeText(context,"识别错误错误代码为"+msg.what,Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
