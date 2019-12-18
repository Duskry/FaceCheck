package com.ren.face.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ren.face.bean.Student;
import com.ren.face.database.FaceHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ren.face.constant.Constant.ERR;
import static com.ren.face.constant.Constant.TYPE_YEAR_MONTH;
import static com.ren.face.constant.Constant.TYPE_YEAR_MONTH_DAY;
import static com.ren.face.constant.Constant.t_account;
import static com.ren.face.constant.Constant.t_time;

/**
 *  学生 数据库 操作类
 */
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
         *  常量类
         *  0  密码错误
         *  1  学生
         *  2  老师
         *  3  超级管理员
         *
         */
        String qAccount="select pwd,role from "+t_account+" where account = " +account ;
        Cursor cursor  =db.rawQuery(qAccount,null);
        String cpwd = null;
        Integer role=ERR;
        while(cursor.moveToNext()){
             cpwd= cursor.getString(cursor.getColumnIndex("pwd"));
             role= cursor.getInt(cursor.getColumnIndex("role"));
        }
        Log.d(TAG, "isRight: ");
        if(cpwd!=null && cpwd.equals(pwd)){
            Log.d(TAG, "isRight: 校验前后密码输入密码"+pwd+"数据库密码"+cpwd);
            Log.d(TAG, "isRight: 权限"+role);
            return role;
        }else {
            return ERR;
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
     *  选择 具体哪天 或者 月查询
     * @param time
     * @return cursor
     */
    public Cursor getCheckTime(String time,Integer type){

        Log.d(TAG, "getCheckTime: 正在查询 "+time);
        Log.d(TAG, "getCheckTime: Type是"+type);
        // 格式 是  2019-12-12
        // 0  年月日
        // 1 年月
        String sql="";
        if(type==TYPE_YEAR_MONTH_DAY){
            sql = "select checkid as _id,account,name,checktime from "+t_time+" where STRFTIME('%Y-%m-%d', checktime) = '"+time+"'";
        }
        // 只查询年月
        else if(type==TYPE_YEAR_MONTH){
            sql = "select checkid as _id,account,name,checktime from "+t_time+" where STRFTIME('%Y-%m', checktime) = '"+time.substring(0,time.length()-3)+"'";
        }
        else{
            Log.d(TAG, "getCheckTime: 时间选择类型错误");
        }
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }


    /**
     *
     *  账户查询 打卡信息
     * @param account
     * @return
     */
    public Cursor getCheckTimeByAccunt(String account){
        String sql = "select checkid as _id,account,name,checktime from "+t_time+" where account = "+account;
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }
}
