package com.ren.face.constant;


public class Constant {


    /**
     *  人脸识别
     *  Api  接口处
     */
    public static final String API_KEY ="0uLYRjI429QqNDH8afscDucW";


    public static final String SERCET_KEY = "KX1aGogSXaSxkU8GkHN4V7p7gIaSdnmw";


    public static final String ADD_URL="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";


    public static  final String SEARCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/search";


    public static final String FACE_GROUP = "student_check";


    public static final String GET_USER_LIST_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/getusers";


    public static final String DEL_USER_URL= "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete";





    /**
     *
     * 数据库创建管理
     *
     */
    public static final String db_name="face_db";


    public static final String t_account="t_account";


    public static final String t_time = "t_time";


    /**
     *
     * 登录请求
     *
     */

    public static final Integer REQ_CODE_RES = 111;


    public static final Integer RES_CODE_RES = 222;


    /**
     *
     *  调用 相册相机
     *
     */
    public static final Integer REQ_PIC_CODE = 333;

    public static final Integer REQ_ALU_CODE= 444;


    /**
     *
     *   权限代码
     *
     */

    public static final int ERR = 0;

    public static final int ROLE_STUDENT =1;

    public static  final int ROLE_TEACHER =2;

    public static final int ROLE_ADMIN = 3;


    /**
     *
     *  日期查询
     *
     */

    public static final int TYPE_YEAR_MONTH = 1;

    public static final int TYPE_YEAR_MONTH_DAY = 0;

}
