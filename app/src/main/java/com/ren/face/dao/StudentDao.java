package com.ren.face.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ren.face.bean.Student;
import com.ren.face.bean.StudentCheck;
import com.ren.face.database.FaceHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ren.face.constant.Constant.t_account;
import static com.ren.face.constant.Constant.t_time;

public class StudentDao {
    private static final String TAG = "StudentDao";
    public StudentDao(Context context){
        faceHelper=new FaceHelper(context);
        db=faceHelper.getWritableDatabase();
    }

    private FaceHelper faceHelper;

    private SQLiteDatabase db;


    /**
     *
     *  注册用户
     *
     * @param account
     * @param name
     * @param pwd
     * @param role
     */
    public void insertAccount(String account,String name ,String pwd,Integer role){
        String sql="insert into "+t_account+"(account,name,pwd,role) values(?,?,?,?)";
        db.execSQL(sql,new Object[]{account,name,pwd,role});
        db.close();
        Log.d(TAG, "insertAccount: 插入账户成功，账户"+account);
    }


    /**
     *  打卡记录成功
     *  插入 time表
     * @param student
     */
    public void insertTime(Student student){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String checktime = df.format(new Date());
        String sql="insert into "+t_time+"(account,name,checktime) values(?,?,?)";
        Log.d(TAG, "insertTime: 插入的时间为"+checktime);
        db.execSQL(sql,new Object[]{student.getAccount(),student.getName(),checktime});
        db.close();
        Log.d(TAG, "insertTime: 插入打卡表成功");
    }


    /**
     *  密码 核对
     * @param account
     * @param pwd
     * @return
     * @throws SQLException
     */
    public Integer isRight(String account,String pwd) throws SQLException {
        /**
         *  返回代码说明
         *  这些 也应该写入常量类
         *  0  密码错误
         *  1  学生
         *  2  老师
         *  3  超级管理员
         *
         */
        String qAccount="select pwd,role from "+t_account+" where account = " +account ;
        Cursor cursor  =db.rawQuery(qAccount,null);
        String cpwd = null;
        Integer role=0;
        while(cursor.moveToNext()){
             cpwd= cursor.getString(0);
             role= cursor.getInt(1);
        }
        Log.d(TAG, "isRight: ");
        if(cpwd!=null && cpwd.equals(pwd)){
            Log.d(TAG, "isRight: 校验前后密码输入密码"+pwd+"数据库密码"+cpwd);
            Log.d(TAG, "isRight: 权限"+role);
            return role;
        }else {
            return 0;
        }
    }

    /**
     *  获取 学生  信息
     * @param account
     * @return
     */
    public Student getStudent(String account){
        Student student = new Student();
        student.setAccount(account);
        String sql = "select name from "+t_account+" where account = "+account;
        Cursor cursor = db.rawQuery(sql,null);
        String name = "";
        while (cursor.moveToNext()){
            name = cursor.getString(0);
        }
        student.setName(name);
        return student;
    }


    /**
     *  选择 具体哪天
     * @param time
     * @return
     */
    public StudentCheck getCheckTime(String time){
        // 格式 是  2019-12-12
        String sql = "select account,name from "+t_time+" where STRFTIME('%Y-%m-%d', checktime) = "+time;
        Cursor cursor = db.rawQuery(sql,null);

        return null;
    }


}
