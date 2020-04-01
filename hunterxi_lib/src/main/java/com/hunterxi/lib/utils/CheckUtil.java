package com.hunterxi.lib.utils;

/**
 * @author HunterXi
 * 创建日期：2019/5/15 16:50
 * 描述：检查、检测工具类
 */
public class CheckUtil {

    public static final String CHECK_SUCCESS = "OK";

    /**
     * 初步验证手机号
     * @param phoneNum
     * @return
     */
    public static String phoneCheck(String phoneNum) {
        if (phoneNum.length() != 11) {
            return "手机号长度不对";
        }

        // 首位1开头，后面10位是0-9
        if (!phoneNum.matches("^(1)\\d{10}$")){
            return "手机号有误，请重新输入";
        }

        return CHECK_SUCCESS;
    }

    /**
     * 初步验证短信验证码
     * @param password
     * @return
     */
    public static String passwordCheck(String password) {
        if (password.length() != 6) {
            return "短信验证码长度不对";
        }

        if (!password.matches("^[a-zA-Z0-9]{6}")) {
            return "验证码有误，请重新输入";
        }

        return CHECK_SUCCESS;
    }
}
