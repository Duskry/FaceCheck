package com.ren.face.service;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.ren.face.bean.AddResult;
import com.ren.face.bean.SearchResult;
import com.ren.face.bean.Student;
import com.ren.face.bean.UserListResult;
import com.ren.face.utils.Base64Util;
import com.ren.face.utils.GsonUtils;
import com.ren.face.utils.HttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ren.face.constant.Constant.ADD_URL;
import static com.ren.face.constant.Constant.DEL_USER_URL;
import static com.ren.face.constant.Constant.FACE_GROUP;
import static com.ren.face.constant.Constant.GET_USER_LIST_URL;
import static com.ren.face.constant.Constant.SEARCH_URL;
import static com.ren.face.service.AuthService.getAuth;
import static com.ren.face.utils.ImageUtil.getBytesByBitmap;

/**
 *  添加人脸和鉴别人脸类
 *
 */
public class Face {



    /**
     *  添加人脸
     * @param name  名字
     * @param account  账户
     * @param bitmap 人脸位图
     * @param handler  上传成功的消息回传
     *
     */

    public static void addFace(String name, String account, Bitmap bitmap,Handler handler) {
        // 错误代码
        Integer code = null;
        // 请求url
        String url = ADD_URL;
        Message message = new Message();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", Base64Util.encode(getBytesByBitmap(bitmap)));
            // 组
            map.put("group_id", FACE_GROUP);
            // 用户标识
            map.put("user_id", account);
            // 用户信息 ，存储名字
            map.put("user_info", name);
            map.put("liveness_control", "NORMAL");

            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");
            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            // 获得权限
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            // 返回结果
            Gson gson = new Gson();
            AddResult addResult = gson.fromJson(result,AddResult.class);
            System.out.println(addResult);
            code = addResult.getError_code();
            message.what=0;
            // 上传成功
            if(code==0) {
                message.what=1;

            }else {
                message.what=code;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
    }

    /**
     *
     *   在人脸库中查询
     * @param bitmap  该人脸的位图
     * @param account 账户
     * @param handler  消息回传
     */
    public static void faceSearch(Bitmap bitmap,String account,Handler handler) {
        // 请求url
        String url = SEARCH_URL;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", Base64Util.encode(getBytesByBitmap(bitmap)));
            map.put("liveness_control", "NORMAL");
            map.put("group_id_list", FACE_GROUP);
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            Gson gson = new Gson();
            SearchResult searchResult = gson.fromJson(result,SearchResult.class);
            Integer code =searchResult.getError_code();
            Message message = new Message();
            message.what=-1;
            // 成功
            if(code==0){
                SearchResult.ResultBean.UserListBean userListBean =searchResult.getResult().getUser_list().get(0);
                // 账号相等
                if(userListBean.getUser_id().equals(account)){
                    // 相识度大于90 才认为 是本人
                    if(userListBean.getScore()>90)
                        message.what=0;

                }
            }else{
                // 设置 百度给的错误代码
                message.what=code;
            }
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *
     *  获得组中所有用户
     *
     * @param handler
     * @param student
     */
    public static void getUserList(Handler handler, Student student){
        String url = GET_USER_LIST_URL;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("group_id", FACE_GROUP);
            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            Gson gson = new Gson();
            Message message = new Message();
            //  用户表中没有用户的信息
            message.what=0;
            UserListResult userListResult= gson.fromJson(result,UserListResult.class);
            for (String string: userListResult.getResult().getUser_id_list()
                 ) {
                if(string.equals(student.getAccount())){
                    //  用户表中有
                    message.what=1;
                }
            }
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除用户
     * @param handler
     * @param account
     */
    public static void deleteUser(Handler handler, String account){
        System.out.println(account);
        String url = DEL_USER_URL;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("group_id", FACE_GROUP);
            map.put("user_id",account);
            String param = GsonUtils.toJson(map);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            Gson gson = new Gson();
            Message message = new Message();
            AddResult addResult = gson.fromJson(result,AddResult.class);
            if(addResult.getError_code()==0){
                message.what=0;

            }else {
                message.what =  addResult.getError_code();
            }
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
        Android 规范 ，不能在主线程中上传文件等耗时操作，会严重影响体验
        必须使用线程，使用方式多样
     */
    public static void runUpload(final String name, final String account, final Bitmap bitmap, final Handler handler){
        // 启动线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                addFace(name,account,bitmap,handler);
            }
        }).start();
    }
    public static void runSerach(final Bitmap bitmap, final String account, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                faceSearch(bitmap,account,handler);
            }
        }).start();
    }

    public static void runGet(final Handler handler, final Student student){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserList(handler,student);
            }
        }).start();
    }
    public static void runDelete(final Handler handler, final String account){
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteUser(handler,account);
            }
        }).start();
    }
}
