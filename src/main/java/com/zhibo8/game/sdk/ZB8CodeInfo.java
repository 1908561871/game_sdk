package com.zhibo8.game.sdk;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8CodeInfo {

    public static int CODE_NOT_INSTALL = 1;//未安装
    public static int CODE_VERIFY_FAILURE = 3; //认证失败
    public static int CODE_UNINITIALIZED = 4; //未初始化sdk
    public static int CODE_AUTHORIZE_FAILURE = 5; //授权失败

    public static int CODE_PAY_FAILURE = 6; //支付失败

    public static int CODE_GET_GOODS_FAILURE = 8; //获取商品详情失败
    public static int CODE_GET_USER_INFO_ERROR = 10;
    public static int CODE_OTHER = 5; //其他
    public static int CODE_TEENAGER_PROTECT = 9;


    public static String MSG_NOT_INSTALL = "未安装直播吧";
    public static String MSG_VERIFY_FAILURE = "认证失败";
    public static String MSG_UNINITIALIZED = "未初始化sdk";
    public static String MSG_AUTHORIZE_FAILURE = "授权失败";
    public static String MSG_PAY_FAILURE = "支付失败";
    public static String MSG_GET_GOODS_FAILURE= "获取商品详情失败";
    public static String MSG_GET_USER_INFO_ERROR = "获取用户信息失败";
    public static String MSG_CODE_TEENAGER_PROTECT = "触发未成年保护";



}
